package fr.isep.algo.projetjo;

public class Athlete {
    private String name;
    private String country;
    private int age;
    private String sex;

    public Athlete(String name, String country, int age, String sex) {
        this.name = name;
        this.country = country;
        this.age = age;
        this.sex = sex;
    }

    // Getters et setters
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void deleteAge() {
        this.age = 0;
    }

    public void deleteCountry() {
        this.country = "";
    }

    public void deleteSex() {
        this.sex = "";
    }

    public void deleteName() {
        this.name = "";
    }
}