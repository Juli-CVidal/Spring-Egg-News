/*
// Curso Egg FullStack
 */
package com.egg.news.repositories;

import com.egg.news.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author JulianCVidal
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    @Query("SELECT i FROM Image i WHERE i.id = :id")
    Image searchById(@Param("id") String id);
}
