package utils;

import dependency.graph.DependencyGraph;
import dependency.target.Target;
import execution.TargetExecutionSummary;
import utils.ProccecesDTOs.MissionInfoDTO;

import java.util.*;

public class GraphInExecution {

    private String missionName;
    private DependencyGraph graphInExecution;
    private Map<Target.TargetStatus, Set<Target>> status2Targets;
    private Map<Target, TargetExecutionSummary> target2summary;
    private String targetsSummaryDir;
    private Long totalDuration = 0L;

    public GraphInExecution(MissionInfoDTO missionPreferences, DependencyGraph graphTheMissionDefinedUpon) {

        this.graphInExecution = createSubGraphForMission(missionPreferences,graphTheMissionDefinedUpon);
        missionName = missionPreferences.getMissionName();

        status2Targets = new HashMap<>();
        status2Targets.put(Target.TargetStatus.Frozen, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Skipped, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Waiting, new HashSet<>());
        status2Targets.put(Target.TargetStatus.InProcess, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Finished, new HashSet<>());
        status2Targets.put(Target.TargetStatus.Done, new HashSet<>());
        updateStatus2Target();
        initializeTarget2summary();
        graphInExecution.initializeWaitingTargets();

    }

    public DependencyGraph getGraphInExecution() {
        return graphInExecution;
    }


    //TODO IM ASSUMING THAT A MISSION DEFINED UPON ONE GRAPH ONLY

    public DependencyGraph createSubGraphForMission(MissionInfoDTO missionPreferences, DependencyGraph graphTheMissionDefinedUpon){

        DependencyGraph subGraph = new DependencyGraph();

        for (String targetName : missionPreferences.getTargetsToExecute()){

            Target curTarget = graphTheMissionDefinedUpon.getTargetByName(targetName);

            subGraph.addTargetToGraph(targetName , curTarget);

        }

        subGraph.filterTargetDependendies();
        subGraph.updateAllTargetDependencyLevel();

        // TODO IS IT STILL RELEVANT?
        //subGraph.setName2SerialSet(dependencyGraph.getName2SerialSet()); // not logically true

        return subGraph;
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

    public void updateEffectOfTargetsExecution(Set<Target> executedTargets){
        System.out.println("i am in updateEffectedTargets");
        Iterator<Target> curTargetIter = executedTargets.iterator();
        while(curTargetIter.hasNext()){
            Target curTarget = curTargetIter.next();
            if(curTarget.getTargetStatus() == Target.TargetStatus.Finished) {
                if (curTarget.getTaskResult() == Target.TaskResult.Success || curTarget.getTaskResult() == Target.TaskResult.Warning) {
                    setAndUpdateTargetSuccess(curTarget);
                } else if (curTarget.getTaskResult() == Target.TaskResult.Failure) {
                    setAndUpdateTargetFailure(curTarget);
                }
            }
            curTargetIter.remove();
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