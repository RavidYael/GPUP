

package dependency.target;

import java.util.HashSet;
import java.util.Set;



public class Target {

  public static enum DependencyLevel {Root,Middle,Leaf, Independed}
  public static enum Dependency {DependsOn , RequiredFor}

    private String name;
    private Set<String> requiredFor;
    private Set<String> dependsOn;
    private String data;
    private DependencyLevel dependencyLevel;

    public Target(String name,String data) {

        this.name = name;
        this.data = data;
        requiredFor = new HashSet<>();
        dependsOn = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public DependencyLevel getDependencyLevel() {
        return dependencyLevel;
    }

    public Set<String> getRequiredFor() {
        return requiredFor;
    }

    public Set<String> getDependsOn() {
        return dependsOn;
    }

    public Set<String> getDependsOnOrNeededFor(Dependency depEnum)
    {
        if (depEnum == Dependency.DependsOn)
            return getDependsOn();

        else
            return getRequiredFor();
    }

    public void addToRequiredFor(String name)
    {
        requiredFor.add(name);
    }

    public void addToDependsOn(String name)
    {
        dependsOn.add(name);
    }




    @Override
    public String toString() {
        return "Target{" +
                "name='" + name + '\'' +
                '}';
    }



}
