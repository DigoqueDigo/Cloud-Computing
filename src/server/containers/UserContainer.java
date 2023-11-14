package server.containers;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import user.User;


public class UserContainer{

    private Map<String,User> userContainer;
    private ReadLock readLock;
    private WriteLock writeLock;


    public UserContainer(){
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
        this.userContainer = new HashMap<String,User>();

    }


    public boolean putUser(User user){

        try{
            this.writeLock.lock();
            return this.userContainer.putIfAbsent(user.getUsername(),user) == null;
        }

        catch (Exception e) {return false;}
        finally {this.writeLock.unlock();}
    }


    public User getUser(String username){

        try{
            this.readLock.lock();
            return this.userContainer.get(username);
        }

        catch (Exception e) {return null;}
        finally {this.readLock.unlock();}
    }
}