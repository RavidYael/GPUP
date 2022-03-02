package utils;

import DTOs.TargetDTO;
import dependency.graph.DependencyGraph;
import dependency.target.Target;
import execution.TargetExecutionSummary;
import DTOs.MissionInfoDTO;

import java.util.*;

public class GraphInExecution {

    private String missionName;
    private DependencyGraph graphInExecution;
    private Map<Target.TargetStatus, Set<Target>> status2Targets;
    private Map<Target, TargetExecutionSummary> target2summary;
    private Map<DependencyGraph.TaskType, Integer> taskPricing;
    private String targetsSummaryDir;
    private Long totalDuration = 0L;
    private Map<String,Integer> user2executedTargets;
    private MissionInfoDTO.MissionStatus missionStatus;

    public void setMissionStatus(MissionInfoDTO.MissionStatus missionStatus) {
        this.missionStatus = missionStatus;
    }


    private Object CalculationLock = new Object();

    public GraphInExecution(String missionName,Set<String> targetsToExecute, DependencyGraph graphTheMissionDefinedUpon, String uploader) {

      //  this.graphInExecution = createSubGraphForMission(missionPreferences,graphTheMissionDefinedUpon);
        this.graphInExecution = graphTheMissionDefinedUpon.getSubGraphFromTargets(targetsToExecute);
        graphInExecution.setGraphName(missionName);
        graphInExecution.setUploaderName(uploader);

        this.missionName = missionName;
        this.graphInExecution.setTaskPricing(graphTheMissionDefinedUpon.getTaskPricing());

        status2Targets = new HashMap<>();
        status2Targets.put(Target.TargetStatus.Frozen, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Skipped, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Waiting, new HashSet<>());
        status2Targets.put(Target.TargetStatus.InProcess, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Finished, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Done, new HashSet<>());
        user2executedTargets = new HashMap<>();
        graphInExecution.initializeWaitingTargets();
        updateStatus2Target();
        initializeTarget2summary();

    }

    public DependencyGraph getGraphInExecution() {
        return graphInExecution;
    }

    public Integer getNumOfExecutedTargetsForUser(String userName) {
        return user2executedTargets.get(userName);
    }




    private void initializeTarget2summary() {
        target2summary = new HashMap<>();
        for (Target target : graphInExecution.getAllTargets().values()) {
            TargetExecutionSummary defaultSummary = new TargetExecutionSummary(target.getName(), target.getTaskResult(), null);
            target2summary.put(target, defaultSummary);
        }
    }

//    private void updateTarget2summary() {
//        for (Target target : graphInExecution.getAllTargets().values()) {
//            target2summary.get(target).setTaskResult(target.getTaskResult());
//        }
//    }

    private void clearStatus2Targets() {
        for (Target.TargetStatus targetStatus : status2Targets.keySet()) {
            if (targetStatus != Target.TargetStatus.Done) {
                status2Targets.get(targetStatus).clear();
            }
        }
    }

    private void updateStatus2Target() {
        System.out.println("i am updating status2Targets");
        clearStatus2Targets();
        for (Target target : graphInExecution.getAllTargets().values()) {
            status2Targets.get(target.getTargetStatus()).add(target);
        }
    }

    public void incrementTargetExecutedForUser(String userName){
       Integer curCount = user2executedTargets.computeIfAbsent(userName,t-> 0);
       user2executedTargets.put(userName,curCount+1);

    }



    public MissionInfoDTO.MissionStatus updateEffectOfTargetsExecution(TargetDTO executedTarget){

        synchronized (CalculationLock) {

            Target curTarget = graphInExecution.getTargetByName(executedTarget.getName());
            curTarget.updateTargetByDTO(executedTarget);

                if (curTarget.getTargetStatus() == Target.TargetStatus.Finished) {
                    if (curTarget.getTaskResult() == Target.TaskResult.Success || curTarget.getTaskResult() == Target.TaskResult.Warning) {
                        setAndUpdateTargetSuccess(curTarget);
                    } else if (curTarget.getTaskResult() == Target.TaskResult.Failure) {
                        setAndUpdateTargetFailure(curTarget);
                    }
                }
            }
            updateStatus2Target();

        printGraphAndStatus();

        if (status2Targets.get(Target.TargetStatus.Finished).size() + status2Targets.get(Target.TargetStatus.Done).size() + status2Targets.get(Target.TargetStatus.Skipped).size() == graphInExecution.getAllTargets().values().size()  ){
            return MissionInfoDTO.MissionStatus.finished;
        }
        else return MissionInfoDTO.MissionStatus.running;
    }

    private void printGraphAndStatus() {
        for (Target curTarget :graphInExecution.getAllTargets().values()
             ) {
            System.out.println(curTarget.getName() + " " + curTarget.getTargetStatus());
        }
    }


    public Set<String> setAndUpdateTargetSuccess(Target target) {
        Set<String> waitingTargets = new HashSet<>();
        target.setTargetStatus(Target.TargetStatus.Finished);
        for (String targetName : target.getRequiredFor()) {
            Target t = graphInExecution.getTargetByName(targetName);
            t.getDependsOn().remove(target.getName());
            if (t.getDependsOn().isEmpty()) {
                t.setTargetStatus(Target.TargetStatus.Waiting);
                waitingTargets.add(t.getName());
            }
        }
        return waitingTargets;
    }

    public Set<String> setAndUpdateTargetFailure(Target target) {
        Set<String> skippedTargets = new HashSet<>();
        for (String targetName : target.getRequiredFor()) {
            Target parentTarget = graphInExecution.getTargetByName(targetName);
            parentTarget.setTargetStatus(Target.TargetStatus.Skipped);
            skippedTargets.add(parentTarget.getName());
            setAndUpdateTargetFailure(parentTarget);
        }
        return skippedTargets;
    }

}
