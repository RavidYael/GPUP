import java.awt.*;
import java.awt.Menu;
import org.jgrapht.alg.shortestpath.*;

//hi changes propose
// omer is writing a message



import UI.*;
import engine.dependency.graph.DependencyGraph;
import engine.dependency.target.Target;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.KShortestSimplePaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

public class main {
    public static void main(String[] args) {

        GpupMenu menu = new GpupMenu(new DependencyGraph());
        menu.executeMenu();


    }

    }
