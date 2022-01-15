package FXData;
import dependency.target.Target;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class GraphvizFactory {

    private String dotText = "";
    /**-----------------------------------saved phrases---------------------------------------*/
    private final String tempPath;
    private final String rootColor;
    private final String middleColor;
    private final String leafColor;
    private final String independentColor;
    private static final String fileNameDOT = "GeneratedGraph.dot";
    private static final String fileNamePNG = "GeneratedGraph.png";
    private static final String createPNGFromDOT = "dot -Tpng "+ fileNameDOT + " -o " + fileNamePNG;
    private static final String nodeProperties = "node[style = filled fontsize=42 width=1.5 shape=circle fillcolor=white]\n";
    private static final String graphProperties = "graph[truecolor=true bgcolor = transparent nodesep = 1.3 ranksep = 1.3]\n";
    private static final String edgeProperties = "edge[color=black arrowsize=3.0 penwidth =3.0]\n";

    /**-------------------------------------constructor---------------------------------------*/
    GraphvizFactory(String tempPath, String rootColor, String middleColor, String leafColor, String independentColor){
        this.tempPath = tempPath;
        this.rootColor = rootColor;
        this.middleColor = middleColor;
        this.leafColor = leafColor;
        this.independentColor = independentColor;
    }

    /**-------------------------------------dotText writer---------------------------------------*/
    public void openGraph(){dotText = "digraph G {\n" + nodeProperties + graphProperties + edgeProperties;}
    public void closeGraph(){dotText += "}\n";}
    public void addRoot(String rootName){dotText += rootName + "[fillcolor =" + rootColor +"]\n";}
    public void addLeaf(String rootName){dotText += rootName + "[fillcolor =" + leafColor +"]\n";}
    public void addMiddle(String rootName){dotText += rootName + "[fillcolor =" + middleColor +"]\n";}
    public void addIndependent(String rootName){dotText += rootName + "[fillcolor =" + independentColor +"]\n";}

    public void addNode(Target target) {
        switch (target.getDependencyLevel()){
            case Leaf: addLeaf(target.getName());
                break;
            case Middle:addMiddle(target.getName());
                break;
            case Root:addRoot(target.getName());
                break;
            case Independed:addIndependent(target.getName());
                break;
        }
    }
    public void addConnections(Target from, String arrowColor){
        if(from.getDependsOn().isEmpty())
            return;
        dotText += from.getName() + "-> {";
        for (String targetName : from.getDependsOn())
            dotText += targetName + " ";
        dotText += "} [color = " +arrowColor +"]\n";
    }

    /**-------------------------------------generate javaFX Image from dotText---------------------------------------*/
    public Image generateImage(){
        byte[] img_stream = null;
        FileInputStream in = null;

        File dotFile = new File(tempPath + "/" +fileNameDOT);
        if (createDotFile(dotFile)) return null;

        if (createImageCMD()) return null;

        if (dotFile.exists())
            dotFile.delete();

        File pngFile = new File(tempPath + "/" +fileNamePNG);
        img_stream = getBytes(pngFile);
        if (img_stream == null) return null;

        return new Image(new ByteArrayInputStream(img_stream));
    }

    private byte[] getBytes(File pngFile) {
        FileInputStream in;
        byte[] img_stream;
        try {
            in = new FileInputStream(pngFile.getAbsolutePath());
            img_stream = new byte[in.available()];
            in.read(img_stream);
            if( in != null ) in.close();
            if (pngFile.exists())
                pngFile.delete();
        } catch (Exception ex) {
            System.out.println("could not generate png from graph");
            return null;
        }
        return img_stream;
    }

    private boolean createImageCMD() {
        try {
            Process process = Runtime.getRuntime().exec(
                    "cmd /c start /wait cmd.exe /K \"cd \\ && cd " + tempPath + " && " + createPNGFromDOT + "&& exit");
            process.waitFor();
        }catch (Exception exception) {
            System.out.println("could not generate png from graph - problem with GraphViz in cmd");
            return true;
        }
        return false;
    }

    private boolean createDotFile(File dotFile) {
        try (Writer out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(dotFile), "UTF-8"))) {
            out.write(dotText + "\r\n");
        } catch (Exception e) {
            System.out.println("could not generate png from graph - problem with saving dot file");
            return true;
        }
        return false;
    }


//    public void saveImage(String name) {
//        File dotFile = new File(tempPath + "/" +name + ".dot");
//        String outputImage = tempPath+"\\" + fileNamePNG;
//        String dotAppLocation = "C:\\Program Files (x86)\\Graphviz\\bin\\dot.exe";
//        String command = dotAppLocation+ "-Tpng"+ dotFile.getPath() + "-o" + outputImage;
//        if (createDotFile(dotFile)) return;
//
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder("cd\\","dot", "-Tpng ", dotFile.getPath(), "-o",outputImage);
//            Process process = processBuilder.start();
//            process.waitFor();
//            processBuilder.redirectErrorStream(true);
//            processBuilder.redirectError(new File("C:\\Users\\oatar\\IdeaProjects\\GPUP-Advanced\\JavaFx\\resources\\graphvizLogFile.log"));
//            System.out.println(process.getErrorStream());
//        }catch (Exception exception) {
//
//            System.out.println("could not generate png from graph - problem with GraphViz in cmd");
//        }
//    }

    public void saveImage(String name) {
       // File dotFile = new File(tempPath + "/" +name + ".dot");
        File dotFile = new File(tempPath + "/" +name + ".dot");
        String dotFileLocalPath = name + ".dot";
        String createPNGFromDOT = "dot -Tpng "+ dotFileLocalPath + " -o " + fileNamePNG;

       if (createDotFile(dotFile)) return;

        try {
            Process process = Runtime.getRuntime().exec("cmd /c start /wait cmd.exe /K \"cd \\ && cd " + tempPath + " && " + createPNGFromDOT + " && exit");
            process.waitFor();
        }catch (Exception exception) {
            System.out.println("could not generate png from graph - problem with GraphViz in cmd");
        }
    }


}


