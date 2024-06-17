package genspark.pj.SecureAuthenticationSystem.Repository;

import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogDAO extends JpaRepository<Blog, Long> {
    @Query("SELECT b FROM Blog b WHERE b.posted = true")
    List<Blog> getPostedBlogs();

    @Query("SELECT b FROM Blog b WHERE b.posted = false")
    List<Blog> getUnPostedBlogs();

    @Query("SELECT b FROM Blog b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Blog> findByTitle(String title);

    @Query("SELECT b FROM Blog b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Blog> findByAuthor(String author);

    @Query(value = "SELECT * from Blog ORDER BY title", nativeQuery = true)
    List<Blog> findBlogSortedTitle();

    @Modifying
    @Transactional
    @Query("UPDATE Blog b SET b.posted = NOT b.posted WHERE b.id = :id")
    void togglePostedStatus(Long id);
}
