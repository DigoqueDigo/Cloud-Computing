package src;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Authentication {
    private Map<String, String> credenciais; // NOME -> PASSWORD
    private ReentrantLock lock;

    public Authentication() {
        this.credenciais = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public Map<String, String> getCredenciais(){
        return this.credenciais;
    }

    public boolean registerUser(String username, String password) {
        lock.lock();
        try{
            if (!credenciais.containsKey(username)) {
                credenciais.put(username, password);
                return true;
            }
        }
        finally {
            lock.unlock();
        }

        return false;
    }

    public boolean authenticateUser(String username, String password) {
        return credenciais.containsKey(username) && credenciais.get(username).equals(password);
    }


}
