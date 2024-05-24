package fr.isep.algo.projetjo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {
    private int id;
    private String name;
    private String location;
    private Date date;
    private int sport;
    private List<Athlete> athletes;

    // Constructor
    public Event(int id, int sport, String name, String location, Date date) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.sport = sport;
        this.athletes = new ArrayList<>();
    }

    // Additional constructor for compatibility with the DAO
    public Event(int id, String name, String location, Date date) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.athletes = new ArrayList<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public java.sql.Date getDate() {
        return (java.sql.Date) date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

    public List<Athlete> getAthletes() {
        return athletes;
    }

    public void addAthlete(Athlete athlete) {
        athletes.add(athlete);
    }

    public void removeAthlete(Athlete athlete) {
        athletes.remove(athlete);
    }
}
