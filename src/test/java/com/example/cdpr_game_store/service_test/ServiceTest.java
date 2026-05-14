package com.example.cdpr_game_store.service_test;

import com.example.cdpr_game_store.entity.VideoGame;
import com.example.cdpr_game_store.repository.GenreRepository;
import com.example.cdpr_game_store.repository.VideoGameRepository;
import com.example.cdpr_game_store.service.GenreService;
import com.example.cdpr_game_store.service.VideoGameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ServiceTest {

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

    @Mock
    private VideoGameRepository videoGameRepository;

    @InjectMocks
    private VideoGameService videoGameService;

    @Test
    void GetGameByIdFound() {
        VideoGame game = new VideoGame();
        game.setTitle("Hades");
        game.setPrice(39.99);
        game.setDescription("Roguelike dungeon crawler");

        when(videoGameRepository.findById(1L)).thenReturn(Optional.of(game));

        Optional<VideoGame> result = videoGameService.getGameById(1L);
        assertTrue(result.isPresent());
        assertEquals("Hades", result.get().getTitle());
    }

    @Test
    void GetGameByIdNotFound() {
        when(videoGameRepository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(videoGameService.getGameById(99L).isEmpty());
    }

    @Test
    void UpdateGameNotFound() {
        when(videoGameRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> videoGameService.updateGame(99L, new VideoGame()));
    }

    @Test
    void DeleteGameCallsRepository() {
        videoGameService.deleteGame(1L);
        verify(videoGameRepository, times(1)).deleteById(1L);
    }
}
