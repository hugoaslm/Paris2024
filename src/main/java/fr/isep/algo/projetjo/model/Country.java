package fr.isep.algo.projetjo.model;

public class Country {
    private String name;
    private int goldMedals;
    private int silverMedals;
    private int bronzeMedals;
    private int totalMedals;

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getGoldMedals() {
        return goldMedals;
    }

    public int getSilverMedals() {
        return silverMedals;
    }

    public int getBronzeMedals() {
        return bronzeMedals;
    }

    public void addGoldMedals(int count) {
        this.goldMedals += count;
    }

    public void addSilverMedals(int count) {
        this.silverMedals += count;
    }

    public void addBronzeMedals(int count) {
        this.bronzeMedals += count;
    }

    public void setBronzeMedals(int bronzeMedals) {
        this.bronzeMedals = bronzeMedals;
    }

    public void setGoldMedals(int goldMedals) {
        this.goldMedals = goldMedals;
    }

    public void setSilverMedals(int silverMedals) {
        this.silverMedals = silverMedals;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalMedals() {
        return goldMedals + silverMedals + bronzeMedals;
    }

    public void setTotalMedals(int totalMedals) {
        this.totalMedals = totalMedals;
    }

}
