package com.qi.repository;

import com.qi.modal.RecentProjects;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ProjectRepo extends JpaRepository<RecentProjects,String> {
    @Query("SELECT DISTINCT p FROM RecentProjects p LEFT JOIN FETCH p.images")
    List<RecentProjects> findAllWithImages();
    @Query("SELECT p FROM RecentProjects p LEFT JOIN FETCH p.images WHERE p.id = :id")
    Optional<RecentProjects> findByIdWithImages(String id);

    @Query("""
        SELECT p FROM RecentProjects p
        WHERE (:cursor IS NULL OR p.createdAt < :cursor)
        ORDER BY p.createdAt DESC
    """)
    List<RecentProjects> findProjectsPage(Instant cursor, Pageable pageable);
}
