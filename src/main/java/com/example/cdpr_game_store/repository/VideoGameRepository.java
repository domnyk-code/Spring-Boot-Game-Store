package com.example.cdpr_game_store.repository;

import com.example.cdpr_game_store.entity.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoGameRepository extends JpaRepository<VideoGame, Long> {
    List<VideoGame> findByTitleContainingIgnoreCase(String title);

    List<VideoGame> findByGenres_NameIgnoreCase(String genreName);
}
