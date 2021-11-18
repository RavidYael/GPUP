package dependency.graph;


import dependency.target.Target;
import dependency.target.Target;
import dependency.target.Target;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;




public class DependencyGraph {

    private Map<String, Target> allTargets;
    private Map<Target.DependencyLevel,Set<Target>> targetsByDependencyLevel;

    public DependencyGraph() {}
    public Map<String,Target> getAllTargets() {
        return allTargets;
    }
    public Set<Target> getTargetsByLevel(Target.DependencyLevel level) {
        return targetsByDependencyLevel.get(level);
    }
    public Target getTargetByName(String targetName) {return allTargets.get(targetName);}
    public Integer getTargetsCountByLevel(Target.DependencyLevel level) {
        return getTargetsByLevel(level).size();
    }

   public boolean displayAllPathsBetweenTwoTargets(Target src, Target dest, Target.Dependency dependedOnOrNeeded ) {
        Map<Target,Boolean> isVisited = new HashMap<>();
        ArrayList<String> pathList = new ArrayList<>();
        pathList.add(src.getName());
        Boolean[] flag = new Boolean[] {new Boolean(false)};
        getAllPathsUtils(src,dest,isVisited,pathList,dependedOnOrNeeded, flag);
        return flag[0];
    }

    private void getAllPathsUtils(Target src, Target dest, Map<Target,Boolean> isVisited, ArrayList<String> pathList, Target.Dependency requiredOrNeeded, Boolean[] flag ) {

        if (src.equals(dest)) {
            System.out.println(pathList.toString());
            flag[0] = true; // May Not Work
        }

        isVisited.put(src, true);
        Set<String> curSet = src.getDependsOnOrNeededFor(requiredOrNeeded);
        for (String targetName : curSet) {
            Target t = getTargetByName(targetName);
            if (isVisited.get(t) == false) {
                pathList.add(targetName);
                getAllPathsUtils(t, dest, isVisited, pathList, requiredOrNeeded, flag);

                pathList.remove(targetName);
            }

            isVisited.put(src, false);

        }
    }

    public void setAllTargets(Map<String, Target> allTargets) {
        this.allTargets = allTargets;
    }

    public void addTargetToGraph(String name, Target toAdd) {
        allTargets.put(name,toAdd);
    }

    public void setTargetsByDependencyLevel() {
        boolean noDepends,noRequired;
        for (Target target:
             allTargets.values()) {
            noDepends = target.getDependsOn().isEmpty();
            noRequired = target.getRequiredFor().isEmpty();
            if (noDepends && noRequired)
                targetsByDependencyLevel.get(Target.DependencyLevel.Independed).add(target);
            else if(noDepends && !noRequired)
                targetsByDependencyLevel.get(Target.DependencyLevel.Leaf).add(target);
            else if(!noDepends && noRequired)
                targetsByDependencyLevel.get(Target.DependencyLevel.Root).add(target);
            else if (!noDepends && !noRequired)
                targetsByDependencyLevel.get(Target.DependencyLevel.Middle).add(target);



        }
    }

    //    public Boolean isInCycle(Target src)
//    {
//
//    }



}
