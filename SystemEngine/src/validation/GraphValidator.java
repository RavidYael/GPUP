package validation;

import jaxb.generated.GPUPDescriptor;
import jaxb.generated.GPUPTarget;
import jaxb.generated.GPUPTargetDependencies;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public boolean startValidation() throws Exception {
        // conduct all test in order


           // isAllTargetsExist();


        if (valid) {
            isTargetNameUnique();
        }


        if (valid)
        {
            containsDependencyConflict();
        }
        if (valid){
            isTargetInSerialSetExist();
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
        for(GPUPTarget gt : name2Target.values()) {
            if (gt.getGPUPTargetDependencies() != null) {
                for (GPUPTargetDependencies.GPUGDependency gpupDependency : gt.getGPUPTargetDependencies().getGPUGDependency()) {
                    if (!name2Target.containsKey(gpupDependency.getValue())) {
                        valid = false;
                        throw (new Exception("Target named : " +gpupDependency.getValue() + "  described in dependency doesn't exists"));
                    }
                }
            }
        }
    }

    public void isTargetInSerialSetExist() throws Exception {
        AtomicBoolean Avalid = new AtomicBoolean(true);
        boolean resValid;
        if (investigatedGraph.getGPUPSerialSets() != null) {
            for (GPUPDescriptor.GPUPSerialSets.GPUPSerialSet serialSet : investigatedGraph.getGPUPSerialSets().getGPUPSerialSet()) {
                String[] nameList = serialSet.getTargets().split(",");
                Arrays.stream(nameList).forEach(name -> {
                    if (!name2Target.keySet().contains(name)) {
                        Avalid.set(false);
                    }
                });

            }
        }
        if (!Avalid.get())
            throw (new Exception("not all targets described in serial sets exist"));
        valid = Avalid.get();
        }



    public void containsDependencyConflict() throws Exception
    {
        for (GPUPTarget curTarget : name2Target.values()) {
            if (curTarget.getGPUPTargetDependencies() != null) {
                for (GPUPTargetDependencies.GPUGDependency curDependency : curTarget.getGPUPTargetDependencies().getGPUGDependency()) {
                    String dependencyType = curDependency.getType();
                    GPUPTarget relatedTarget = name2Target.get(curDependency.getValue());
                    if (relatedTarget.getGPUPTargetDependencies() != null) {
                        for(GPUPTargetDependencies.GPUGDependency gpupTargetDependency : relatedTarget.getGPUPTargetDependencies().getGPUGDependency()) {
                            if (gpupTargetDependency.getType().equals(dependencyType) && gpupTargetDependency.getValue().equals(curTarget.getName())) {
                                valid = false;
                                throw (new Exception("Graph contains a conflict of dependencies between  target:" + relatedTarget.getName() + " And target:" + curTarget.getName()));
                            }
                        }
                    }
                }
            }
        }
    }




}
