package MainScreen.adminDashboardScreen;

public class GraphInTable {
    private String name;
    private String uploadedBy;
    private Integer totalTargets;
    private Integer roots;
    private Integer middles;
    private Integer leafs;
    private Integer independent;
    private Integer simulationPrice;
    private Integer compilationPrice;


    public GraphInTable(String name, String uploadedBy, Integer totalTargets, Integer roots, Integer middles, Integer leafs, Integer independent, Integer simulationPrice, Integer compilationPrice) {
        this.name = name;
        this.uploadedBy = uploadedBy;
        this.totalTargets = totalTargets;
        this.roots = roots;
        this.middles = middles;
        this.leafs = leafs;
        this.independent = independent;
        this.simulationPrice = simulationPrice;
        this.compilationPrice = compilationPrice;
    }

    public String getName() {
        return name;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public Integer getTotalTargets() {
        return totalTargets;
    }

    public Integer getRoots() {
        return roots;
    }

    public Integer getMiddles() {
        return middles;
    }

    public Integer getLeafs() {
        return leafs;
    }

    public Integer getIndependent() {
        return independent;
    }

    public Integer getSimulationPrice() {
        return simulationPrice;
    }

    public Integer getCompilationPrice() {
        return compilationPrice;
    }
}
