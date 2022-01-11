package dependency.graph;


import dependency.target.Target;


import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;


public class DependencyGraph implements Serializable {

    private Map<String, Target> allTargets;
    private Map<Target.DependencyLevel, Set<Target>> targetsByDependencyLevel;
    private String workingDir;
    private Map<String, Set<String>> name2SerialSet;
    private int maxParallelism;


    public int getMaxParallelism() {
        return maxParallelism;
    }

    public Map<String, Set<String>> getAllSerialSets() {
        return name2SerialSet;
    }

    public DependencyGraph() {
        allTargets = new HashMap<>();
        targetsByDependencyLevel = new HashMap<>();
        targetsByDependencyLevel.put(Target.DependencyLevel.Leaf ,new HashSet<>());
        targetsByDependencyLevel.put(Target.DependencyLevel.Middle, new HashSet<>());
        targetsByDependencyLevel.put(Target.DependencyLevel.Root, new HashSet<>());
        targetsByDependencyLevel.put(Target.DependencyLevel.Independed, new HashSet<>());
        name2SerialSet = new HashMap<>();


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

    public boolean displayAllPathsBetweenTwoTargets(String srcName, String destName, Target.Dependency dependedOnOrNeeded, Consumer<String> out) {
        Target src = getTargetByName(srcName);
        Target dest = getTargetByName(destName);
        Map<Target, Boolean> isVisited = new HashMap<>();
        for (Target target : allTargets.values()){
            isVisited.put(target,false);
        }

        ArrayList<String> pathList = new ArrayList<>();

        pathList.add(src.getName());

        Boolean[] flag = new Boolean[]{new Boolean(false)};

        getAllPathsUtils(src, dest, isVisited, pathList, dependedOnOrNeeded, flag,out);

        return flag[0];
    }

    private void getAllPathsUtils(Target src, Target dest, Map<Target, Boolean> isVisited, ArrayList<String> pathList, Target.Dependency requiredOrNeeded, Boolean[] flag, Consumer<String> out) {

        if (src == dest) {
            //System.out.println(pathList.toString());
            out.accept(String.join("-->", pathList));

            flag[0] = true; // May Not Work
        }

        isVisited.put(src, true);

        Set<String> curSet = src.getDependsOnOrNeededFor(requiredOrNeeded);

        for (String targetName : curSet) {

            Target t = getTargetByName(targetName);

            if (isVisited.get(t) == false) {

                pathList.add(targetName);

                getAllPathsUtils(t, dest, isVisited, pathList, requiredOrNeeded, flag,out);

                pathList.remove(targetName);
            }

        }

        isVisited.put(src, false);
    }

    public void addTargetToGraph(String name, Target toAdd) {
        allTargets.put(name, toAdd);
    }

    public void setName2SerialSet(Map<String, Set<String>> name2SerialSet) {
        this.name2SerialSet = name2SerialSet;
    }

    public Map<String, Set<String>> getName2SerialSet() {
        return name2SerialSet;
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


    public void updateAllTargetDependencyLevelAfterExecution() {
        boolean noDepends, noRequired;
        for (Target target :
                allTargets.values()) {
            noDepends = target.getDependsOn().isEmpty();
            noRequired = target.getRequiredFor().isEmpty();
            if (noDepends && noRequired) {
                targetsByDependencyLevel.get(Target.DependencyLevel.Independed).add(target);
                target.setDependencyLevel(Target.DependencyLevel.Independed);

            } else if (noDepends && !noRequired) {
                targetsByDependencyLevel.get(Target.DependencyLevel.Leaf).add(target);
                target.setDependencyLevel(Target.DependencyLevel.Leaf);

            } else if (!noDepends && noRequired) {
                targetsByDependencyLevel.get(Target.DependencyLevel.Root).add(target);
                target.setDependencyLevel(Target.DependencyLevel.Root);
            } else if (!noDepends && !noRequired) {
                targetsByDependencyLevel.get(Target.DependencyLevel.Middle).add(target);
                target.setDependencyLevel(Target.DependencyLevel.Middle);

            }

        }
    }

    public void filterTargetDependendies() {
        for (Target curTarget : allTargets.values()) {
            Iterator<String> relatedTargetIter = curTarget.getDependsOn().iterator();
            while (relatedTargetIter.hasNext()) {
                String relatedTargetName = relatedTargetIter.next();
                if (!allTargets.containsKey(relatedTargetName))
                  relatedTargetIter.remove();
            }

             relatedTargetIter = curTarget.getRequiredFor().iterator();
            while(relatedTargetIter.hasNext())
            {
                String relatedTargetName = relatedTargetIter.next();

                if (!allTargets.containsKey(relatedTargetName))
                    relatedTargetIter.remove();
            }
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

    public void addSetToSerialSets(String name, Set<String> serialSet) {
        name2SerialSet.put(name, serialSet);
    }


    public void setMaxParallelism(int maxParallelism) {
        this.maxParallelism = maxParallelism;
    }

    public void resetTraverseDataAfterChangedInSubGraph(Map<Target,Set<String>> Target2ItsOriginalDependOnTargets,Map<Target,Set<String>> Target2ItsOriginalRequiredForTargets)
    {
        for (Target curTarget : allTargets.values()) {
            if(Target2ItsOriginalDependOnTargets.containsKey(curTarget)){
                curTarget.setDependsOn(Target2ItsOriginalDependOnTargets.get(curTarget));
            }
            if(Target2ItsOriginalRequiredForTargets.containsKey(curTarget)){
                curTarget.setRequiredFor(Target2ItsOriginalRequiredForTargets.get(curTarget));
            }
        }
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
            else if(target.getTargetStatus() == Target.TargetStatus.Skipped){
                target.setTargetStatus(Target.TargetStatus.Frozen);
            }
            else if(target.getTaskResult() == Target.TaskResult.Success || target.getTaskResult() == Target.TaskResult.Warning){
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

    public List<String> isTargetInCycle(String investigatedTargetName, Boolean[] inCycle)
    {
        Target investigatedTarget = getTargetByName(investigatedTargetName);
        Map<String, Boolean> isVisited = new HashMap<>();

        for (Target target : allTargets.values()){
            isVisited.put(target.getName(),false);
        }

        List<String> traverse = new ArrayList<>();
        traverse.add(investigatedTarget.getName());

        for (String curTarget : investigatedTarget.getDependsOn())
            if (isCyclicUtil(investigatedTarget,curTarget, isVisited,traverse))
                inCycle[0] = true;

        return traverse;
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

    public Set<Target> getTotalDependencies(String targetName, Target.Dependency dependency) {
        // this method CANT help with cycle find !!
        Set<Target> DependencyRelated = new HashSet<>();
        Set<String> visited = new HashSet<>();
        getTotalDependenciesRec(targetName, dependency,DependencyRelated,visited);
        DependencyRelated.remove(getTargetByName(targetName));
        //due to this line btw
        return DependencyRelated;
    }
    //
    private void getTotalDependenciesRec(String targetName, Target.Dependency dependency, Set<Target> DependencyRelated, Set<String> visitedTargets) {

        Target curTarget = allTargets.get(targetName);
        if (visitedTargets.contains(targetName)) return;

        visitedTargets.add(targetName);

        if (curTarget.getDependsOnOrNeededFor(dependency).isEmpty()){
            return;
        }

        for (String nextTargetName : curTarget.getDependsOnOrNeededFor(dependency)) {
            if(!visitedTargets.contains(nextTargetName)) {
                DependencyRelated.add(allTargets.get(nextTargetName));
                getTotalDependenciesRec(nextTargetName, dependency, DependencyRelated, visitedTargets);
            }
        }

    }

    public boolean isTargetBlocked(Target target) {
        for (Set<String> curSerialSet : name2SerialSet.values()){
            if (curSerialSet.contains(target.getName())){
                for (String targetNameInSet : curSerialSet){
                    if (getTargetByName(targetNameInSet).getTargetStatus().equals(Target.TargetStatus.InProcess))
                        return true;

                }
            }
        }
        return false;
    }
}






