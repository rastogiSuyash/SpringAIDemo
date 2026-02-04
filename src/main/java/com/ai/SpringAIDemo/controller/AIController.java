package com.ai.SpringAIDemo.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AIController {

//    @Autowired
//    private OpenAiChatModel chatModel;

    @Autowired
    private VectorStore vectorStore;

    private ChatClient chatClient;
    private ChatMemory chatMemory= MessageWindowChatMemory.builder().build(); /// for chat memory

    @Autowired
    private EmbeddingModel embeddingModel;
    public AIController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @GetMapping("/api/gpt/{message}")
    public String hello(@PathVariable String message){

//        String msg = chatModel.call("I need a message to greet user");
//        String msg = chatClient
//                .prompt(message)
//                .call()
//                .content();
        ChatResponse msg = chatClient
                .prompt(message)
                .call()
                .chatResponse();

        String result = msg.getResult().getOutput().getText();

        System.out.println(msg.getMetadata().getModel());
        System.out.println(msg.getMetadata().getUsage().getTotalTokens());
        return result;
    }


    @GetMapping("/api/movie")
    public String getMovie(@RequestParam String type, @RequestParam String year, @RequestParam String lang){
        String temp = """
                I want to watch a {type} movie with a good rating released in {year} in {lang} language. Suggest 
                me one specific movie and tell me the cast and length of the movie.
                
                Response format should be :
                Movie name : <name>
                Cast : <cast>
                Length : <length>
                IMDB rating : <rating>
                Basic Plot : <plot>
                """;

        PromptTemplate promptTemplate = PromptTemplate
                .builder()
                .template(temp)
                .build();

        Prompt prompt = promptTemplate.create(Map.of(
                "type", type,
                    "year", year,
                    "lang", lang
        ));

        String result = chatClient.prompt(prompt).call().content();

        return result;
    }
    @GetMapping("/api/embeddings")
    public float[] getVectors(String text){
        return embeddingModel.embed(text);
    }

    @GetMapping("/api/similarity")
    public double getSimilarity(@RequestParam String text1, @RequestParam String text2){
        //cosine similarity
        float[] embeding1=embeddingModel.embed(text1);
        float[] embeding2=embeddingModel.embed(text2);
        double dotProduct=0.0;
        double normA=0.0;
        double normB=00.;

        for ( int i = 0 ; i<embeding1.length;i++){
            dotProduct+= embeding1[i]*embeding2[i];
            normA+=Math.pow((embeding1[i]),2);
            normB+=Math.pow((embeding2[i]),2);
        }
//        System.out.println(dotProduct/ (Math.sqrt(normA)*Math.sqrt(normB)));
        return dotProduct/ (Math.sqrt(normA)*Math.sqrt(normB));
    }

    @GetMapping("/api/products")
    public List<Document> getProducts(@RequestParam String text){

//        return vectorStore.similaritySearch(text);
        return vectorStore.similaritySearch(SearchRequest.builder()
                .query(text)
                .topK(2)
                .build());
    }

    @GetMapping("/api/ask/{query}")
    public String productInfo(@PathVariable String query) {

        /*// Retrieval
        List<Document> docs = vectorStore.similaritySearch(query);

        // Augmentation
        StringBuilder context = new StringBuilder();
        for (Document doc : docs) {
            context.append(doc.getFormattedContent()).append("\n");
        }

        // Generation
        String prompt = """
                         You are a helpful product suggestion assistant.
                Use only the information in the product details below to answer the user.
                If the information is not available there, say you don't know.
                Product details:
                %s
                User question: %s
                Answer in a short, clear way with price and other relevant details:
                """.formatted(context, query);

        return chatClient.prompt(prompt).call().content();*/


        // Automatic RAG
        String template = """
                 {query}
                 Context information is below.
                -------------------------------
                {question_answer_context}
                -------------------------------
                Given the context information and no prior knowledge, answer the query with name and price
                , category and description.
                
                Follow these rules:
                1. If the answer is not in the context, just say that you don't know.
                2. Avoid statements like "Based on the context..." or "The provided information....".
                """;

        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(template)
                .build();


        return chatClient
                .prompt(query)
                .advisors(QuestionAnswerAdvisor
                        .builder(vectorStore)
                        .promptTemplate(promptTemplate)
                        .build())
                .call()
                .content();
    }


}
