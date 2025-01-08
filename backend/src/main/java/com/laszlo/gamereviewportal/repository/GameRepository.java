package com.laszlo.gamereviewportal.repository;

import com.laszlo.gamereviewportal.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {

    // Az összes játék lekérése
    List<GameEntity> findAll();

    // Játékok keresése cím alapján
    Optional<GameEntity> findByTitle(String title);

    // Játékok keresése cím részlete alapján (pl. keresőmező támogatása)
    List<GameEntity> findByTitleContainingIgnoreCase(String title);

    // Játékok keresése fejlesztő alapján
    List<GameEntity> findByDeveloper_Id(Long developerId);

    // Játékok szűrése kiadási dátum szerint
    List<GameEntity> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate);
    //@Query("SELECT g FROM GameEntity g LEFT JOIN FETCH g.gameGenres WHERE g.releaseDate BETWEEN :startDate AND :endDate")
    //List<GameEntity> findGamesBetweenDates(
    //     @Param("startDate") LocalDate startDate,
    //     @Param("endDate") LocalDate endDate
    //    );

    // Játékok listázása értékelések szerint növekvő vagy csökkenő
    List<GameEntity> findByOrderByAverageRatingDesc();
    List<GameEntity> findByOrderByAverageRatingAsc();

    // Pontos árra keresés
    List<GameEntity> findByPrice(BigDecimal price);

    // Pontos értékelésre keresés
    List<GameEntity> findByAverageRating(BigDecimal averageRating);

    // Ár alapú összehasonlítás
    List<GameEntity> findByPriceLessThan(BigDecimal price);
    List<GameEntity> findByPriceGreaterThan(BigDecimal price);
    List<GameEntity> findByPriceLessThanEqual(BigDecimal price);
    List<GameEntity> findByPriceIsGreaterThanEqual(BigDecimal price);

    // Értékelés alapú összehasonlítás
    List<GameEntity> findByAverageRatingLessThan(BigDecimal averageRating);
    List<GameEntity> findByAverageRatingGreaterThan(BigDecimal averageRating);
    List<GameEntity> findByAverageRatingLessThanEqual(BigDecimal averageRating);
    List<GameEntity> findByAverageRatingGreaterThanEqual(BigDecimal averageRating);

}
