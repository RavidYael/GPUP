package MainScreen.adminDashboardScreen;

public class TaskInTable {

    String name;
    String uploadedBy;
    String graphName;
    Integer totalTargets;
    Integer roots;
    Integer middles;
    Integer leafs;
    Integer independent;
    Integer totalPrice;
    Integer workers;

    public TaskInTable(String name, String uploadedBy, String graphName, Integer totalTargets, Integer roots, Integer midlles, Integer leafs, Integer independent, Integer totalPrice, Integer workers) {
        this.name = name;
        this.uploadedBy = uploadedBy;
        this.graphName = graphName;
        this.totalTargets = totalTargets;
        this.roots = roots;
        this.middles = midlles;
        this.leafs = leafs;
        this.independent = independent;
        this.totalPrice = totalPrice;
        this.workers = workers;


    }

    public String getName() {
        return name;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public String getGraphName() {
        return graphName;
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

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getWorkers() {
        return workers;
    }
}
