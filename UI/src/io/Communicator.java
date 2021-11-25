package io;

import dependency.target.Target;
import execution.Task;
import validation.GraphExtractor;
import validation.GraphValidator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public interface Communicator {

       // void loadAndValidateGraphFromFile() throws Exception;
        void displayGraphInformation();
        void displayTargetInformation(String targetName);
        void displayPathBetweenTwoTargets(String name1, String name2, String dependencyType);
        void runTask();
        public void LoadFromFile(String directory) throws Exception;
        public String getInputFromUser();
        String getFileNameFromUser();
        void isTargetInCycle(String targetName);

    }


