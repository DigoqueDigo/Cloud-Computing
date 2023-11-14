package server.containers;
import java.util.HashMap;
import java.util.Map;
import user.User;


public class UserContainer{

    private Map<String,User> userContainer;

    public UserContainer(){
        this.userContainer = new HashMap<String,User>();
    }

    public void put(User user){
        this.userContainer.putIfAbsent(user.getUsername(),user);
    }

    public User getValue(String username){
        return this.userContainer.get(username);
    }
}