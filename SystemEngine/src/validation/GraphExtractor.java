package validation;

import jaxb.generated.GPUPDescriptor;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

public class GraphExtractor {

    private final static String JAXB_XML_PACKAGE_NAME = "jaxb.generated";


    public GPUPDescriptor getGeneratedGraph() {
        return generatedGraph;
    }

    private final GPUPDescriptor generatedGraph;
    private boolean valid = true;


    public GraphExtractor(String directory) throws Exception {
        if (!directory.endsWith("xml")) {
            throw (new Exception("Unsupported file type, file must end with .xml"));

        } else
            generatedGraph = getGraphFromXml(directory);
    }

    public GPUPDescriptor getGraphFromXml(String directory) throws Exception {
        try {
            InputStream inputStream = new FileInputStream(directory);
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            return (GPUPDescriptor) u.unmarshal(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw (new Exception("File not found, please try again"));

        } catch (JAXBException e) {
            e.printStackTrace();

        }

        valid = false;
        return null;

    }
}