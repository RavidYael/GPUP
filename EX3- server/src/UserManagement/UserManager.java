//the newUserManager with Class

package UserManagement;


import DTOs.AdminDTO;
import DTOs.UserDTO;
import DTOs.WorkerDTO;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Map<String,UserDTO> usersDTOMap;
    private final Map<String, WorkerDTO> WorkersDTOMap;
    private final Map<String, AdminDTO> AdminsDTOMap;


    public UserManager() {
        usersDTOMap = new HashMap<>();
        WorkersDTOMap = new HashMap<>();
        AdminsDTOMap = new HashMap<>();
    }

    public synchronized void addAdmin(String username,String Degree) {
        AdminDTO newAdmin = new AdminDTO(username,Degree); //TODO: WHATS ELSE INTERESTING ME??
        usersDTOMap.put(username,newAdmin);
        AdminsDTOMap.put(username,newAdmin);
    }

    public synchronized void addWorker(String username,String Degree,Integer availableThreads) {
        WorkerDTO newWorker = new WorkerDTO(username,Degree,availableThreads);
        usersDTOMap.put(username,newWorker);
        WorkersDTOMap.put(username,newWorker);
        
    }



    //IS IT WORKS? may be cause the bug of deleting while iterating
    public synchronized void removeUser(String username) {
        usersDTOMap.remove(username);
    }

    public synchronized Collection<UserDTO> getUsers() {
        return usersDTOMap.values();
    }


    public boolean isUserExists(String username) {
        return(usersDTOMap.containsKey(username));
    }


    public UserDTO getUserDTO(String userName){
        return usersDTOMap.get(userName);
    }
}
