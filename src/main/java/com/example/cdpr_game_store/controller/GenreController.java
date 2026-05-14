package com.example.cdpr_game_store.controller;

import com.example.cdpr_game_store.entity.Genre;
import com.example.cdpr_game_store.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAll() { return genreService.getAll(); } // public

    @PostMapping  // admin only
    public ResponseEntity<Genre> add(@RequestBody Genre genre) {
        return ResponseEntity.status(201).body(genreService.addGenre(genre));
    }

    @DeleteMapping("/{id}")  // admin only
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
