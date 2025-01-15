package com.laszlo.gamereviewportal;

import com.laszlo.gamereviewportal.entity.GameEntity;
import com.laszlo.gamereviewportal.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void testFindGamesBetweenDates() {
        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2013, 12, 31);

        List<GameEntity> games = gameRepository.findGamesBetweenDates(startDate, endDate);
        games.forEach(System.out::println);
    }
}

