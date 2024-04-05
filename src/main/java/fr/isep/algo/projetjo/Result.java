package fr.isep.algo.projetjo;

public class Result {
    private Event event;
    private Athlete athlete;
    private String time;
    private int score;
    private String medal;

    public Result(Event event, Athlete athlete, String time, int score, String medal) {
        this.event = event;
        this.athlete = athlete;
        this.time = time;
        this.score = score;
        this.medal = medal;
    }

    // Getters et setters
}
