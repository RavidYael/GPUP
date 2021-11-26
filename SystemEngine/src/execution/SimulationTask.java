package execution;

import dependency.target.Target;

import java.io.Serializable;
import java.util.Random;

public class SimulationTask  extends Task implements Serializable {
    private String taskName = "Simulation";
    private int processTime;
    private boolean isRandomTime;
    private float successProb;
    private float successWithWarningProb;

    public SimulationTask(int processTime, boolean isRandomTime, float successProb, float successWithWarningProb) {

        this.isRandomTime = isRandomTime;
        if (isRandomTime)
        {
            this.processTime = new Random().nextInt(processTime);
        }
        else
            this.processTime = processTime;
        this.successProb = successProb;
        this.successWithWarningProb = successWithWarningProb;
    }

    @Override
    public Target.TaskResult runOnTarget(Target target) {
        Target.TaskResult resStatus;
        target.setTargetStatus(Target.TargetStatus.InProcess);
        double rand = new Random().nextDouble();
        System.out.println("Target " + target.getName() + " is now processing ");
        System.out.println("Target information: " + target.getData());

        try {
            Thread.sleep(processTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("Target " +target.getName()+ " completed with status: ");
        if (rand < successProb)
        {
            double newRand = new Random().nextDouble();
            if (newRand < successWithWarningProb) {
                resStatus = Target.TaskResult.Warning;
                System.out.println("Warning.");

            }
            else {
                resStatus = Target.TaskResult.Success;
                System.out.println("Success.");

            }

        }
        else {
            resStatus = Target.TaskResult.Failure;
            System.out.println("Failure.");

        }
        System.out.println();
        return resStatus;

    }

    @Override
    public String getTaskName() {
        return taskName;
    }

}
