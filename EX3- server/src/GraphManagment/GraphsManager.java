package GraphManagment;

import DTOs.GraphInfoDTO;
import dependency.graph.DependencyGraph;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphsManager {

    private Map<String, DependencyGraph> GraphsByNames;
    private Map<String, Path> GraphsXmlsByName;
    private Map<String, GraphInfoDTO> graphInfoDTOMap;



    public GraphsManager(){
        GraphsByNames = new HashMap<>();
        GraphsXmlsByName = new HashMap<>();
        graphInfoDTOMap = new HashMap<>();
    }

    public Set<GraphInfoDTO> getAllGraphsDTO(){
        return graphInfoDTOMap.values().stream().collect(Collectors.toSet());
    }
    public DependencyGraph getGraphByName(String graphName) {
        return GraphsByNames.get(graphName);
    }

    public GraphInfoDTO getGraphInfoDTO(String graphName) {
        return graphInfoDTOMap.get(graphName);
    }

    public Path getGraphsXmlsByName(String graphName) {
        return GraphsXmlsByName.get(graphName);
    }


    public void addGraphXml(String graphName, Path graphPath){
        GraphsXmlsByName.put(graphName ,graphPath);
    }

    public void addGraph(String graphName,DependencyGraph upGraph){
        GraphsByNames.put(graphName,upGraph);
        graphInfoDTOMap.put(graphName,new GraphInfoDTO(upGraph));

    }

    public Boolean isGraphExist(String GraphName){
        return (GraphsByNames.containsKey(GraphName));
    }

}
