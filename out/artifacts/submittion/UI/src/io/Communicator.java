package io;

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


