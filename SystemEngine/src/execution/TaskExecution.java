package execution;

import dependency.graph.DependencyGraph;
import dependency.target.Target;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskExecution {
    private Task task;
    private DependencyGraph graphInExecution;
    private Map<Target.TargetStatus,Set<Target>> status2Targets;


    public TaskExecution(DependencyGraph dependencyGraph, Task task) {
        try {
            this.graphInExecution = (DependencyGraph) dependencyGraph.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        this.task = task;
        status2Targets = new HashMap<>();

    }

    private void updateStatus2Target()
    {
        status2Targets.clear();
        for (Target target: graphInExecution.getAllTargets().values())
        {
            status2Targets.get(target.getTargeStatus()).add(target);
        }
    }


    public void runTaskFromScratch()
    {
        Set<String> updatedTargets;
        for (Target target : status2Targets.get(Target.TargetStatus.Waiting))
        {
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
            updateStatus2Target();
        }
    }

}
