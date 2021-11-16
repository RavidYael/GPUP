package validation;

import jaxb.generated.GPUPDescriptor;
import jaxb.generated.GPUPTarget;
import jaxb.generated.GPUPTargetDependencies;

import java.util.HashMap;
import java.util.Map;

public class GraphValidator {

    private final GPUPDescriptor investigatedGraph;
    private boolean valid = true;
    private Map<String,GPUPTarget> name2Target;


    public GraphValidator(GPUPDescriptor investigatedGraph) {
        this.investigatedGraph = investigatedGraph;
        name2Target = new HashMap<>();

        for(GPUPTarget gt : investigatedGraph.getGPUPTargets().getGPUPTarget())
        {
            name2Target.put(gt.getName(),gt);
        }
    }

    public boolean startValidation()
    {
        // conduct all test in order
        try {
            isAllTargetsExist();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (valid)
        {
            try {
                isTargetNameUnique();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (valid)
        {
            try {
                containsDependencyConflict();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return valid;

    }

    public void isTargetNameUnique() throws Exception {

        if (name2Target.keySet().size() != investigatedGraph.getGPUPTargets().getGPUPTarget().size()) {
            valid = false;
            throw(new Exception("Graph contains name duplication"));
        }

    }

    public void isAllTargetsExist() throws Exception
    {
        for(GPUPTarget gt : name2Target.values())
        {
            for (GPUPTargetDependencies.GPUGDependency gpupDependency : gt.getGPUPTargetDependencies().getGPUGDependency())
            {
                if (!name2Target.containsKey(gpupDependency.getValue()))
                {
                    valid = false;
                    throw(new Exception("One or more target described in dependencies does not exist"));
                }
            }
        }
    }

    public void containsDependencyConflict() throws Exception
    {
        for (GPUPTarget curTarget : name2Target.values())  {
            for (GPUPTargetDependencies.GPUGDependency curDependency : curTarget.getGPUPTargetDependencies().getGPUGDependency()) {
                String dependencyType = curDependency.getType();
                if (dependencyType.equals("dependsOn")) // reverse type of dependency
                    dependencyType = "requiredFor";
                else
                    dependencyType = "dependsOn";
                GPUPTarget relatedTarget = name2Target.get(curDependency.getValue());
                GPUPTargetDependencies.GPUGDependency oppositeRelation = new GPUPTargetDependencies.GPUGDependency();
                oppositeRelation.setValue(curTarget.getName()); // create Dependency object with opposite relation and
                oppositeRelation.setType(dependencyType);        // name of the target we started with in order to find the conflict
                if (relatedTarget.getGPUPTargetDependencies().getGPUGDependency().contains(oppositeRelation)) {
                    valid = false;
                    throw(new Exception("Graph contains a conflict of dependencies between  target:" + oppositeRelation.getValue() + "And target:" +curDependency.getValue()));
                }
            }
        }
    }



}
