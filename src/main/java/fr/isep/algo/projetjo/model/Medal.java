package fr.isep.algo.projetjo.model;

public class Medal {
    private int medalId;
    private int athleteId;
    private String medalType;

    public Medal(int medalId, int athleteId, String medalType) {
        this.medalId = medalId;
        this.athleteId = athleteId;
        this.medalType = medalType;
    }

    public int getMedalId() {
        return medalId;
    }

    public void setMedalId(int medalId) {
        this.medalId = medalId;
    }

    public int getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    public String getMedalType() {
        return medalType;
    }

    public void setMedalType(String medalType) {
        this.medalType = medalType;
    }

}
