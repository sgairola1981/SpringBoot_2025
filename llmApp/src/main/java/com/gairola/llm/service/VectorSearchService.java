package com.gairola.llm.service;

import com.gairola.llm.entity.DocumentChunk;
import com.gairola.llm.repository.DocumentRepository;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VectorSearchService {

    private final EmbeddingModel embeddingModel;
    private final DocumentRepository repository;

    public String search(String question) {

        long start = System.currentTimeMillis();

        Embedding queryEmbedding = embeddingModel.embed(question).content();

        // ✅ load limited data
        List<DocumentChunk> chunks = repository.findTop100();

        List<ScoredChunk> scored = chunks.parallelStream() // 🔥 parallel
                .map(chunk -> {

                    float[] embedding = parseEmbedding(chunk.getEmbedding()); // still needed (temporary)

                    double score = cosineSimilarity(
                            queryEmbedding.vector(),
                            embedding
                    );

                    return new ScoredChunk(
                            // ✅ trim content EARLY (important)
                            chunk.getContent().length() > 200
                                    ? chunk.getContent().substring(0, 200)
                                    : chunk.getContent(),
                            score
                    );
                })
                .filter(sc -> sc.score() > 0.35) // early filter
                .sorted(Comparator.comparingDouble(ScoredChunk::score).reversed())
                .limit(2) // 🔥 reduce to 2
                .toList();

        if (scored.isEmpty()) return "";

        String context = scored.stream()
                .map(ScoredChunk::content)
                .collect(Collectors.joining("\n\n"));

        long end = System.currentTimeMillis();
        System.out.println("⚡ Vector Search Time: " + (end - start) + " ms");

        return context;
    }
    private record ScoredChunk(String content, double score) {}

    private float[] parseEmbedding(String s) {
        String clean = s.replace("[", "").replace("]", "");
        String[] parts = clean.split(",");
        float[] v = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            v[i] = Float.parseFloat(parts[i].trim());
        }
        return v;
    }

    private double cosineSimilarity(float[] a, float[] b) {
        int len = Math.min(a.length, b.length); // ✅ safe
        double dot = 0, normA = 0, normB = 0;

        for (int i = 0; i < len; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

}