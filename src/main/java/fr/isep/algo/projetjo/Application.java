package fr.isep.algo.projetjo;

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

    public void addAthlete(Athlete athlete) {
        athletes.add(athlete);
    }

    public void removeAthlete(Athlete athlete) {
        athletes.remove(athlete);
    }

    public void addSport(Sport sport) {
        sports.add(sport);
    }

    public void removeSport(Sport sport) {
        sports.remove(sport);
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

    public void addResult(Result result) {
        results.add(result);
    }

    public void removeResult(Result result) {
        results.remove(result);
    }

    public List<Sport> getSports() {
        return sports;
    }
}