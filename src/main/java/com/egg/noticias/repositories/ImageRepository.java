/*
// Curso Egg FullStack
 */
package com.egg.noticias.repositories;

import com.egg.noticias.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author JulianCVidal
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

}
