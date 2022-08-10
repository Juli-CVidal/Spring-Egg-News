/*
// Curso Egg FullStack
 */
package com.egg.noticias.repositories;

// @author JulianCVidal
import com.egg.noticias.entities.News;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {

    @Query("SELECT n FROM News WHERE n.deleted = false")
    List<News> getAllNews();

    @Query("SELECT n FROM News n WHERE n.title = :title")
    News searchByTitle(@Param("title") String title);

    @Query("SELECT n FROM News n WHERE n.journalist.name = :name")
    List<News> searchByJournalist(@Param("name") String name);
}
