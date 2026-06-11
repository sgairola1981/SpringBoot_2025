package com.gairola.ollama.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "knowledge_item")
@SequenceGenerator(
        name = "knowledge_item_seq_gen",
        sequenceName = "knowledge_item_seq",
        allocationSize = 1
)
public class KnowledgeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "knowledge_item_seq_gen")
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    public KnowledgeItem() {
    }

    public KnowledgeItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}