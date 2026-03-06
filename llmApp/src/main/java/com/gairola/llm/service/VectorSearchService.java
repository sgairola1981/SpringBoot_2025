package com.gairola.llm.service;

import com.gairola.llm.entity.DocumentChunk;
import com.gairola.llm.repository.DocumentRepository;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorSearchService {

    private final EmbeddingModel embeddingModel;
    private final DocumentRepository repository;

    public String search(String question) {

        // Generate embedding for question
        Embedding queryEmbedding = embeddingModel.embed(question).content();

      //  List<DocumentChunk> chunks = repository.findAll();
        List<DocumentChunk> chunks =repository.searchContent(question);

        String bestContext = "";
        double bestScore = -1;

        for(DocumentChunk chunk : chunks){

            double score = cosineSimilarity(
                    queryEmbedding.vector(),
                    parseEmbedding(chunk.getEmbedding())
            );

            if(score > bestScore){
                bestScore = score;
                bestContext = chunk.getContent();
            }
        }

        return bestContext;
    }

    private float[] parseEmbedding(String embeddingString){

        String clean = embeddingString.replace("[","").replace("]","");
        String[] parts = clean.split(",");

        float[] vector = new float[parts.length];

        for(int i = 0; i < parts.length; i++){
            vector[i] = Float.parseFloat(parts[i].trim());
        }

        return vector;
    }

    private double cosineSimilarity(float[] a, float[] b){

        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for(int i = 0; i < a.length; i++){

            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}