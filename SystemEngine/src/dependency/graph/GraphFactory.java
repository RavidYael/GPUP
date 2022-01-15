package dependency.graph;

import dependency.target.Target;
import jaxb.generated.GPUPDescriptor;
import jaxb.generated.GPUPTarget;
import jaxb.generated.GPUPTargetDependencies;
import validation.GraphExtractor;
import validation.GraphValidator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GraphFactory {
    private static DependencyGraph dependencyGraph;
    private static GPUPDescriptor generatedGraph;



    private static boolean loadAndValidateGraphFromFile(String directory) throws Exception {
            generatedGraph = new GraphExtractor(directory).getGeneratedGraph();


        GraphValidator graphValidator = new GraphValidator(generatedGraph);
        return graphValidator.startValidation();  // not sure where to catch the exceptions

    }

    private static void generateSerialSets(){
    if (generatedGraph.getGPUPSerialSets() != null) {
        for (GPUPDescriptor.GPUPSerialSets.GPUPSerialSet serialSet : generatedGraph.getGPUPSerialSets().getGPUPSerialSet()) {
            Set<String> tempSet = new HashSet<>();
            String[] nameList = serialSet.getTargets().split(",");
            Arrays.stream(nameList).forEach(s -> tempSet.add(s));
            dependencyGraph.addSetToSerialSets(serialSet.getName(), tempSet);
        }
    }

    }

    public static DependencyGraph newGraphWithData(String directory) throws Exception {

       if (!loadAndValidateGraphFromFile(directory))
           return null;

        dependencyGraph = new DependencyGraph();
        dependencyGraph.setWorkingDir(generatedGraph.getGPUPConfiguration().getGPUPWorkingDirectory());
        dependencyGraph.setMaxParallelism(generatedGraph.getGPUPConfiguration().getGPUPMaxParallelism());
        Target toAdd;
        for(GPUPTarget curTarget : generatedGraph.getGPUPTargets().getGPUPTarget()) // adding targets to graph
        {
            toAdd = new Target(curTarget.getName(),curTarget.getGPUPUserData());
            if (curTarget.getGPUPTargetDependencies()!= null) {
                for (GPUPTargetDependencies.GPUGDependency gpupDependency :
                        curTarget.getGPUPTargetDependencies().getGPUGDependency()) {
                    if (gpupDependency.getType().equals("requiredFor"))
                        toAdd.addToRequiredFor(gpupDependency.getValue());
                    else
                        toAdd.addToDependsOn(gpupDependency.getValue());

                }
            }
            dependencyGraph.addTargetToGraph(toAdd.getName(),toAdd);
        }
        dependencyGraph.fixTargetsDependencies();
       dependencyGraph.updateAllTargetDependencyLevel();
       generateSerialSets();
        return dependencyGraph;

    }
}
