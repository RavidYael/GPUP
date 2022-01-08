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
        void displayPathBetweenTwoTargets();
        void runTask();
        public void LoadFromFile() throws Exception;
        public void saveToFile(String directory);
        public String getInputFromUser();
        void isTargetInCycle(String targetName);

    }


