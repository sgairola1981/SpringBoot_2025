package com.gairola.llm.entity;

import jakarta.persistence.*;

@Entity
@Table(name="DOCUMENT_CHUNKS")
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doc_seq")
    @SequenceGenerator(name="doc_seq", sequenceName="DOC_CHUNK_SEQ", allocationSize=1)
    private Long id;

    @Lob
    private String content;

    @Lob
    private String embedding;

    private int chunkIndex;


    public Long getId() { return id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getEmbedding() { return embedding; }
    public void setEmbedding(String embedding) { this.embedding = embedding; }
    public int getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }
}