package console;

import dependency.graph.DependencyGraph;
import dependency.graph.GraphFactory;
import dependency.target.Target;
import execution.SimulationTask;
import execution.Task;
import execution.TaskExecution;
import io.InputCommunicator;
import io.OutputCommunicator;

import java.util.Scanner;

public class ConsloeAppUI implements OutputCommunicator, InputCommunicator {

    private DependencyGraph dependencyGraph;



    @Override
    public void LoadFromFile(String directory) throws Exception {
            dependencyGraph =  GraphFactory.newGraphWithData(directory);

    }

    @Override
    public String getInputFromUser() {
        Scanner s = new Scanner(System.in);
        String res = s.next();
        return res;
    }


    @Override
    public void displayGraphInformation() {
        System.out.println("There are " + dependencyGraph.getAllTargets().size() + "targets");
        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Root) + "are roots");
        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Middle) + "are middles");
        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Leaf) + "are leafes");
        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Independed) + "are independents");

    }
    private Target getAndVerifyTargetByName(String targetName) {
        Target resTarget = dependencyGraph.getTargetByName(targetName);
        while (resTarget == null) {
            System.out.println("target does not exists in Graph, please try again, or enter # to go back.");
            String name = getInputFromUser();
            if (name.equals("#")) return null;
            resTarget = dependencyGraph.getTargetByName(name);
        }
        return resTarget;

    }
    private Target.Dependency getAndVerifyDependencyByString(String dependencyName)
    {
        while(!dependencyName.equals("DependsOn") && !dependencyName.equals("RequiredFor"))
        {
            System.out.println("dependency type invalid, should be 'RequiredFor' or 'DependsOn',  (enter '#' to go back)");
            String newName = getInputFromUser();
            if (newName.equals("#")) return null;
        }
        if (dependencyName.equals("DependsOn"))
            return Target.Dependency.DependsOn;
        else
            return Target.Dependency.RequiredFor;
    }


    @Override
    public void displayTargetInformation(String targetName) {

       Target dispTarget = getAndVerifyTargetByName(targetName);
       if (dispTarget == null) return;

        System.out.println("Target name:" + dispTarget.getName() + "\n" +
                "Target dependecy level in graph is: " + dispTarget.getDependencyLevel() + "\n");

        if (dispTarget.getDependsOn().size() == 0)
            System.out.println("Target isn't directly depended on any other target");
        else
            System.out.println("Target depends directly on: " + dispTarget.getDependsOn().toString());

        if (dispTarget.getRequiredFor().size() == 0)
            System.out.println("Target isn't needed for any other Target");
        else
            System.out.println("Target is needed for: " + dispTarget.getRequiredFor().toString());
        }

    @Override
    public void runTask() {
        SimulationTask task;
        boolean random;
        System.out.println("How long will the task run? (in ms)");
        int processTime = Integer.parseInt(getInputFromUser());
        System.out.println("Is task run time Random? 'y'/'n'" );
        random = getInputFromUser().equals("y");

        System.out.println("what is the probability of success?");
        float probSuccess = Float.parseFloat(getInputFromUser());

        System.out.println("what is the probability of success with warning?");
        float probWarning = Float.parseFloat(getInputFromUser());

        task = new SimulationTask(processTime,random,probSuccess,probWarning);
        TaskExecution taskExecution = new TaskExecution(dependencyGraph,task);
        taskExecution.runTaskFromScratch();


    }

    @Override
    public void displayPathBetweenTwoTargets(String name1, String name2, String dependencyTypeStr) {
        Target t1 = getAndVerifyTargetByName(name1);
        Target t2 = getAndVerifyTargetByName(name2);
        if (t1 == null || t2 == null) return;
        Target.Dependency dependencyType = getAndVerifyDependencyByString(dependencyTypeStr);
        if (dependencyType == null) return;
        dependencyGraph.displayAllPathsBetweenTwoTargets(t1,t2,dependencyType);




    }
}

