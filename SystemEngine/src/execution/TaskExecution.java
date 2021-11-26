package execution;

import dependency.graph.DependencyGraph;
import dependency.target.Target;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TaskExecution {
    private Task task;
    private DependencyGraph graphInExecution;
    private Map<Target.TargetStatus,Set<Target>> status2Targets;
    private Map<Target,TargetExecutionSummary> target2summary;
    private String targetsSummaryDir;
    private Long totalDuration =0L;


    public TaskExecution(DependencyGraph dependencyGraph, Task task) {
        this.graphInExecution = dependencyGraph.createDeepCopy();
        this.task = task;
        status2Targets = new HashMap<>();
        status2Targets.put(Target.TargetStatus.Frozen,new HashSet<>());
        status2Targets.put(Target.TargetStatus.Skipped,new HashSet<>());
        status2Targets.put(Target.TargetStatus.Waiting,new HashSet<>());
        status2Targets.put(Target.TargetStatus.InProcess,new HashSet<>());
        status2Targets.put(Target.TargetStatus.Finished,new HashSet<>());
        status2Targets.put(Target.TargetStatus.Done,new HashSet<>());
        updateStatus2Target();
        initializeTarget2summary();
    }

    private void createTaskWorkingDirectory(){
        String timeStamp = getTimeStamp("dd.MM.yyyy HH.mm.ss");
        String workingDirStr =  graphInExecution.getWorkingDir();
        String taskDirStr = workingDirStr + "\\" + task.getTaskName() +" " + timeStamp;
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

    private void initializeTarget2summary(){
        target2summary = new HashMap<>();
        for (Target target : graphInExecution.getAllTargets().values()){
            TargetExecutionSummary defaultSummary = new TargetExecutionSummary(target.getName(),target.getTaskResult(),null);
            target2summary.put(target,defaultSummary);
        }
    }

    private void updateTarget2summary(){
        for (Target target : graphInExecution.getAllTargets().values()){
            target2summary.get(target).setTaskResult(target.getTaskResult());
        }
    }

    private void clearStatus2Targets() {
        for (Target.TargetStatus targetStatus : status2Targets.keySet()){
            if (targetStatus != Target.TargetStatus.Done) {
                status2Targets.get(targetStatus).clear();
            }
        }
    }

    private void updateStatus2Target()
    {
      clearStatus2Targets();
        for (Target target: graphInExecution.getAllTargets().values())
        {
            status2Targets.get(target.getTargeStatus()).add(target);
        }
    }




    private String getTimeStamp(String format){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(format);
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }

    public void runTaskFromScratch()
    {
        createTaskWorkingDirectory();
        Set<String> updatedTargets;
        Iterator<Target> iter = status2Targets.get(Target.TargetStatus.Waiting).iterator();
        while(iter.hasNext())
        {
            Target target = iter.next();
            Instant start = Instant.now();
            Target.TaskResult result = task.runOnTarget(target);
            Instant finish = Instant.now();
            Duration duration = Duration.between(start,finish);
            Long s = duration.getSeconds();
            target2summary.get(target).setDuration(s);
            target.setTaskResult(result);
            incrementTotalDuration(s);
            if(result == Target.TaskResult.Success || result == Target.TaskResult.Warning)
            {
                updatedTargets =  graphInExecution.setAndUpdateTargetSuccess(target);
                if (!updatedTargets.isEmpty())
                    System.out.println("The following targets are now ready to process: " + updatedTargets.toString());
            }
            else if (result == Target.TaskResult.Failure)
            {
                updatedTargets = graphInExecution.setAndUpdateTargetFailure(target);
                if (!updatedTargets.isEmpty())
                    System.out.println("The following targets now cannot be processed: " +updatedTargets.toString());
            }
            target.setTargetStatus(Target.TargetStatus.Finished);
            updateStatus2Target();
            iter = status2Targets.get(Target.TargetStatus.Waiting).iterator();
        }


        updateTarget2summary();
        printExecutionSummary();
        printTargetExecutionSummary();

    }

    private void printTargetExecutionSummary() {
   for(Target curTarget : target2summary.keySet())
   {
       try {
           PrintStream printStream = new PrintStream(new File(targetsSummaryDir+"\\"+ curTarget.getName() +".log"));
           printSimultaniously(target2summary.get(curTarget),System.out,printStream);
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }

   }

    }

    private void printSimultaniously(Object obj, PrintStream out1, PrintStream out2) {
    out1.println(obj);
    out2.println(obj);
    }

    public void runTaskIncrementally(){

        graphInExecution.updateTargetStatusForIncrementalExecution();
        updateStatus2Target();

        if (isExecutionComplete()) {
            System.out.println("All targets were executed successfully! nothing left to run :)");
            return;
        }

        runTaskFromScratch();

    }

    private void printExecutionSummary(){
        System.out.println("Task completed");
        String runTime = String.format("%02d:%02d:%02d", totalDuration / 3600, (totalDuration % 3600) / 60, (totalDuration % 60));
        System.out.println("Task ran for: " +runTime);
        System.out.println(status2Targets.get(Target.TargetStatus.Finished).stream().filter(t -> t.getTaskResult() == Target.TaskResult.Success).count() + " Targets succeeded");
        System.out.println(status2Targets.get(Target.TargetStatus.Finished).stream().filter(t-> t.getTaskResult() == Target.TaskResult.Failure).count()+ " Targets Failed");
        System.out.println(status2Targets.get(Target.TargetStatus.Finished).stream().filter(t-> t.getTaskResult() == Target.TaskResult.Failure).collect(Collectors.toSet()).toString());
        System.out.println(status2Targets.get(Target.TargetStatus.Finished).stream().filter(t-> t.getTaskResult() == Target.TaskResult.Warning).count() + " Targets succeeded with warning");
        System.out.println(status2Targets.get(Target.TargetStatus.Skipped).size() + " Targets skipped");

    }

    private boolean isExecutionComplete(){
        return status2Targets.get(Target.TargetStatus.Done).size() == graphInExecution.getAllTargets().size();

    }

    private void incrementTotalDuration(Long targetDuration) {
        totalDuration += targetDuration;
    }
}

