package fr.isep.algo.projetjo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {
    private int id;
    private String name;
    private String location;
    private Date date;
    private Sport sport;
    private List<Athlete> athletes;

    // Constructor
    public Event(int id, String name, String location, Date date, Sport sport) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
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
