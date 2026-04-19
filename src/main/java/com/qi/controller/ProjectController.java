package com.qi.controller;

import com.qi.dto.ProjectResponse;
import com.qi.service.OurService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/spring/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final OurService ourService;


    @PostMapping("/upload")
    public String uploadProject(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam("images") List<MultipartFile> images
    ) {
        return ourService.uploadProject(title, description, images);
    }


    @GetMapping("/get-all")
    public List<ProjectResponse> getProjects(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ourService.getProjects(cursor, size);
    }


    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable String id) {
        return ourService.deleteProject(id);
    }
}