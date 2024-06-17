package genspark.pj.SecureAuthenticationSystem.Repository;

import genspark.pj.SecureAuthenticationSystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User,Integer> {
    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    Optional<User> findByUsername(String username);
}
