package com.qi.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
@Data
public class ProjectResponse {
    private String id;
    private String title;
    private String description;
    private List<String> imageUrls;
    private Instant createdAt;
}
