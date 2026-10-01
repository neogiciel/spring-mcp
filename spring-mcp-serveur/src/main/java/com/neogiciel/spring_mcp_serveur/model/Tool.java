package com.neogiciel.spring_mcp_serveur.model;

public class Tool {

    private String name;
    private String description;

    public Tool(String name,String description){
        this.name = name;
        this.description = description;  
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }
}
