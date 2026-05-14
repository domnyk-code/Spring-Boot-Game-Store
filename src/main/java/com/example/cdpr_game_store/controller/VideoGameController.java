package com.example.cdpr_game_store.controller;

import com.example.cdpr_game_store.entity.VideoGame;
import com.example.cdpr_game_store.service.VideoGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class VideoGameController {

    private final VideoGameService videoGameService;

    public VideoGameController(VideoGameService videoGameService) {
        this.videoGameService = videoGameService;
    }

    // visitor endpoints
    @GetMapping
    public List<VideoGame> getAll() {
        return videoGameService.getAllGames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoGame> getById(@PathVariable Long id) {
        return videoGameService.getGameById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<VideoGame> search(@RequestParam String title) {
        return videoGameService.searchByTitle(title);
    }

    @GetMapping("/genre/{name}")
    public List<VideoGame> byGenre(@PathVariable String name) {
        return videoGameService.filterByGenre(name);
    }

    // admin endpoints
    @PostMapping
    public ResponseEntity<VideoGame> add(@RequestBody VideoGame game) {
        return ResponseEntity.status(201).body(videoGameService.addGame(game));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoGame> update(@PathVariable Long id, @RequestBody VideoGame game) {
        return ResponseEntity.ok(videoGameService.updateGame(id, game));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        videoGameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

}
