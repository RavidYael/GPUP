//the newUserManager with Class

package UserManagement;


import DTOs.UserDTO;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {



    private final Map<String,UserDTO> userDTOMap;

    public UserManager() {
        userDTOMap = new HashMap<>();
    }

    public synchronized void addUser(String username,String Degree) {
        UserDTO newUser = new UserDTO(username,Degree);
        userDTOMap.put(username,newUser);
    }

    //IS IT WORKS? may be cause the bug of deleting while iterating
    public synchronized void removeUser(String username) {
        userDTOMap.remove(username);
    }

    public synchronized Collection<UserDTO> getUsers() {
        return userDTOMap.values();
    }


    public boolean isUserExists(String username) {
        return(userDTOMap.containsKey(username));
    }


    public UserDTO getUserDTO(String userName){
        return userDTOMap.get(userName);
    }
}
