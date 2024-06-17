package genspark.pj.SecureAuthenticationSystem.Entity;

import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private List<String> tags;
    private Instant timestamp;
    private Boolean posted = false;
//    @JoinColumn(name = "author_id")
//    private User author;
    private String author;

    public Blog(String title, String content, List<String> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }


    @PrePersist
    protected void onCreate(){ // on create, it sets timestamp to now and author to be current user
        this.timestamp = Instant.now();
        //this. author = userDAO.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        this.author = SecurityContextHolder.getContext().getAuthentication().getName();
        if (this.posted == null){
            this.posted = false;
        }
    }

}
