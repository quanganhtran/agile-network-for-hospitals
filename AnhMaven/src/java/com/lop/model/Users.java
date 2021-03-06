/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lop.model;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anh
 */
@XmlRootElement
public class Users implements ModelCollectionInterface<User>, Serializable {
    
    private AtomicInteger count;
    private final ConcurrentHashMap<String, User> byId;
    private final ConcurrentHashMap<String, User> byUsername;
    
    public Users() {
        count = new AtomicInteger();
        byId = new ConcurrentHashMap<>();
        byUsername = new ConcurrentHashMap<>();
    }
    
    public User login(User user) {
//        System.out.println("login received: " + user);
        for (User u : byUsername.values()) {
//            System.out.println("comparing to " + u);
            if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword()))
                return u;
        }
        return null;
    }
    
    @Override
    public void add(User obj) {
        obj.setId(count.incrementAndGet());
        byId.put(Integer.toString(obj.getId()), obj);
        byUsername.put(obj.getUsername(), obj);
    }
    
    public boolean modify(String id, User newU) {
//        User u = byId.get(id);
        User oldU = byId.replace(id, newU);
        if (oldU != null) {
//            byUsername.remove(oldU.getUsername(), oldU);
//            byUsername.put(newU.getUsername(), newU);
            return true;
        } else {
            return false;
        }
    }
    
    public User get(String key) {
        return byId.get(key);
    }
    
    public User get(String key, String method) throws NumberFormatException {
        return byId.get(key);
    }

    @XmlElement
    @Override
    public ConcurrentHashMap<String, User> getById() {
        return byId;
    }
    
    @XmlElement
    public ConcurrentHashMap<String, User> getByUsername() {
        return byUsername;
    }
    
}
