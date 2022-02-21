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
}



