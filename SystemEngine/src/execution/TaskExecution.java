package execution;

import dependency.graph.DependencyGraph;
import dependency.target.Target;
import dependency.target.TargetRunner;
import javafx.application.Platform;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class TaskExecution  implements Serializable, Runnable {
    private GPUPTask GPUPTask;
    private DependencyGraph graphInExecution;
    private Map<Target.TargetStatus, Set<Target>> status2Targets;
    private Map<Target, TargetExecutionSummary> target2summary;
    private String targetsSummaryDir;
    private Long totalDuration = 0L;
    private int curNumOfThreads;
    private ThreadPoolExecutor threadPoolExecutor;
    private Boolean pauseStatus = false;
    private Boolean donePausing = false;
    private Consumer taskInfoConsumer;
    private Boolean stopWorkSyncer = false;
    private Long executionStart;

    public void setCurNumOfThreads(int maxPerl) {
        this.threadPoolExecutor.setCorePoolSize(maxPerl);
        this.threadPoolExecutor.setMaximumPoolSize(maxPerl);
    }

    public TaskExecution(DependencyGraph graphInExecution, int numOfTrheads, GPUPTask GPUPTask, Consumer consumer) {
        this.curNumOfThreads = numOfTrheads;
        this.graphInExecution = graphInExecution;
        this.GPUPTask = GPUPTask;
        this.taskInfoConsumer = consumer;
        this.threadPoolExecutor = new ThreadPoolExecutor(curNumOfThreads,graphInExecution.getMaxParallelism(),1000, MINUTES,new LinkedBlockingQueue<Runnable>());

        GPUPTask.setExecutionManager(this);

        status2Targets = new HashMap<>();
        status2Targets.put(Target.TargetStatus.Frozen, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Skipped, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Waiting, new HashSet<>());
        status2Targets.put(Target.TargetStatus.InProcess, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Finished, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Done, new HashSet<>());
        updateStatus2Target();
        initializeTarget2summary();
        graphInExecution.initializeWaitingTargets();

    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setGPUPTask(execution.GPUPTask GPUPTask) {
        this.GPUPTask = GPUPTask;
    }

    private void createTaskWorkingDirectory() {
        String timeStamp = getTimeStamp("dd.MM.yyyy HH.mm.ss");
        String workingDirStr = graphInExecution.getWorkingDir();
        String taskDirStr = workingDirStr + "\\" + GPUPTask.getTaskName() + " " + timeStamp;
        File taskDirectory = new File(taskDirStr);
        Path path = Paths.get(taskDirStr);
        this.targetsSummaryDir = path.toString();
        if (!taskDirectory.exists()) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeTarget2summary() {
        target2summary = new HashMap<>();
        for (Target target : graphInExecution.getAllTargets().values()) {
            TargetExecutionSummary defaultSummary = new TargetExecutionSummary(target.getName(), target.getTaskResult(), null);
            target2summary.put(target, defaultSummary);
        }
    }

    private void updateTarget2summary() {
        for (Target target : graphInExecution.getAllTargets().values()) {
            target2summary.get(target).setTaskResult(target.getTaskResult());
        }
    }

    private void clearStatus2Targets() {
        for (Target.TargetStatus targetStatus : status2Targets.keySet()) {
            if (targetStatus != Target.TargetStatus.Done) {
                status2Targets.get(targetStatus).clear();
            }
        }
    }

    private void updateStatus2Target() {
       // System.out.println("i am updating status2Targets");
        clearStatus2Targets();
        for (Target target : graphInExecution.getAllTargets().values()) {
            status2Targets.get(target.getTargetStatus()).add(target);
        }
    }


    private String getTimeStamp(String format) {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(format);
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }

    @Override
    public void run() {
        //if(GPUPTask.getTaskName() == GPUPTask.TaskNames.Simulation){
        runTaskFromScratch();
//כאן צריכה להיות לוגיקה שאומרת לו איזה סוג מסימה לבצע, אפשר להוסיף את זה כenum בGPUPTask
        // }
    }

    public void runTaskFromScratch() {
        executionStart = System.currentTimeMillis();
        this.pauseStatus = false;
        this.donePausing = false;


        GPUPTask.setTotalWork(Long.valueOf(graphInExecution.getAllTargets().size()));
        GPUPTask.startWork();
    //    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThreads);
        Set<Target> executedTargets = new HashSet<>();
        createTaskWorkingDirectory();
        Set<String> updatedTargets;
        Iterator<Target> iter = status2Targets.get(Target.TargetStatus.Waiting).iterator();
        Boolean hasMoreToExecute = true;
        List<Future> futureRes = new ArrayList<>();

        while (hasMoreToExecute) {
            synchronized (stopWorkSyncer) {
                while (pauseStatus) {
                    try {
                        stopWorkSyncer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (iter.hasNext()) {
                Target target = iter.next();
                if (!graphInExecution.isTargetBlocked(target, executedTargets)) {
                TargetRunner targetRunner = new TargetRunner(target, GPUPTask, graphInExecution, this);
                executedTargets.add(target);
                futureRes.add(threadPoolExecutor.submit(targetRunner));
            }

            else
            {
               Platform.runLater(()->taskInfoConsumer.accept("Target: " + target.getName() +"is blocked, trying next available target"));
                continue;
            }

                }

            else {

                for (Future taskResult : futureRes) {

                    if (pauseStatus) {
                        break;
                    }

                    try {
                        taskResult.get(10l, SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
//
//                if (!pauseStatus) {
                graphInExecution.updateEffectOfTargetsExecution(executedTargets);
                updateStatus2Target();
                iter = status2Targets.get(Target.TargetStatus.Waiting).iterator();
                if (!iter.hasNext()) {
                    hasMoreToExecute = false;
                }
            }
        }


        GPUPTask.finishWork();
        updateTarget2summary();
        printExecutionSummary();
     //   printTargetExecutionSummary();
    }

        private void printTargetExecutionSummary () {

            for (Target curTarget : target2summary.keySet()) {
                try {
                    PrintStream printStream = new PrintStream(new File(targetsSummaryDir + "\\" + curTarget.getName() + ".log"));
                    printSimultaniously(target2summary.get(curTarget), System.out, printStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }

        private void printSimultaniously (Object obj, PrintStream out1, PrintStream out2){
            out1.println(obj);
            out2.println(obj);
        }

        public void runTaskIncrementally () {

            graphInExecution.updateTargetStatusForIncrementalExecution();
            updateStatus2Target();

            if (isExecutionComplete()) {
                Platform.runLater(() -> taskInfoConsumer.accept("All targets were executed successfully! nothing left to run :)"));
                return;
            }

            runTaskFromScratch();

        }

        private void calculateTotalDuration () {
          //  status2Targets.get(Target.TargetStatus.Finished).stream().forEach(t -> totalDuration += t.getExecutionTime());
        }

        private void printExecutionSummary () {
            totalDuration = System.currentTimeMillis() - executionStart;
            Platform.runLater(() -> taskInfoConsumer.accept("Task completed\n" +
                    "Task ran for: " +
                    totalDuration/1000.0 + " sec\n" +
                    status2Targets.get(Target.TargetStatus.Finished).stream().filter(t -> t.getTaskResult() == Target.TaskResult.Success).count() + " Targets succeeded\n" +
                    String.join(",",status2Targets.get(Target.TargetStatus.Finished).stream().filter(t-> t.getTaskResult().equals(Target.TaskResult.Success)).map(t-> t.getName()).collect(Collectors.toSet()))+"\n"+
                    status2Targets.get(Target.TargetStatus.Finished).stream().filter(t -> t.getTaskResult() == Target.TaskResult.Failure).count() + " Targets Failed:\n" +
                    String.join(",",status2Targets.get(Target.TargetStatus.Finished).stream().filter(t -> t.getTaskResult() == Target.TaskResult.Failure).map(t->t.getName()).collect(Collectors.toSet())) + "\n" +
                    status2Targets.get(Target.TargetStatus.Finished).stream().filter(t -> t.getTaskResult() == Target.TaskResult.Warning).count() + " Targets succeeded with warning\n" +
                    String.join(",",status2Targets.get(Target.TargetStatus.Finished).stream().filter(t-> t.getTaskResult().equals(Target.TaskResult.Warning)).map(t->t.getName()).collect(Collectors.toSet()))+"\n"+
                    status2Targets.get(Target.TargetStatus.Skipped).size() + " Targets skipped" + "\n"+
                    String.join(",",status2Targets.get(Target.TargetStatus.Skipped).stream().map(t-> t.getName()).collect(Collectors.toSet()))));

        }

        private boolean isExecutionComplete () {
            return status2Targets.get(Target.TargetStatus.Done).size() == graphInExecution.getAllTargets().size();

        }

        private void incrementTotalDuration (Long targetDuration){
            totalDuration += targetDuration;
        }

        public void setPauseStatus (Boolean paused) {
            synchronized (stopWorkSyncer){
                pauseStatus = paused;
                if(!paused){
                    this.stopWorkSyncer.notifyAll();
                }
            }
        }


        public Boolean isPaused () {
        return pauseStatus;
        }


    private void pausingTask(List<Future> futureTasks, Set<Target> executedTargets,ThreadPoolExecutor threadPoolExecutor) {

        threadPoolExecutor.shutdownNow();
        try {
            threadPoolExecutor.awaitTermination(  status2Targets.get(Target.TargetStatus.Waiting).size() * 10  ,SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        graphInExecution.updateEffectOfTargetsExecution(executedTargets);
        updateStatus2Target();
        donePausing = true;
    }

    public Boolean getStopWorkSyncer() {
        return stopWorkSyncer;
    }

    public void setStopWorkSyncer(Boolean stopWorkSyncer) {
        this.stopWorkSyncer = stopWorkSyncer;
    }
}

