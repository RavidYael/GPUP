package UserManagement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class userManager {

    public class User{

        private String name;
        private String degree;

        public User(String Name,String Degree){
            this.name = Name;
            this.degree = Degree;
        }
    }

    private final Set<User> usersSet;

    public userManager() {
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(String username,String Degree) {
        User newUser = new User(username,Degree);
        usersSet.add(newUser);
    }

    //IS IT WORKS? may be cause the bug of deleting while iterating
    public synchronized void removeUser(String username) {
        for (User user :usersSet) {
            if (user.name==username)
                usersSet.remove(user);
            return;
        }
    }

    public synchronized Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }


    public boolean isUserExists(String username) {
        for (User user :usersSet) {
            if (user.name==username)
                return true;
        }
        return false;
    }
}
