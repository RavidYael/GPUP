//the newUserManager with Class

package UserManagement;


import DTOs.UserDTO;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class userManager {



    private final Set<UserDTO> usersSet;

    public userManager() {
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(String username,String Degree) {
        UserDTO newUser = new UserDTO(username,Degree);
        usersSet.add(newUser);
    }

    //IS IT WORKS? may be cause the bug of deleting while iterating
    public synchronized void removeUser(String username) {
        for (UserDTO user :usersSet) {
            if (user.getName().equals(username))
                usersSet.remove(user);
            return;
        }
    }

    public synchronized Set<UserDTO> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }


    public boolean isUserExists(String username) {
        for (UserDTO user :usersSet) {
            if (user.getName().equals(username))
                return true;
        }
        return false;
    }
}
