package com.qi.modal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "project_images")
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private RecentProjects project;
}
