package com.laszlo.gamereviewportal;

import com.laszlo.gamereviewportal.entity.GameEntity;
import com.laszlo.gamereviewportal.entity.GameGenreEntity;
import com.laszlo.gamereviewportal.entity.GenreEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameEntityTest {

    private GameEntity minecraft;
    private GenreEntity actionGenre;
    private GenreEntity adventureGenre;

    @BeforeEach
    void setup() {
        // Kategóriák létrehozása
        actionGenre = new GenreEntity();
        actionGenre.setId(1L);
        actionGenre.setName("Action");

        adventureGenre = new GenreEntity();
        adventureGenre.setId(2L);
        adventureGenre.setName("Adventure");

        // Minecraft játék létrehozása
        minecraft = new GameEntity();
        minecraft.setId(2L);
        minecraft.setTitle("Minecraft");

        // Kapcsolatok létrehozása
        GameGenreEntity actionConnection = new GameGenreEntity();
        actionConnection.setId(1L);
        actionConnection.setGame(minecraft);
        actionConnection.setGenre(actionGenre);

        GameGenreEntity adventureConnection = new GameGenreEntity();
        adventureConnection.setId(2L);
        adventureConnection.setGame(minecraft);
        adventureConnection.setGenre(adventureGenre);

        // Kapcsolatok hozzáadása a játékhoz
        Set<GameGenreEntity> gameGenres = new HashSet<>();
        gameGenres.add(actionConnection);
        gameGenres.add(adventureConnection);

        minecraft.setGameGenres(gameGenres);
    }

    @Test
    void testMinecraftCategories() {
        // A kategóriák neveinek listázása
        Set<GameGenreEntity> gameGenres = minecraft.getGameGenres();
        assertEquals(2, gameGenres.size(), "Minecraft should have 2 categories");

        // Ellenőrizd, hogy mindkét kategória szerepel-e
        Set<String> genreNames = gameGenres.stream()
                .map(gameGenre -> gameGenre.getGenre().getName())
                .collect(Collectors.toSet());

        assertTrue(genreNames.contains("Action"), "Minecraft should have 'Action' category");
        assertTrue(genreNames.contains("Adventure"), "Minecraft should have 'Adventure' category");

        // Ellenőrizd a toString egyes részeit
        String toStringResult = minecraft.toString();
        assertTrue(toStringResult.contains("id=2"), "ID should be 2 in toString");
        assertTrue(toStringResult.contains("title='Minecraft'"), "Title should be 'Minecraft' in toString");
        assertTrue(toStringResult.contains("genres=[Action, Adventure]") || toStringResult.contains("genres=[Adventure, Action]"),
                "Genres should contain both 'Action' and 'Adventure' in any order");
    }
}
