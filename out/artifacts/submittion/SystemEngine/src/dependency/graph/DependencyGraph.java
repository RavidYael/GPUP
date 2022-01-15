package dependency.graph;


import dependency.target.Target;


import java.io.Serializable;
import java.util.*;


public class DependencyGraph implements Serializable {

    private Map<String, Target> allTargets;
    private Map<Target.DependencyLevel, Set<Target>> targetsByDependencyLevel;
    private String workingDir;


    public DependencyGraph() {
        allTargets = new HashMap<>();
        targetsByDependencyLevel = new HashMap<>();
        targetsByDependencyLevel.put(Target.DependencyLevel.Leaf ,new HashSet<>());
        targetsByDependencyLevel.put(Target.DependencyLevel.Middle, new HashSet<>());
        targetsByDependencyLevel.put(Target.DependencyLevel.Root, new HashSet<>());
        targetsByDependencyLevel.put(Target.DependencyLevel.Independed, new HashSet<>());


    }

    public String getWorkingDir() {
        return workingDir;
    }

    public Map<String, Target> getAllTargets() {
        return allTargets;
    }

    public Set<Target> getTargetsByLevel(Target.DependencyLevel level) {
        return targetsByDependencyLevel.get(level);
    }

    public Target getTargetByName(String targetName) {
        return allTargets.get(targetName);
    }

    public Integer getTargetsCountByLevel(Target.DependencyLevel level) {
        return getTargetsByLevel(level).size();
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public boolean displayAllPathsBetweenTwoTargets(Target src, Target dest, Target.Dependency dependedOnOrNeeded) {
        Map<Target, Boolean> isVisited = new HashMap<>();
        for (Target target : allTargets.values()){
            isVisited.put(target,false);
        }

        isVisited.values().forEach(bool -> bool = false);
        ArrayList<String> pathList = new ArrayList<>();
        pathList.add(src.getName());
        Boolean[] flag = new Boolean[]{new Boolean(false)};
        getAllPathsUtils(src, dest, isVisited, pathList, dependedOnOrNeeded, flag);
        return flag[0];
    }

    private void getAllPathsUtils(Target src, Target dest, Map<Target, Boolean> isVisited, ArrayList<String> pathList, Target.Dependency requiredOrNeeded, Boolean[] flag) {

        if (src.equals(dest)) {
            System.out.println(pathList.toString());
            flag[0] = true; // May Not Work
        }

        isVisited.put(src, true);
        Set<String> curSet = src.getDependsOnOrNeededFor(requiredOrNeeded);
        for (String targetName : curSet) {
            Target t = getTargetByName(targetName);
            if (!isVisited.isEmpty()) {
                if (isVisited.get(t) == false) {
                    pathList.add(targetName);
                    getAllPathsUtils(t, dest, isVisited, pathList, requiredOrNeeded, flag);

                    pathList.remove(targetName);
                }
            }

            isVisited.put(src, false);

        }
    }

    public void setAllTargets(Map<String, Target> allTargets) {
        this.allTargets = allTargets;
    }
    public void updateAllTargetDependencyLevel(Map<Target.DependencyLevel, Set<Target>> targetsByDependencyLevel) {
        this.targetsByDependencyLevel = targetsByDependencyLevel;
    }

    public void addTargetToGraph(String name, Target toAdd) {
        allTargets.put(name, toAdd);
    }

    public void updateAllTargetDependencyLevel() {
        boolean noDepends, noRequired;
        for (Target target :
                allTargets.values()) {
            noDepends = target.getDependsOn().isEmpty();
            noRequired = target.getRequiredFor().isEmpty();
            if (noDepends && noRequired) {
                targetsByDependencyLevel.get(Target.DependencyLevel.Independed).add(target);
                target.setDependencyLevel(Target.DependencyLevel.Independed);
                target.setTargetStatus(Target.TargetStatus.Waiting);
            } else if (noDepends && !noRequired) {
                targetsByDependencyLevel.get(Target.DependencyLevel.Leaf).add(target);
                target.setDependencyLevel(Target.DependencyLevel.Leaf);
                target.setTargetStatus(Target.TargetStatus.Waiting);
            } else if (!noDepends && noRequired) {
                targetsByDependencyLevel.get(Target.DependencyLevel.Root).add(target);
                target.setDependencyLevel(Target.DependencyLevel.Root);
            } else if (!noDepends && !noRequired) {
                targetsByDependencyLevel.get(Target.DependencyLevel.Middle).add(target);
                target.setDependencyLevel(Target.DependencyLevel.Middle);

            }


        }
    }
    public Set<String> setAndUpdateTargetSuccess(Target target) {
        Set<String> waitingTargets = new HashSet<>();
        target.setTargetStatus(Target.TargetStatus.Finished);
        for (String targetName : target.getRequiredFor()) {
            Target t = allTargets.get(targetName);
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
            Target parentTarget = getTargetByName(targetName);
            parentTarget.setTargetStatus(Target.TargetStatus.Skipped);
            skippedTargets.add(parentTarget.getName());
            setAndUpdateTargetFailure(parentTarget);
        }
        return skippedTargets;
    }
    public void fixTargetsDependencies()
    {
        for (Target target : allTargets.values())
        {
            for (String depends : target.getDependsOn())
            {
                allTargets.get(depends).addToRequiredFor(target.getName());
            }
            for(String required : target.getRequiredFor()){
                allTargets.get(required).addToDependsOn(target.getName());
            }
        }
    }

    public void updateTargetStatusForIncrementalExecution(){
        for (Target target : allTargets.values()){
            if (target.getTaskResult() == Target.TaskResult.Failure)
                target.setTargetStatus(Target.TargetStatus.Waiting);
            else if(target.getTargeStatus() == Target.TargetStatus.Skipped){
                target.setTargetStatus(Target.TargetStatus.Frozen);
            }
            else if(target.getTaskResult() == Target.TaskResult.Success){
                target.setTargetStatus(Target.TargetStatus.Done);
            }
        }
    }

    public DependencyGraph createDeepCopy() {
        DependencyGraph copyGraph = new DependencyGraph();
        copyGraph.setWorkingDir(this.workingDir);
        for (String targetName : allTargets.keySet()){
            copyGraph.allTargets.put(targetName,new Target(allTargets.get(targetName)));
        }

        copyGraph.updateAllTargetDependencyLevel();
        return copyGraph;

    }

    public boolean isTargetInCycle(Target investigatedTarget)
    {

        Map<String, Boolean> isVisited = new HashMap<>();

        for (Target target : allTargets.values()){
            isVisited.put(target.getName(),false);
        }

        List<String> traverse = new ArrayList<>();
        traverse.add(investigatedTarget.getName());

        for (String curTarget : investigatedTarget.getDependsOn())
            if (isCyclicUtil(investigatedTarget,curTarget, isVisited,traverse))
                return true;

        return false;
    }

    private boolean isCyclicUtil(Target investigatedTarget,String curTarget,Map<String,Boolean> isVisited,List<String> theTraverse) {

        if (isVisited.get(curTarget)) {
            theTraverse.remove(curTarget);
            return false;
        }

        theTraverse.add(curTarget);

        if (getTargetByName(curTarget) == investigatedTarget) {
            System.out.println(theTraverse);
            return true;
        }

        isVisited.put(curTarget, true);

        Set<String> curTargetDependOn = getTargetByName(curTarget).getDependsOn();

        for (String neighbor : curTargetDependOn)
            if(isCyclicUtil(investigatedTarget, neighbor, isVisited,theTraverse)) {
                return true;
            }

        theTraverse.remove(curTarget);
        return false;

    }


}






