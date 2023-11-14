package server.containers;
import user.User;


public class ServerContainer{

    private UserContainer userContainer;

    public ServerContainer(){
        this.userContainer = new UserContainer();
    }

    public boolean putUser(User user){
        return this.userContainer.putUser(user);
    }

    public User getUser(String username){
        return this.userContainer.getUser(username);
    }
}