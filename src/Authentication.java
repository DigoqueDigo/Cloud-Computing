package src;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Authentication {
    private Map<String, String> credenciais; // NOME -> PASSWORD
    private ReentrantLock lock;

    public Authentication() {
        this.credenciais = new HashMap<>();
    }

    public Map<String, String> getCredenciais(){
        return this.credenciais;
    }

    public void registerUser(String username, String password) {
        lock.lock();
        try{
            credenciais.put(username, password);
        }
        finally {
            lock.unlock();
        }
    }

    public boolean authenticateUser(String username, String password) {
        return credenciais.containsKey(username) && credenciais.get(username).equals(password);
    }


}
