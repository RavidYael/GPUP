package FXData;

import DTOs.GraphInfoDTO;
import DTOs.MissionInfoDTO;
import DTOs.TargetDTO;
import DTOs.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dependency.graph.DependencyGraph;
import dependency.graph.GraphFactory;
import mainScreen.workerDashboardScreen.TaskInTable;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static FXData.Constants.*;
import static jakarta.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

public class ServerDataManager {
    //TODO : HERE YOU PROBABLY GOING To WRITE THE REQUEST TO BE PAUSED AND RESUME ,
    // WE NEED TO UPDATE THE WORK EXECUTOR THAT HE IS BEING PAUSED TROUGH THE REQUEST (:

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

    public Set<MissionInfoDTO> getAllMissionsForUser(){
        Set<MissionInfoDTO> missionDTOSet = new HashSet<>();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL +"/tasksListForUser").newBuilder();
        urlBuilder.addQueryParameter(USER_NAME, (String)SimpleCookieManager.getSimpleCookie(USER_NAME));

        Request request = new Request.Builder()
                .url(urlBuilder.build())
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

    public boolean amIListed(String taskName) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL +"/subscribedUsers").newBuilder();
        urlBuilder.addQueryParameter(MISSION_NAME, taskName);
        boolean res = false;

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson usersGson = new Gson();
            Type collectionType = new TypeToken<Set<UserDTO>>(){}.getType();
            Set<UserDTO> usersFromServer = usersGson.fromJson(response.body().string(), collectionType);
            if (usersFromServer!=null)
             res = usersFromServer.stream().map(u->u.getName()).collect(Collectors.toSet()).contains((String)SimpleCookieManager.getSimpleCookie(USER_NAME));


        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
    public TargetDTO getTargetToRunFromServer(){
        TargetDTO targetDTOFromServer = null;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL +"/runnable-target").newBuilder(); //TODO url not final
       // urlBuilder.addQueryParameter("MissionName",missionName);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == SC_CONFLICT)
                targetDTOFromServer = null;
            else if (response.code() == SC_ACCEPTED) {
                targetDTOFromServer = new Gson().fromJson(response.body().string(), TargetDTO.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetDTOFromServer;

    }

    public void updateServerWithTaskResult(TargetDTO targetDTOToRun) {
        Request request = new Request.Builder()
                .url(BASE_URL +"/update-target-result")
                .post(RequestBody.create(MediaType.parse("application/json"),new Gson().toJson(targetDTOToRun)))
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("update server: "+ response.code());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public MissionInfoDTO getTaskFromServer(String taskName) {
        MissionInfoDTO missionInfoDTO = null;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL +"/task-data").newBuilder();
        urlBuilder.addQueryParameter("selectedMissionName", taskName);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try {
            Response response = client.newCall(request).execute();
            missionInfoDTO = new Gson().fromJson(response.body().string(),MissionInfoDTO.class);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return missionInfoDTO;
    }

    public void pauseResumeOrStopRequest(String taskName,String action) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL+ "/subscribe").newBuilder();
        urlBuilder.addQueryParameter(MISSION_NAME, taskName);
        urlBuilder.addQueryParameter(SUBSCRIBE_TYPE, action);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code()>= 500){
                System.out.println("error in server trying to " +action);
            }
            else if(response.code() >=200 && response.code() <300){
                System.out.println("server " + action +" succesfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public Integer getExecutedTargetsForUser(String missionName) {
        Integer numOfTargets =0;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL+ "/target-run-by-user").newBuilder();
        urlBuilder.addQueryParameter(MISSION_NAME, missionName);
        urlBuilder.addQueryParameter(USER_NAME, (String)SimpleCookieManager.getSimpleCookie(USER_NAME));

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try {
            Response response = client.newCall(request).execute();
            numOfTargets = new Gson().fromJson(response.body().string(),Integer.class);
            if (numOfTargets == null)
                numOfTargets =0;


        } catch (IOException e) {
            e.printStackTrace();
        }
    return numOfTargets;
    }

}



