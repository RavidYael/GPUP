package dependency.graph;

import dependency.target.Target;
import jaxb.generated.GPUPDescriptor;
import jaxb.generated.GPUPTarget;
import jaxb.generated.GPUPTargetDependencies;
import validation.GraphExtractor;
import validation.GraphValidator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GraphFactory {
    private static DependencyGraph dependencyGraph;
    private static GPUPDescriptor generatedGraph;



    private static boolean loadAndValidateGraphFromFile(String directory) throws Exception {
        Path path = Paths.get(directory);
        try {
            generatedGraph = new GraphExtractor(path).getGeneratedGraph();
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
        for(GPUPTarget curTarget : generatedGraph.getGPUPTargets().getGPUPTarget())
        {
            Target temp = new Target(curTarget.getName(),curTarget.getGPUPUserData());
            for (GPUPTargetDependencies.GPUGDependency gpupDependency :
                 curTarget.getGPUPTargetDependencies().getGPUGDependency()) {
                if (gpupDependency.getType().equals("DependsOn"))
                    temp.addToDependsOn(gpupDependency.getValue());
                else
                    temp.addToRequiredFor(gpupDependency.getValue());

            }
            dependencyGraph.addTargetToGraph(temp.getName(),temp);
        }
        dependencyGraph.setTargetsByDependencyLevel();
        return dependencyGraph;


    }
}
