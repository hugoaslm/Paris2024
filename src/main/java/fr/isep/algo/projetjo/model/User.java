package fr.isep.algo.projetjo.model;

public class User {
    private int id;
    private String pseudo;
    private String mdp;
    private int admin;

    // Constructeur
    public User(int id, String pseudo, String mdp, int admin) {
        this.id = id;
        this.pseudo = pseudo;
        this.mdp = mdp;
        this.admin = admin;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMdp() {
        return mdp;
    }

    public int getRole() {
        return admin;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public void setRole(int admin) {
        this.admin = admin;
    }
}
