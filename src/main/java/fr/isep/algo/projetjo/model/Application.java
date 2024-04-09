package fr.isep.algo.projetjo.model;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private List<Athlete> athletes;
    private List<Sport> sports;
    private List<Event> events;
    private List<Result> results;

    public Application() {
        this.athletes = new ArrayList<>();
        this.sports = new ArrayList<>();
        this.events = new ArrayList<>();
        this.results = new ArrayList<>();
    }
}