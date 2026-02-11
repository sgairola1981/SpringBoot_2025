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

    // ✅ SAFE Oracle CLOB search (NO ORA-06502)
    @Query(value = """
        SELECT * FROM (
            SELECT w.*, ROWNUM rn FROM (
                SELECT * FROM web_pages w
                WHERE DBMS_LOB.INSTR(LOWER(w.content), LOWER(:query)) > 0
                   OR LOWER(w.title) LIKE LOWER(:likeQuery)
                ORDER BY w.scraped_at DESC
            ) w
            WHERE ROWNUM <= :maxRows
        )
        WHERE rn > :offset
        """, nativeQuery = true)
    List<WebPage> searchPagesManual(
            @Param("query") String query,
            @Param("likeQuery") String likeQuery,
            @Param("offset") long offset,
            @Param("maxRows") long maxRows
    );

    // ✅ COUNT query (also safe)
    @Query(value = """
        SELECT COUNT(*) FROM web_pages w
        WHERE DBMS_LOB.INSTR(LOWER(w.content), LOWER(:query)) > 0
           OR LOWER(w.title) LIKE LOWER(:likeQuery)
        """, nativeQuery = true)
    long countByQuery(
            @Param("query") String query,
            @Param("likeQuery") String likeQuery
    );

    // ✅ Pageable version
    @Query(
            value = """
            SELECT * FROM web_pages w
            WHERE DBMS_LOB.INSTR(LOWER(w.content), LOWER(:query)) > 0
               OR LOWER(w.title) LIKE LOWER(:likeQuery)
            ORDER BY w.scraped_at DESC
            """,
            countQuery = """
            SELECT COUNT(*) FROM web_pages w
            WHERE DBMS_LOB.INSTR(LOWER(w.content), LOWER(:query)) > 0
               OR LOWER(w.title) LIKE LOWER(:likeQuery)
            """,
            nativeQuery = true
    )
    Page<WebPage> searchPages(
            @Param("query") String query,
            @Param("likeQuery") String likeQuery,
            Pageable pageable
    );

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

    // ✅ Add this method (replaces findByUrl)
    @Query("SELECT w FROM WebPage w WHERE w.url = :url")
    List<WebPage> findByUrlStartingWith(@Param("url") String url);
    @Query("SELECT w FROM WebPage w ORDER BY w.scrapedAt DESC")
    List<WebPage> findRecent(int limit);
    // Add to existing interface:
    @Query("SELECT w FROM WebPage w WHERE w.sessionId = :sessionId ORDER BY w.scrapedAt DESC")
    Page<WebPage> findBySessionIdOrderByScrapedAtDesc(@Param("sessionId") String sessionId, Pageable pageable);

    Optional<WebPage> findByUrlAndSessionId(String url, String sessionId);

    Page<WebPage> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title, String content, Pageable pageable
    );
    @Query(
            value = """
        SELECT * FROM (
            SELECT w.*, ROWNUM rnum FROM (
                SELECT * FROM web_pages w
                WHERE
                    DBMS_LOB.INSTR(LOWER(w.content), LOWER(:keyword)) > 0
                    OR LOWER(w.title) LIKE LOWER(:title)
                ORDER BY w.scraped_at DESC
            ) w
            WHERE ROWNUM <= :end
        )
        WHERE rnum > :start
        """,
            countQuery = """
        SELECT COUNT(*) FROM web_pages w
        WHERE
            DBMS_LOB.INSTR(LOWER(w.content), LOWER(:keyword)) > 0
            OR LOWER(w.title) LIKE LOWER(:title)
        """,
            nativeQuery = true
    )
    List<WebPage> searchOracle(
            @Param("keyword") String keyword,
            @Param("title") String title,
            @Param("start") int start,
            @Param("end") int end

    );

    // ✅ COUNT QUERY (THIS WAS MISSING)
    @Query(value = """
    SELECT COUNT(*)
    FROM web_pages
    WHERE TITLE LIKE :q OR TITLE LIKE :q2
""", nativeQuery = true)
    long countOracle(@Param("q") String q,
                     @Param("q2") String q2);

    @Query(
            value = """
    SELECT *
    FROM (
        SELECT t.*, ROWNUM rn
        FROM (
            SELECT *
            FROM web_pages
            WHERE DBMS_LOB.INSTR(content, :query) > 0
            ORDER BY scraped_at DESC
        ) t
        WHERE ROWNUM <= :endRow
    )
    WHERE rn > :startRow
  """,
            nativeQuery = true
    )
    List<WebPage> searchOracle11gNoIndex(
            @Param("query") String query,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow
    );

    @Query(
            value = """
    SELECT COUNT(*)
    FROM web_pages
    WHERE DBMS_LOB.INSTR(LOWER(content), LOWER(:query)) > 0
  """,
            nativeQuery = true
    )
    long countOracle11gNoIndex(@Param("query") String query);


}
