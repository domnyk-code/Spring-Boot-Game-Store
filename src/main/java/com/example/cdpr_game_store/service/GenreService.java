package com.example.cdpr_game_store.service;

import com.example.cdpr_game_store.entity.Genre;
import com.example.cdpr_game_store.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    //    Simple search functions
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    public Optional<Genre> getGenreByName(String name) {
        return genreRepository.findByNameIgnoreCase(name);
    }

    //    Saves and returns a genre to repo
    public Genre addGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    //    Deletes a genre from repo
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
