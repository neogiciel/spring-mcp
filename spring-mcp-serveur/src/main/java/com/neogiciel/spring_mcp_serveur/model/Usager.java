package com.neogiciel.spring_mcp_serveur.model;

public class Usager {

    private String rcu;
    private String nom;
    private String prenom;

    public Usager(String rcu,String nom,String prenom){
        this.rcu = rcu;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String  getRcu(){
        return rcu;
    }

    public String  getNom(){
        return nom;
    }
    public String  getPrenom(){
        return prenom;
    }


}
