package com.gairola.GairolaSearchEngine.repository;


import com.gairola.GairolaSearchEngine.entity.WebPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WebPageRepository extends JpaRepository<WebPage, Long> {

    // ✅ ADD THESE METHODS
    Optional<WebPage> findByUrl(String url);

    @Query("SELECT w FROM WebPage w WHERE " +
            "LOWER(w.content) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
            "LOWER(w.title) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<WebPage> findByContentOrTitleContainingIgnoreCase(String keyword, Pageable pageable);
}