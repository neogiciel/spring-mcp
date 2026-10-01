package com.neogiciel.spring_mcp_serveur.model;

public class Structure {
    private String siret;
    private String label;

    public Structure(String siret,String label){
        this.siret = siret;
        this.label = label;
    }

    public String  getSiret(){
        return siret;
    }

    public String  getLabel(){
        return label;
    }

}
