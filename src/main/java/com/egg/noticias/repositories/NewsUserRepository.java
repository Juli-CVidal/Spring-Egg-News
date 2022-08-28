/*
// Curso Egg FullStack
 */
package com.egg.noticias.repositories;

import com.egg.noticias.entities.NewsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// @author JulianCVidal
@Repository
public interface NewsUserRepository extends JpaRepository<NewsUser, String> {

    @Query("SELECT n FROM NewsUser n WHERE n.email = :email")
    public NewsUser getUserByEmail(@Param("email") String email);
    
    @Query("SELECT n FROM NewsUser n WHERE n.id = :id")
    public NewsUser getUserById(@Param("id") String id);
}
