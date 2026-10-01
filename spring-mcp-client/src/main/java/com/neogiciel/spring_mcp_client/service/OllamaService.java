package com.neogiciel.spring_mcp_client.service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Base64;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.document.Document;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class OllamaService {

  protected static final Logger LOGGER = LoggerFactory.getLogger(OllamaService.class);
  private ChatClient chatClient;
  private OllamaChatModel ollamaChatModel;
  private ChatMemory chatMemory;
  
  /*
  * Constructeur
  */
  public OllamaService(ChatClient.Builder builder,OllamaChatModel ollamaChatModel,ChatMemory chatMemory){
      this.chatClient = builder.build();
      this.ollamaChatModel=ollamaChatModel;
      this.chatMemory=chatMemory;
  }

  /*
  * chat
   */
  public  String chat(String model, String question){
   LOGGER.info("[OllamaChatService] ******chat********");
   LOGGER.info("[OllamaChatService] model = "+ model);
   LOGGER.info("[OllamaChatService] question = "+ question);

  // Set the model in options
  OllamaOptions options = OllamaOptions.builder()
               .model(model)
               .build();


   // Create a new ChatClient with the specified model
   var chatClientLocal = ChatClient.builder(ollamaChatModel)
                .defaultOptions(options)
                .build();


    return chatClientLocal.prompt()
                 .user(question)
                .call()
                .content();

   
  }



  
}