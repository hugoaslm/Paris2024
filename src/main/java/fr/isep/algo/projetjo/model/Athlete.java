package fr.isep.algo.projetjo.model;

public class Athlete {
    private int id;
    private String nom;
    private String prenom;
    private String pays;
    private int age;
    private String sex;
    private int sport;

    public Athlete(String nom, String prenom, String pays, int age, String sex, int id, int sport) {
        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.age = age;
        this.sex = sex;
        this.id = id;
        this.sport = sport;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getPays() {
        return pays;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

}