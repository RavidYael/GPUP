package FXData;

import DTOs.GraphInfoDTO;
import DTOs.MissionInfoDTO;
import DTOs.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.fxml.builder.URLBuilder;
import dependency.graph.DependencyGraph;
import dependency.graph.GraphFactory;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static FXData.Constants.*;

public class ServerDataManager {

    private OkHttpClient client;

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public Set<UserDTO> getUsersWithRoles(){
        Set<UserDTO> users = null;
        Request userListRequest = new Request.Builder()
                .url(BASE_URL+ "/userList")
                .get()
                .build();
        Response userListResponse;
        Call call = client.newCall(userListRequest);
        try {
             userListResponse = call.execute();
             Gson gson = new Gson();
             Type collectionType = new TypeToken<Set<UserDTO>>(){}.getType();
             users = gson.fromJson(userListResponse.body().string(),collectionType);


        } catch (IOException e) {
            e.printStackTrace();
        }


    return users;
    }

    public Set<GraphInfoDTO> getGraphsDTO(){
        Set<GraphInfoDTO> graphs = null;
        Request request =  new Request.Builder()
                .url(BASE_URL +"/graphsList")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String graphsGson = response.body().string();
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Set<GraphInfoDTO>>(){}.getType();
            graphs =  gson.fromJson(graphsGson,collectionType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return graphs;
    }

    public DependencyGraph getDependencyGraphByName(String graphName){
        String xmlPath = "";
        DependencyGraph dependencyGraph = null;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL +"/graphs-xml").newBuilder();
        urlBuilder.addQueryParameter("graphName",graphName);

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

        try {
            Response response = client.newCall(request).execute();
            xmlPath = response.body().string();
            String xmlPathFormatted = xmlPath.trim().replace("\"","");
            dependencyGraph = GraphFactory.newGraphWithData(xmlPathFormatted);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    return dependencyGraph;

    }


    public Set<MissionInfoDTO> getAllMissionsDTO() {
        Set<MissionInfoDTO> missionDTOSet = new HashSet<>();
        Request request = new Request.Builder()
                .url(BASE_URL +"/tasksList")
                .build();

        try {
            Response response = client.newCall(request).execute();
            Gson missionGson = new Gson();
            Type collectionType = new TypeToken<Set<MissionInfoDTO>>(){}.getType();
            missionDTOSet = missionGson.fromJson(response.body().string(),collectionType);

        } catch (IOException e) {
            e.printStackTrace();
        }

    return missionDTOSet;
    }


    public List<MissionInfoDTO> getMissionDTOByName(String missionName){
        Set<MissionInfoDTO> allMissions = getAllMissionsDTO();
        return allMissions.stream().filter(m-> m.getMissionName().equals(missionName)).collect(Collectors.toList());
    }

    public Double getTaskProcess(String missionName) {
        Double progress = 0.0;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL+ "/task-progress").newBuilder();
        urlBuilder.addQueryParameter(MISSION_NAME, missionName);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try {
            Response response = client.newCall(request).execute();
            //progress = new Gson().fromJson(response.body().string(),Long.class);
            progress = Double.valueOf(response.body().string());

            if (response.code()>= 500){
                System.out.println("error in server trying to progress ");
            }
            else if(response.code() >=200 && response.code() <300){
                System.out.println("server " +"progress"+" succesfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return progress;

    }
    public void taskExecutionControl(String taskName,String action){
        HttpUrl.Builder urlBuilder  = HttpUrl.parse(BASE_URL+"/task-control").newBuilder();
        urlBuilder.addQueryParameter(MISSION_NAME,taskName );
        urlBuilder.addQueryParameter(CONTROL_TYPE, action);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("Admin attempted " +action+ " ended with status: " + response.code());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reUploadTask(String taskName, String type) {
        Request request = new Request.Builder()
                .url(BASE_URL +"/re-upload-task")
                .addHeader(CONTROL_TYPE,type)
                .addHeader(MISSION_NAME,taskName)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("re-upload Task: " +taskName +" "+type +" ended with status code: "+ response.code());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
