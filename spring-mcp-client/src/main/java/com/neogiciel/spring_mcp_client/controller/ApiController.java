package com.neogiciel.spring_mcp_client.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Base64;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neogiciel.spring_mcp_client.service.OllamaService;


@RestController
//@Controller
public class ApiController {

  /*Log*/
  protected static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
  
  
  /*Services*/
  private OllamaService ollamaService;
  private final RestClient restClient;
 
  /* 
  * Contructeur
  */
  public ApiController(OllamaService ollamaService, RestClient.Builder builder){
      this.ollamaService = ollamaService;
       this.restClient = builder
                .baseUrl("http://localhost:8081")
                .build();
  }


  public String getUrl(String url) throws Exception {
        return  restClient.get().uri(url).retrieve().body(String.class);
  }

  public String objectMapper(String reponse)   throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(reponse);
        return root.get("url").asText();
  }


  /*
  * Poser une question
  */
  @GetMapping(value ="/question", produces = MediaType.APPLICATION_JSON_VALUE)
  public String question() {
        LOGGER.info("[ApiController] ************** question **************" );

        try{
            String mcpServerInit = this.getUrl("/api/init") ;
            LOGGER.info("[ApiController] mcpServer init {} " + mcpServerInit);
            String mcpServerList = this.getUrl("/api/list") ;
            LOGGER.info("[ApiController] mcpServer list {} " + mcpServerList);

            //Inislitalisation des variables
            String  modeleia = "llama3.2";

            String question1 = """
            Tu dois sélectionner le tool le plus adapté pour répondre à la question utilisateur.

            RÈGLES :
            - Utilise uniquement les tools fournis.
            - Sélectionne un seul tool.
            - Ne réponds pas directement à la question utilisateur.
            - N'invente aucun tool, argument ou URL.
            - Utilise exactement l'id, le nom et l'URL définis dans le tool sélectionné.
            - Extrais de la question utilisateur les valeurs nécessaires aux arguments.
            - Construis l'URL en remplaçant les variables entre accolades par les valeurs extraites de la question.
            - Par exemple, si l'URL est "/api/structures/{siret}" et que le SIRET est "1234", retourne "/api/structures/1234".
            - Retourne uniquement un objet JSON valide.
            - Ne retourne aucun texte avant ou après le JSON.
            - N'utilise pas de bloc Markdown ```json.
            - Utilise exactement les propriétés JSON : "id", "nom", "arguments" et "url".

            FORMAT DE RÉPONSE OBLIGATOIRE :

            {
            "id": 0,
            "nom": "nom_du_tool",
            "arguments": {
                "nom_argument": "valeur"
            },
            "url": "/url/complete"
            }

            Question utilisateur :
            "Recherche la structure ayant le SIRET 1234"

            TOOLS DISPONIBLES :

            %s

            """.formatted(mcpServerList);

            String reponse1 = this.ollamaService.chat(modeleia,question1);
            LOGGER.info("[ApiController] reponse = {}",reponse1);
            
            String url = objectMapper(reponse1);
            LOGGER.info("[ApiController] url url {} " + url);

            String mcpServerApi = this.getUrl(url) ;
            LOGGER.info("[ApiController] mcpServer api {} " + mcpServerApi);

            String question2 = """
            Question utilisateur :
            "Recherche la structure ayant le SIRET 12345678901234"
            
            %s

            """.formatted(mcpServerApi);

            String reponse2 = this.ollamaService.chat(modeleia,question2);
            LOGGER.info("[ApiController] reponse = {}",reponse2);

            return reponse2;
        
        }catch(Exception e){
            LOGGER.info("[ApiController] erreur = {}",e.getMessage());
            return "Erreur";
        }

  }


  /*
  * Poser une question
  */
  @PutMapping(value ="/putquestion", produces = MediaType.APPLICATION_JSON_VALUE)
  public String putquestion(@RequestParam("question") String question) {
        LOGGER.info("[ApiController] ************** question **************" );
        LOGGER.info("[ApiController] question = "+question);

        try{

            String mcpServerInit = this.getUrl("/api/init") ;
            LOGGER.info("[ApiController] mcpServer init {} " + mcpServerInit);
            String mcpServerList = this.getUrl("/api/list") ;
            LOGGER.info("[ApiController] mcpServer list {} " + mcpServerList);

            //Inislitalisation des variables
            String  modeleia = "llama3.2";
            //String  modeleia = "gemma";

            String question1 = """
            Tu dois sélectionner le tool le plus adapté pour répondre à la question utilisateur.

            RÈGLES :
            - Utilise uniquement les tools fournis.
            - Sélectionne un seul tool.
            - Ne réponds pas directement à la question utilisateur.
            - N'invente aucun tool, argument ou URL.
            - Utilise exactement l'id, le nom et l'URL définis dans le tool sélectionné.
            - Extrais de la question utilisateur les valeurs nécessaires aux arguments.
            - Construis l'URL en remplaçant les variables entre accolades par les valeurs extraites de la question.
            - Par exemple, si l'URL est "/api/structures/{siret}" et que le SIRET est "1234", retourne "/api/structures/1234".
            - Ta réponse doit commencer directement par le caractère { et se terminer directement par le caractère }.
            - Retourne uniquement un objet JSON valide.
            - Ne retourne aucun autre caractère avant { ou après }.
            - N'utilise pas de bloc Markdown ```json.
            - N'utilise pas ```json.
            - N'utilise pas ```.
            - Utilise exactement les propriétés JSON : "id", "nom", "arguments" et "url".

            FORMAT DE RÉPONSE OBLIGATOIRE :

            {
            "id": 0,
            "nom": "nom_du_tool",
            "arguments": {
                "nom_argument": "valeur"
            },
            "url": "/url/complete"
            }

            Question utilisateur :
            "%s"

            TOOLS DISPONIBLES :

            %s

            """.formatted(question,mcpServerList);

            String reponse1 = this.ollamaService.chat(modeleia,question1);
            LOGGER.info("[ApiController] reponse = {}",reponse1);
            
            String url = objectMapper(reponse1);
            LOGGER.info("[ApiController] url url {} " + url);


            String mcpServerApi = this.getUrl(url) ;
            LOGGER.info("[ApiController] mcpServer api {} " + mcpServerApi);

            String question2 = """
            Question utilisateur :
            "%s"
            
            %s

            """.formatted(question,mcpServerApi);
            LOGGER.info("[ApiController] question2 = {}",question2);


           String reponse2 = this.ollamaService.chat(modeleia,question2);
           LOGGER.info("[ApiController] reponse = {}",reponse2);

            return reponse2;

            //return "test";
        
        }catch(Exception e){
            LOGGER.info("[ApiController] erreur = {}",e.getMessage());
            return "Erreur";
        }

  }


  //Réponse
  //Reponse 1
  //Pour trouver la structure associée au SIRET 12345678901234, vous pouvez utiliser la fonction `rechercher_structure` 
  // en passant le paramètre `siret` comme suit : ```bash rechercher_structure siret=12345678901234 ``` Cette commande affichera l'information de la structure associée au SIRET 12345678901234.
  //Reponse 2
  //Vous pouvez utiliser la fonction `rechercher_structure` pour effectuer la recherche. ```
  //bash rechercher_structure siret="12345678901234" ``` Cette commande recherchera la structure ayant le SIRET spécifique fourni et affichera les informations de la structure si elle est trouvée.

  /*String question = """
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

            Donne moi uniquement le numero du tool dans ta reponse             
            """;*/


            /*String question = """
            Question utilisateur :
            "Recherche la structure ayant le SIRET 12345678901234"
            
            TOOLS DISPONIBLES :

            %s

            Donne moi uniquement le numero du tool dans ta reponse             
            """.formatted(mcpServer);*/



}
