package io;

public interface InputCommunicator {

    public void LoadFromFile(String directory) throws Exception;
    public String getInputFromUser();
}
