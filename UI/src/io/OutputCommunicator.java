package io;

import dependency.target.Target;
import validation.GraphExtractor;
import validation.GraphValidator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

 public interface OutputCommunicator {

    // void loadAndValidateGraphFromFile() throws Exception;

     void displayGraphInformation();
     void displayTargetInformation(String targetName);
     void displayPathBetweenTwoTargets(String name1, String name2, String dependencyType);

}
