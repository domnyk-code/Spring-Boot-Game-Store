package com.example.cdpr_game_store.service;

import com.example.cdpr_game_store.entity.Genre;
import com.example.cdpr_game_store.entity.VideoGame;
import com.example.cdpr_game_store.repository.VideoGameRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VideoGameService {

    private final VideoGameRepository videoGameRepository;
    private final GenreService genreService;

    public VideoGameService(VideoGameRepository videoGameRepository, GenreService genreService) {
        this.videoGameRepository = videoGameRepository;
        this.genreService = genreService;
    }

//    Simple search functions
    public List<VideoGame> getAllGames() {
        return videoGameRepository.findAll();
    }
    public Optional<VideoGame> getGameById(Long id) {
        return videoGameRepository.findById(id);
    }
    public List<VideoGame> searchByTitle(String t)  {
        return videoGameRepository.findByTitleContainingIgnoreCase(t);
    }
    public List<VideoGame> filterByGenre(String genre) { return videoGameRepository.findByGenres_NameIgnoreCase(genre); }

//    Saves and returns a video game to repo, with genre list
    public VideoGame addGame(VideoGame game) {
        Set<Genre> resolvedGenres = checkGenres(game.getGenres());
        game.setGenres(resolvedGenres);
        return videoGameRepository.save(game);
    }

//    Deletes game from repo by id
    public void deleteGame(Long id) {
        videoGameRepository.deleteById(id);
    }

//    Updates and returns game based on input game
    public VideoGame updateGame(Long id, VideoGame updated) {
        return videoGameRepository.findById(id).map(game -> {

            game.setTitle(updated.getTitle());
            game.setPrice(updated.getPrice());
            game.setDescription(updated.getDescription());
            game.setGenres(updated.getGenres());
            return videoGameRepository.save(game);

        }).orElseThrow(() -> new RuntimeException("Game not found"));
    }

    // A method to check if all genres in a set are inside existing repo
    private Set<Genre> checkGenres(Set<Genre> genres) {
        return genres.stream()
                .map(g -> genreService.getGenreById(g.getId())
                        .orElseThrow(() -> new RuntimeException("Genre not found: " + g.getId())))
                .collect(Collectors.toSet());
    }
}
