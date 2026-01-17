
package com.gairola.GairolaSearchEngine.repository;


import com.gairola.GairolaSearchEngine.entity.WebPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebPageRepository extends JpaRepository<WebPage, Long> {

    /**
     * ✅ FIXED: Native SQL for Oracle CLOB compatibility
     * Derived queries fail on CLOB + LOWER/CONCAT
     */
    @Query(value = """
        SELECT * FROM web_pages w 
        WHERE LOWER(DBMS_LOB.SUBSTR(w.content, 4000, 1)) LIKE LOWER(:query) 
           OR LOWER(w.title) LIKE LOWER(:query)
        ORDER BY w.scraped_at DESC
        """,
            countQuery = """
        SELECT COUNT(*) FROM web_pages w 
        WHERE LOWER(DBMS_LOB.SUBSTR(w.content, 4000, 1)) LIKE LOWER(:query) 
           OR LOWER(w.title) LIKE LOWER(:query)
        """,
            nativeQuery = true)
    Page<WebPage> searchPages(@Param("query") String query, Pageable pageable);

    // Stats
    long countByDepth(int depth);

    @Query(value = "SELECT AVG(LENGTH(w.content)) FROM web_pages w WHERE w.content IS NOT NULL", nativeQuery = true)
    Double averageContentLength();

    // Recent pages
    @Query(value = "SELECT * FROM web_pages WHERE depth = :depth ORDER BY scraped_at DESC", nativeQuery = true)
    Page<WebPage> findRecentByDepth(@Param("depth") int depth, Pageable pageable);
    // ✅ ADD THESE METHODS
    Optional<WebPage> findByUrl(String url);
}