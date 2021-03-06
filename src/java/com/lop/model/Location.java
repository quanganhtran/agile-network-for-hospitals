/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anh
 */
@XmlRootElement
public class Location extends Model implements Serializable {

    private String number;
    private ArrayList<User> users;
    private ArrayList<Patient> patients;
    private List<Link> links = new ArrayList<>();

    public Location() {
    }

    public Location(String number) {
        this.number = number;
    }

    public Location(String number, ArrayList<User> users) {
        this.number = number;
        this.users = users;
        this.patients = new ArrayList<>();
    }

    @XmlElement
    public String getNumber() {
        return number;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(String url, String rel) {
        Link link = new Link();
        link.setLink(url);
        link.setRel(rel);
        links.add(link);
    }
}
