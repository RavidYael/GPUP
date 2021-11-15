package dependency.graph.target;

import java.util.Set;


public class Target {

  public static enum DependencyLevel {Root,Middle,Leaf, Independed}
  public static enum Dependency {DependsOn , RequiredFor}

    private String name;
    private Set<Target> requiredFor;
    private Set<Target> dependsOn;
    private dependency.graph.target.TargetData data;
    private DependencyLevel dependencyLevel;

    public Target(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DependencyLevel getDependencyLevel() {
        return dependencyLevel;
    }

    public Set<Target> getRequiredFor() {
        return requiredFor;
    }

    public Set<Target> getDependsOn() {
        return dependsOn;
    }

    public Set<Target> getDependsOnOrNeededFor(Dependency depEnum)
    {
        if (depEnum == Dependency.DependsOn)
            return getDependsOn();

        else
            return getRequiredFor();
    }



    public TargetData getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Target{" +
                "name='" + name + '\'' +
                '}';
    }



}
