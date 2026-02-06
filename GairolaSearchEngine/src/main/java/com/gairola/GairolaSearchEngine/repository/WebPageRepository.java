package com.gairola.GairolaSearchEngine.repository;

import com.gairola.GairolaSearchEngine.entity.WebPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebPageRepository extends JpaRepository<WebPage, Long> {

    // ✅ Main search (manual ROWNUM for Oracle 11g)
    @Query(value = """
        SELECT * FROM (
            SELECT w.*, ROWNUM as rn FROM (
                SELECT * FROM web_pages w 
                WHERE LOWER(DBMS_LOB.SUBSTR(w.content, 4000, 1)) LIKE LOWER(:query) 
                   OR LOWER(w.title) LIKE LOWER(:query)
                ORDER BY w.scraped_at DESC
            ) w 
            WHERE ROWNUM <= :maxRows
        ) 
        WHERE rn > :offset
        """, nativeQuery = true)
    List<WebPage> searchPagesManual(@Param("query") String query, @Param("offset") long offset, @Param("maxRows") long maxRows);

    @Query(value = """
        SELECT COUNT(*) FROM web_pages w 
        WHERE LOWER(DBMS_LOB.SUBSTR(w.content, 4000, 1)) LIKE LOWER(:query) 
           OR LOWER(w.title) LIKE LOWER(:query)
        """, nativeQuery = true)
    long countByQuery(@Param("query") String query);

    // ✅ Stats
    long countByDepth(int depth);

    @Query(value = "SELECT COUNT(DISTINCT url) FROM web_pages", nativeQuery = true)
    long countDistinctUrls();

    @Query(value = "SELECT AVG(LENGTH(content)) FROM web_pages WHERE content IS NOT NULL", nativeQuery = true)
    Double averageContentLength();

    // ✅ Recent pages (manual pagination)
    @Query(value = """
        SELECT * FROM (
            SELECT w.*, ROWNUM rn FROM (
                SELECT * FROM web_pages ORDER BY scraped_at DESC
            ) w WHERE ROWNUM <= :limit
        ) WHERE rn > :offset
        """, nativeQuery = true)
    List<WebPage> findRecentManual(@Param("offset") int offset, @Param("limit") int limit);

    Optional<WebPage> findByUrl(String url);
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
    // ✅ Add this method (replaces findByUrl)
    @Query("SELECT w FROM WebPage w WHERE w.url = :url")
    List<WebPage> findByUrlStartingWith(@Param("url") String url);
    @Query("SELECT w FROM WebPage w ORDER BY w.scrapedAt DESC")
    List<WebPage> findRecent(int limit);

}
