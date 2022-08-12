/*
// Curso Egg FullStack
 */
package com.egg.noticias.repositories;

// @author JulianCVidal
import com.egg.noticias.entities.Journalist;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalistRepository extends JpaRepository<Journalist, String> {

    @Query("SELECT j FROM Journalist j WHERE j.deleted = false")
    List<Journalist> getAllJournalists();

    @Query("SELECT j FROM Journalist j WHERE j.id = :id")
    Journalist searchById(@Param("id") String id);
    
    @Query("SELECT j FROM Journalist j WHERE j.name = :name")
    Journalist searchByName(@Param("name") String name);
}
