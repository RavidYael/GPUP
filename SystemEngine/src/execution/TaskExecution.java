package execution;

import dependency.graph.DependencyGraph;
import dependency.target.Target;

import java.util.*;
import java.util.stream.Collectors;

public class TaskExecution {
    private Task task;
    private DependencyGraph graphInExecution;
    private Map<Target.TargetStatus,Set<Target>> status2Targets;


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



//    public void runTaskFromScratch()
//    {
//        Set<String> updatedTargets;
//
//        for (Target target : status2Targets.get(Target.TargetStatus.Waiting))
//        {
//            Target.TaskResult result = task.runTaskOnTarget(target);
//            target.setTaskResult(result);
//            if(result == Target.TaskResult.Success || result == Target.TaskResult.Warning)
//            {
//               updatedTargets =  graphInExecution.setAndUpdateTargetSuccess(target);
//                if (!updatedTargets.isEmpty())
//                    System.out.println("The following targets are now ready to process: " + updatedTargets.toString());
//            }
//            else if (result == Target.TaskResult.Failure)
//            {
//                 updatedTargets = graphInExecution.setAndUpdateTargetFailure(target);
//                if (!updatedTargets.isEmpty())
//                System.out.println("The following targets now cannot be processed: " +updatedTargets.toString());
//            }
//            target.setTargetStatus(Target.TargetStatus.Finished);
//            updateStatus2Target();
//        }
//    }
//
//}

    public void runTaskFromScratch()
    {
        Set<String> updatedTargets;
        Iterator<Target> iter = status2Targets.get(Target.TargetStatus.Waiting).iterator();
        while(iter.hasNext())
        {
            Target target = iter.next();
            Target.TaskResult result = task.runTaskOnTarget(target);
            target.setTaskResult(result);
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

        printExecutionSummary();
        //TODO print to log file


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
        System.out.println(status2Targets.get(Target.TargetStatus.Finished).stream().filter(t -> t.getTaskResult() == Target.TaskResult.Success).count() + " Targets succeeded");
        System.out.println(status2Targets.get(Target.TargetStatus.Finished).stream().filter(t-> t.getTaskResult() == Target.TaskResult.Failure).count()+ " Targets Failed");
        System.out.println(status2Targets.get(Target.TargetStatus.Finished).stream().filter(t-> t.getTaskResult() == Target.TaskResult.Failure).collect(Collectors.toSet()).toString());
        System.out.println(status2Targets.get(Target.TargetStatus.Finished).stream().filter(t-> t.getTaskResult() == Target.TaskResult.Warning).count() + " Targets succeeded with warning");
        System.out.println(status2Targets.get(Target.TargetStatus.Skipped).size() + " Targets skipped");




    }
    private boolean isExecutionComplete(){
        return status2Targets.get(Target.TargetStatus.Done).size() == graphInExecution.getAllTargets().size();

    }
}

