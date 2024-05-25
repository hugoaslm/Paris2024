package fr.isep.algo.projetjo.model;

import java.util.List;

public class Result {
    private int resultId;
    private int eventId;
    private String vainqueur;
    private String resultData;
    private String eventName;
    private List<Athlete> athletesName;

    public Result(int resultId, int eventId, String vainqueur, String resultData) {
        this.resultId = resultId;
        this.eventId = eventId;
        this.vainqueur = vainqueur;
        this.resultData = resultData;
    }

    public int getResultId() {
        return resultId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getVainqueur() {
        return vainqueur;
    }

    public String getResultData() {
        return resultData;
    }

    public void setVainqueur(String vainqueur) {
        this.vainqueur = vainqueur;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setAthletesName(List<Athlete> athletesName) {
        this.athletesName = athletesName;
    }

    public List<Athlete> getAthletesName() {
        return athletesName;
    }
}
