package fr.isep.algo.projetjo.model;

import java.util.ArrayList;
import java.util.List;

public class Sport {
    private String name;
    private List<Athlete> athletes;

    public Sport(String name) {
        this.name = name;
        this.athletes = new ArrayList<>();
    }

    public void addAthlete(Athlete athlete) {
        athletes.add(athlete);
    }

    public void removeAthlete(Athlete athlete) {
        athletes.remove(athlete);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Athlete> getAthletes() {
        return athletes;
    }
}