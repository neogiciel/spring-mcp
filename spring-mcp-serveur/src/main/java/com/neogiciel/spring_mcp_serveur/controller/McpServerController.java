package com.neogiciel.spring_mcp_serveur.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import com.neogiciel.spring_mcp_serveur.service.McpService;
import com.neogiciel.spring_mcp_serveur.model.Usager;
import com.neogiciel.spring_mcp_serveur.model.Structure;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.http.MediaType;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;

import com.neogiciel.spring_mcp_serveur.model.McpServerInfo;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/* 
Question utilisateur :

"Recherche la structure ayant le SIRET 12345678901234"


TOOLS DISPONIBLES :

1. rechercher_usager
   Description :
   Recherche un usager à partir de son identifiant RCU

   Paramètre :
   rcu : string


2. rechercher_structure
   Description :
   Recherche une structure à partir de son SIRET

   Paramètre :
   siret : string

Reponse:
Tool sélectionné : 2
Nom              : rechercher_structure
Paramètre        : siret
Valeur           : 12345678901234
*/

@RestController 
@RequestMapping("/api")
public class McpServerController {

    Logger logger = LoggerFactory.getLogger(McpServerController.class);

    private final McpService mcpService;

    McpServerController(McpService mcpService){
        this.mcpService = mcpService;
    }


    /*
     * Page Index
    */
    @GetMapping("/init")
    public McpSchema.InitializeResult init() {
    //public McpServerInfo init() {
        logger.info("[PageController] *************** init ****************** ");
        McpServerInfo mcpServerInfo = mcpService.init();

         McpSchema.Implementation serverInfo =
            new McpSchema.Implementation(
                    mcpServerInfo.getName(),
                    mcpServerInfo.getVersion()
            );

        McpSchema.ServerCapabilities capabilities =
            McpSchema.ServerCapabilities.builder()
                    .tools(true)
                    .resources(true, true)
                    .prompts(true)
                    .completions()
                    .build();

        return new McpSchema.InitializeResult(
            "2025-06-18",
            capabilities,
            serverInfo,
            null );
    }

     
    /*
     * Page Index
    */
    @GetMapping("/list")
    public McpSchema.ListToolsResult list() {
    //public Map<String, String>  list() {
        logger.info("[PageController] *************** list ****************** ");
        //return new McpSchema.ListToolsResult(toolList,null);
        return this.mcpService.list();



    }

    /*
     * Page Index
    */
   @GetMapping(value ="/usagers/{rcu}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Usager rechercherUsager(@PathVariable String rcu) {
        logger.info("[PageController] *************** rechercherUsager ****************** ");
        Usager usager = mcpService.rechercherUsager("1234");
        logger.info("[PageController] rcu = "+ usager.getRcu());
        logger.info("[PageController] nom "+ usager.getNom());
        logger.info("[PageController] prenom "+ usager.getPrenom());
        return usager;
    }

    /*
     * Page Index
    */
   @GetMapping(value ="/structures/{siret}", produces = MediaType.APPLICATION_JSON_VALUE)
   public Structure rechercherStructure(@PathVariable String siret) {
        logger.info("[PageController] *************** rechercherStructure ****************** ");
        Structure structure = mcpService.rechercherStructure(siret);
        logger.info("[PageController] label = "+structure.getLabel());
        logger.info("[PageController] siret = "+structure.getSiret());
        return structure;
    }



}
