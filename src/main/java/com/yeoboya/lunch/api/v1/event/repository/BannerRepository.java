package com.yeoboya.lunch.api.v1.event.repository;

import com.yeoboya.lunch.api.v1.event.domain.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query(
            "SELECT b " +
                    "FROM Banner b " +
                    "WHERE b.startDate <= :now " +
                    "AND b.endDate >= :now " +
                    "ORDER BY b.displayOrder, b.endDate"
    )
    List<Banner> findAllByDateRange(
            @Param("now") LocalDateTime now);

    @Query(
            "SELECT b " +
                    "FROM Banner b " +
                    "WHERE b.displayLocation = :displayLocation " +
                    "AND b.startDate <= :now " +
                    "AND b.endDate >= :now " +
                    "ORDER BY b.displayOrder, b.endDate"
    )
    List<Banner> findAllByDisplayLocationAndDateRange(
            @Param("displayLocation") Banner.DisplayLocation displayLocation,
            @Param("now") LocalDateTime now);
}
