package console;

import io.InputCommunicator;
import io.OutputCommunicator;

import java.util.Scanner;

public class GpupMenu {

    OutputCommunicator outputCommunicator;
    InputCommunicator inputCommunicator;

    public GpupMenu( ) {
        this.outputCommunicator = new ConsloeAppUI();
        this.inputCommunicator = new ConsloeAppUI();
    }

    public void printMenu()
    {
        System.out.println("Please choose an option:");
        System.out.println(
                         "1. Read system info from file\n" +
                        "2. Display dependency graph info\n"+
                        "3. Display specific target info\n" +
                        "4. Find path between two targets\n"+
                        "5. Run task\n" +
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
                case 1: // load file
                    String directory = inputCommunicator.getInputFromUser();
                    try {
                        inputCommunicator.LoadFromFile(directory);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                        System.out.println("Please try again");
                        break;
                    }
                    caseCount++;
                    break;

                case 2: // Display dependency graph info
                    if (caseCount >= 1) {
                        outputCommunicator.displayGraphInformation();
                    }
                    else System.out.println("Please load file First");
                    break;

                case 3: //Display specific target info
                    if (caseCount >=1) {
                        System.out.println("Please enter name of target, or '#' to go back");
                        String targetName = inputCommunicator.getInputFromUser();
                        if (targetName.equals("#")) break;
                        outputCommunicator.displayTargetInformation(targetName);
                    }

                    else {
                        System.out.println("Please load file First");
                        break;
                    }


                case 4: // Find path between two targets
                    System.out.println("Please enter names of 2 targets, or # to go back");
                    String name1 = inputCommunicator.getInputFromUser();
                    if (name1.equals("#")) break;

                    String name2 = inputCommunicator.getInputFromUser();
                    if (name2.equals("#")) break;


                    System.out.println("Plese enter dependency between said targets: 'DependsOn' or 'RequiredFor' ");
                    String dependencyType = inputCommunicator.getInputFromUser();
                    if (dependencyType.equals("#")) break;


                    outputCommunicator.displayPathBetweenTwoTargets(name1,name2,dependencyType);


                case 7:

                    break;


            }

            printMenu();
            choice = s.nextInt();
        }
    }
}
