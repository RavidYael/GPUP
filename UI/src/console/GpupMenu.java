package console;

import execution.SimulationTask;
import io.Communicator;


import java.util.Scanner;

public class GpupMenu {

   private Communicator communicator;

    public GpupMenu() {

        this.communicator = new ConsloeAppUI();

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
                        "7. Save system to file \n" +
                        "8. Exit \n");
    }

    public void executeMenu() {
        String choice;
        printMenu();

        choice = communicator.getInputFromUser();
        int caseCount = 0;
        while (true) {
            switch (choice) {
                case "1": // load file
                    try{
                        communicator.LoadFromFile();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                        System.out.println("Please try again");
                        break;
                    }

                    caseCount++;
                    break;

                case "2": // Display dependency graph info
                    if (caseCount >= 1) {
                        communicator.displayGraphInformation();
                    }
                    else System.out.println("Please load file First");
                    break;

                case "3": //Display specific target info
                    if (caseCount >=1) {
                        System.out.println("Please enter name of target, or '#' to go back");
                        String targetName = communicator.getInputFromUser();
                        if (targetName.equals("#")) break;
                        communicator.displayTargetInformation(targetName);
                        break;
                    }

                    else {
                        System.out.println("Please load file First");
                        break;
                    }


                case "4": // Find path between two targets
                    if (caseCount >=1)
                    communicator.displayPathBetweenTwoTargets();
                    else
                        System.out.println("please load file first");
                    break;

                case "5": // run simulation task
                    if (caseCount >= 1) {
                        communicator.runTask();
                    }
                    else {
                        System.out.println("Please load file First");
                    }
                    break;

                case "6":
                    if (caseCount >= 1) {
                        System.out.println("Please enter name of target, or '#' to go back");
                        String targetName = communicator.getInputFromUser();
                        communicator.isTargetInCycle(targetName);
                    }
                    else
                        System.out.println("Please load File First");

                    break;

                case "7":
                    if (caseCount >=1) {
                        System.out.println("Please enter full path of file to save into");
                        String directory = communicator.getInputFromUser();
                        communicator.saveToFile(directory);
                    }
                    else
                        System.out.println("Please load File First");
                    break;

                case "8":
                    System.out.println("Good bye!!");
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please enter a valid input! \n");
                    break;

            }

            printMenu();
            choice = communicator.getInputFromUser();
        }
    }
}
