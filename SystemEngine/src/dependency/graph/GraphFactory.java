package dependency.graph;

import dependency.target.Target;
import jaxb.generated.GPUPDescriptor;
import jaxb.generated.GPUPTarget;
import jaxb.generated.GPUPTargetDependencies;
import validation.GraphExtractor;
import validation.GraphValidator;

public class GraphFactory {
    private static DependencyGraph dependencyGraph;
    private static GPUPDescriptor generatedGraph;



    private static boolean loadAndValidateGraphFromFile(String directory) throws Exception {
       // Path path = Paths.get(directory);
        try {
            generatedGraph = new GraphExtractor(directory).getGeneratedGraph();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }

        GraphValidator graphValidator = new GraphValidator(generatedGraph);
        return graphValidator.startValidation();  // not sure where to catch the exceptions

    }

    public static DependencyGraph newGraphWithData(String directory) throws Exception {

       if (!loadAndValidateGraphFromFile(directory))
           return null;

        dependencyGraph = new DependencyGraph();
        Target toAdd;
        for(GPUPTarget curTarget : generatedGraph.getGPUPTargets().getGPUPTarget())
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

        return dependencyGraph;

    }
}
