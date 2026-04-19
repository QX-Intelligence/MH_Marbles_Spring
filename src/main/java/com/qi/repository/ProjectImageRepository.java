package com.qi.repository;

import com.qi.modal.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectImageRepository extends JpaRepository<ProjectImage,String> {
    @Modifying
    @Query("DELETE FROM ProjectImage i WHERE i.project.id = :projectId")
    void deleteByProjectId(String projectId);

    @Query("SELECT i.imageUrl FROM ProjectImage i WHERE i.project.id = :projectId")
    List<String> findImageKeysByProjectId(String projectId);


}
