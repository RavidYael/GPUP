package UI;


import dependency.graph.DependencyGraph;
import dependency.graph.target.Target;
import engine.jaxb.generated.GPUPDescriptor;
import engine.validation.GraphExtractor;
import engine.validation.GraphValidator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class GpupMenu {

    public static enum menuOptions { Dummy,LoadFile, DisplayGraph,DisplayTarget,FindPath,RunTask,CheckCycle,Exit}

    private DependencyGraph dependencyGraph;

    public GpupMenu(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
    }

    public void printMenu()
    {
        System.out.println("Please choose an option:");
        System.out.println(
                "1. Read system info from file\n" +
                        "2. Display dependency graph info\n"+
                        "3. Display specific target info\n" +
                        "4. Find path between two targets\n"+
                        "5. Run task\n"+
                        "6. Check if target is in a cycle\n" +
                        "7. Exit \n");


    }

    public void executeMenu() {
        int choice;
        printMenu();
        Scanner s = new Scanner(System.in);
        choice = s.nextInt();
        int caseCount = 0;
        while (choice != 7) {
            switch (choice) {
                case 1:
                    System.out.println("please enter full file path or press '#' to go back");
                    String directory = getInputFromUser();
                    Path path = Paths.get(directory);
                    GPUPDescriptor generatedGraph = null;
                    try {
                        generatedGraph = new GraphExtractor(path).getGeneratedGraph();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    GraphValidator graphValidator = new GraphValidator(generatedGraph);
                    if( graphValidator.startValidation())
                    {
                        // build actual graph
                    }


                    caseCount++;
                    break;

                case 2:
                   // if (caseCount >= 1) {
                        System.out.println("There are " + dependencyGraph.getAllTargets().size() + "targets");
                        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Root) + "are roots");
                        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Middle) + "are middles");
                        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Leaf) + "are leafes");
                        System.out.println(dependencyGraph.getTargetsCountByLevel(Target.DependencyLevel.Independed) + "are independents");
                   // }
                   // else System.out.println("Please load file First");

                    break;
                case 3:
                   // if (caseCount >=1) {
                        System.out.println("Please enter name of target, or '#' to go back");
                        String targetName = getInputFromUser();
                        if (targetName == null) break;
                        Target userTarget = dependencyGraph.getTargetByName(targetName);
                        while (userTarget == null) {
                            System.out.println("No such target exists, please try again");
                            targetName = getInputFromUser();
                            if (targetName == null) break;
                            userTarget = dependencyGraph.getTargetByName(targetName);
                        }
                        System.out.println("Target name:" + userTarget.getName() + "\n" +
                                "Target dependecy level in graph is: " + userTarget.getDependencyLevel() + "\n");

                        if (userTarget.getDependsOn().size() == 0)
                            System.out.println("Target isn't directly depended on any other target");
                        else
                            System.out.println("Target depends directly on: " + userTarget.getDependsOn().toString());

                        if (userTarget.getRequiredFor().size() == 0)
                            System.out.println("Target isn't needed for any other Target");
                        else
                            System.out.println("Target is needed for: " + userTarget.getRequiredFor().toString());
                        break;
                  //  }
                   // else System.out.println("Please load file First");


                case 4:
                case 7:
                    break;


            }
            printMenu();
            choice = s.nextInt();
        }
    }

    private String getInputFromUser()
    {
        Scanner s = new Scanner(System.in);
        String res = s.next();
        if (res.equals("#"))
        {
            return null;
        }
        else return res;
    }

}