package com.gairola.searchEngine.repository;

import com.gairola.searchEngine.entity.WebPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface WebPageRepository extends JpaRepository<WebPage, Long> {
    @Query("""
        SELECT w FROM WebPage w 
        WHERE LOWER(w.title) LIKE LOWER(CONCAT('%', :query, '%')) 
           OR LOWER(w.content) LIKE LOWER(CONCAT('%', :query, '%'))
        ORDER BY w.scrapedAt DESC
        """)
    List<WebPage> search(@Param("query") String query);
}