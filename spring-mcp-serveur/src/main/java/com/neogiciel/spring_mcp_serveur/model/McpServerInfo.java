package com.neogiciel.spring_mcp_serveur.model;

import java.util.List;


public class McpServerInfo {

       private String name;
       private String version;
       private List<String> capabilities;

        public McpServerInfo(){
            this.name = "spring-mcp-serveur";
            this.name = "1.0.0";
            this.capabilities= List.of("tools","resources","prompts","completions");
        }
        public String getName(){
            return name;
        }
    
        public String getVersion(){
            return version;
        }
        public List<String> getCapabilities(){
            return capabilities;
        }
}
