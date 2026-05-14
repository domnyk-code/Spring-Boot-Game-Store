package com.example.cdpr_game_store.repository_test;

import com.example.cdpr_game_store.entity.Genre;
import com.example.cdpr_game_store.entity.VideoGame;
import com.example.cdpr_game_store.repository.GenreRepository;
import com.example.cdpr_game_store.repository.VideoGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
class RepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("gamestore-test")
            .withUsername("test")
            .withPassword("test");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private VideoGameRepository gameRepository;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void setup() {
        gameRepository.deleteAll();
        genreRepository.deleteAll();

        Genre rpg = new Genre();
        rpg.setName("RPG");

        Genre action = new Genre();
        action.setName("Action");

        genreRepository.save(rpg);
        genreRepository.save(action);

        VideoGame witcher = new VideoGame();
        witcher.setTitle("The Witcher 3");
        witcher.setPrice(29.99);
        witcher.setDescription("Open world RPG");
        witcher.setGenres(Set.of(rpg, action));

        VideoGame fifa =new VideoGame();
        fifa.setTitle("FIFA 2025");
        fifa.setPrice(39.99);
        fifa.setDescription("Football simulator");

        gameRepository.saveAll(List.of(witcher, fifa));
    }

    @Test
    void searchByTitle() {
        List<VideoGame> results = gameRepository.findByTitleContainingIgnoreCase("witcher");
        assertEquals(1, results.size());
        assertEquals("The Witcher 3", results.getFirst().getTitle());
    }

    @Test
    void searchByTitleCaseInsensitive() {
        List<VideoGame> results = gameRepository.findByTitleContainingIgnoreCase("WITCHER");
        assertFalse(results.isEmpty());
    }

    @Test
    void filterByGenreReturnsGamesWithThatGenre() {
        List<VideoGame> results = gameRepository.findByGenres_NameIgnoreCase("RPG");
        assertEquals(1, results.size());
        assertEquals("The Witcher 3", results.getFirst().getTitle());
    }

    @Test
    void videoGameCanHaveMultipleGenres() {
        List<VideoGame> results = gameRepository.findByTitleContainingIgnoreCase("witcher");
        assertEquals(2, results.getFirst().getGenres().size());
    }

    @Test
    void filterByGenreReturnsEmpty() {
        List<VideoGame> results = gameRepository.findByGenres_NameIgnoreCase("Horror");
        assertTrue(results.isEmpty());
    }
}
