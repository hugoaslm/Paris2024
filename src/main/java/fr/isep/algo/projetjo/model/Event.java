package fr.isep.algo.projetjo.model;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private Sport sport;
    private List<Athlete> athletes;

    public Event(Sport sport) {
        this.sport = sport;
        this.athletes = new ArrayList<>();
    }

    public void addAthlete(Athlete athlete) {
        athletes.add(athlete);
    }

    public void removeAthlete(Athlete athlete) {
        athletes.remove(athlete);
    }

    // Getters et setters
}
