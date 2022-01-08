//package console;
//
//import dependency.graph.DependencyGraph;
//import dependency.graph.GraphFactory;
//import dependency.target.Target;
//import execution.SimulationTask;
//import execution.Task;
//import execution.TaskExecution;
//import io.Communicator;
//
//
//import java.io.*;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Scanner;
//
//public class ConsloeAppUI implements Communicator, Serializable {
//
//    private DependencyGraph dependencyGraph;
//    private TaskExecution lastTaskExecution;
//
//    public DependencyGraph getDependencyGraph() {
//        return dependencyGraph;
//    }
//
//    public TaskExecution getLastTaskExecution() {
//        return lastTaskExecution;
//    }
//
//    @Override
//    public void LoadFromFile() throws Exception {
//        String directory;
//        System.out.println("Would you like to load previous system condition? ('y'/'n')");
//        String choice = getInputFromUser();
//        switch(choice) {
//            case "y":
//                System.out.println("Please enter a file path of the system you would like to load ");
//                directory = getInputFromUser();
//                ConsloeAppUI temp = DeSerialize(directory);
//                if (temp != null){
//                    this.dependencyGraph = temp.getDependencyGraph();
//                    this.lastTaskExecution = temp.getLastTaskExecution();
//                }
//                else
//                    System.out.println("System loading failed, please try again");
//                break;
//            case "n":
//                System.out.println("Please enter xml file path:");
//                directory = getInputFromUser();
//                DependencyGraph tempGraph = GraphFactory.newGraphWithData(directory);
//                if (tempGraph != null)
//                    dependencyGraph = tempGraph;
//                break;
//            default:
//                System.out.println("please enter 'y' or 'n'");
//        }
//
//    }
//
//    @Override
//    public void saveToFile(String directory) {
//        File file = new File(directory);
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
//                oos.writeObject(this);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private ConsloeAppUI DeSerialize(String directory){
//        try {
//            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(directory))) {
//                return (ConsloeAppUI)ois.readObject();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    public String getInputFromUser() {
//        Scanner s = new Scanner(System.in);
//        String directory = s.nextLine();
//        return directory;
//    }
//
//
//
//
//    @Override
//    public void displayGraphInformation() {
//        System.out.println("There are " + dependencyGraph.getAllTargets().size() + " targets");
//        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Root) + " are roots");
//        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Middle) + " are middles");
//        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Leaf) + " are leafes");
//        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Independed) + " are independents");
//
//    }
//
//    private Target getAndVerifyTargetByName(String targetName) {
//        Target resTarget = dependencyGraph.getTargetByName(targetName);
//        while (resTarget == null) {
//            System.out.println("Target does not exists in Graph, please try again, or enter # to go back.");
//            String name = getInputFromUser();
//            if (name.equals("#")) return null;
//            resTarget = dependencyGraph.getTargetByName(name);
//        }
//        return resTarget;
//
//    }
//
//    private Target.Dependency getAndVerifyDependencyByString(String dependencyName) {
//        while (!dependencyName.equals("depends on") && !dependencyName.equals("required for")) {
//            System.out.println("Dependency type invalid, should be 'required for' or 'depends on',  (enter '#' to go back)");
//            String newName = getInputFromUser();
//            if (newName.equals("#")) return null;
//        }
//        if (dependencyName.equals("depends on"))
//            return Target.Dependency.DependsOn;
//        else
//            return Target.Dependency.RequiredFor;
//    }
//
//
//    @Override
//    public void displayTargetInformation(String targetName) {
//
//        Target dispTarget = getAndVerifyTargetByName(targetName);
//        if (dispTarget == null) return;
//
//        System.out.println("Target name:" + dispTarget.getName() + "\n" +
//                " Target dependecy level in graph is: " + dispTarget.getDependencyLevel() + "\n");
//
//        if (dispTarget.getDependsOn().size() == 0)
//            System.out.println("Target isn't directly depended on any other target");
//        else
//            System.out.println("Target depends directly on: " + dispTarget.getDependsOn().toString());
//
//        if (dispTarget.getRequiredFor().size() == 0)
//            System.out.println("Target isn't needed for any other Target");
//        else
//            System.out.println("Target is needed for: " + dispTarget.getRequiredFor().toString());
//    }
//
//    @Override
//    public void runTask() {
//        SimulationTask task;
//        boolean random;
//
//        System.out.println("How long will the task run? (in ms)");
//        int processTime = Integer.parseInt(getInputFromUser());
//        System.out.println("Is task run time Random? ('y'/'n')");
//        random = getInputFromUser().equals("y");
//
//        System.out.println("what is the probability of success?");
//        float probSuccess = Float.parseFloat(getInputFromUser());
//
//        System.out.println("what is the probability of success with warning?");
//        float probWarning = Float.parseFloat(getInputFromUser());
//        task = new SimulationTask(processTime, random, probSuccess, probWarning);
//
//
//        System.out.println("would you like to run a task on the previous graph? ('y'/'n') ");
//        String choice = getInputFromUser();
//        if (choice.equals("y")) {
//            if (lastTaskExecution == null) {
//                System.out.println("No previos execution was found, running from scratch");
//                lastTaskExecution = new TaskExecution(dependencyGraph, task);
//                lastTaskExecution.runTaskFromScratch();
//            } else {
//                lastTaskExecution.runTaskIncrementally();
//
//            }
//        } else {
//            lastTaskExecution = new TaskExecution(dependencyGraph, task);
//            lastTaskExecution.runTaskFromScratch();
//        }
//
//
//    }
//
//    @Override
//    public void displayPathBetweenTwoTargets() {
//        System.out.println("Please enter names of first target, or # to go back");
//        String name1 = getInputFromUser();
//        Target t1 = getAndVerifyTargetByName(name1);
//        System.out.println("Please enter names of second target, or # to go back");
//        String name2 = getInputFromUser();
//        Target t2 = getAndVerifyTargetByName(name2);
//        if (t1 == null || t2 == null) return;
//        System.out.println("Plese enter dependency between said targets: 'depends on' or required for' ");
//        String dependencyTypeStr = getInputFromUser();
//        Target.Dependency dependencyType = getAndVerifyDependencyByString(dependencyTypeStr);
//        if (dependencyType == null) return;
//        if (dependencyGraph.displayAllPathsBetweenTwoTargets(t1, t2, dependencyType) == false)
//            System.out.println("No path Found");
//    }
//
//    public void isTargetInCycle(String t) {
//        Target target = getAndVerifyTargetByName(t);
//
//        if (target == null)
//            return;
//
//        if (!dependencyGraph.isTargetInCycle(target)) {
//            System.out.println("the target " + target.getName() + " isn't  in any cycle");
//        }
//    }
//
////    public void saveToFile(String directory){
////        dependencyGraph.
////    }
////}
//}
//
//
