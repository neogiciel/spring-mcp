package com.neogiciel.spring_mcp_serveur.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Method;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import com.neogiciel.spring_mcp_serveur.model.Usager;
import com.neogiciel.spring_mcp_serveur.model.Structure;
import com.neogiciel.spring_mcp_serveur.model.McpServerInfo;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.server.McpSyncServer;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class McpService {

    // Tous mes serveurs MCP
    //private final McpSyncServer mcpServer;
   private final ApplicationContext context;

    public McpService(ApplicationContext context) {
        this.context = context;
    }


    /**
     * 1 - Initialisation / négociation avec le serveur MCP.
     */
    public McpServerInfo init() {
        return new McpServerInfo();
    }
     /**
     * 2 - Liste les Tools disponibles sur le serveur MCP.
     */
    public McpSchema.ListToolsResult list() {

        List<McpSchema.Tool> toolList = new ArrayList<>();

        //for (Map.Entry<String, String> entry : tools.entrySet()) {
        Map<String, Object> properties1 = Map.of(
            "rcu", Map.of(
                "type", "string",
                "description", "Identifiant RCU"
            )
        );

        McpSchema.JsonSchema inputSchema1 =
        new McpSchema.JsonSchema(
                "object",
                properties1,
                List.of("rcu"),
                false,
                null,
                null
        );
        
            
        McpSchema.Tool tool1 = McpSchema.Tool.builder()
                .name("rechercher_usager")
                .description("Recherche un usager à partir de son RCU")
                .inputSchema(inputSchema1)
                .meta(Map.of(
                    "id", 0,
                    "url", "/api/usagers/{rcu}"
                ))
                .build();

         toolList.add(tool1);

        //for (Map.Entry<String, String> entry : tools.entrySet()) {
        Map<String, Object> properties2 = Map.of(
            "rcu", Map.of(
                "type", "string",
                "description", "SIRET de la structure"
            )
        );

        McpSchema.JsonSchema inputSchema2 =
        new McpSchema.JsonSchema(
                "object",
                properties2,
                List.of("siret"),
                false,
                null,
                null
        );
        
     
        McpSchema.Tool tool2 = McpSchema.Tool.builder()
                .name("rechercher_structure")
                .description("Recherche une structure à partir de son SIRET")
                .inputSchema(inputSchema2)
                .meta(Map.of(
                    "id", 1,
                    "url", "/api/structures/{siret}"
                ))
                .build();


         toolList.add(tool2);

         return new McpSchema.ListToolsResult(toolList,null);
    
    }   
    /*public Map<String, String> listTools() {

        //Map<String, String> tools = new new ArrayList<>(); 

        Map<String, String> tools = new HashMap<>();

        for (String beanName : context.getBeanDefinitionNames()) {
            Object bean = context.getBean(beanName);

            Class<?> targetClass = org.springframework.aop.support.AopUtils.getTargetClass(bean);

            for (Method method : targetClass.getDeclaredMethods()) {
                McpTool annotation = method.getAnnotation(McpTool.class);
                if (annotation != null) {
                    tools.put(annotation.name(),annotation.description());
                }
            }
        }

        return tools;
    }*/

    @McpTool(
        name = "rechercher_usager",
        description = "Recherche un usager à partir de son identifiant RCU"
    )
    public Usager rechercherUsager(
        @McpToolParam(description = "Identifiant RCU", required = true) String rcu
    ) {
        return new Usager(rcu, "DUPONT", "Jean");
    }
  

    @McpTool(
        name = "rechercher_structure",
        description = "Recherche une structure à partir de son SIRET"
    )
    public Structure rechercherStructure(
        @McpToolParam(description = "SIRET de la structure", required = true)
        String siret
    ) {
        return new Structure(siret, "France Travail Bordeaux");
    }
}
