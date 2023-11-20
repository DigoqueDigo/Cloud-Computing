package server.containers;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.stream.Collectors;

import client.user.User;


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


    public boolean addUser(User user){

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


    public String toString(){

        try{
            this.readLock.lock();
            return this.userContainer.entrySet().stream().map(x -> x.toString()).collect(Collectors.joining("\n"));
        }

        catch (Exception e) {return "";}
        finally {this.readLock.unlock();}
    }
}