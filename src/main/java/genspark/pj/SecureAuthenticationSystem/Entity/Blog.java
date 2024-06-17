package genspark.pj.SecureAuthenticationSystem.Entity;

import jakarta.persistence.*;
import lombok.*;
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
    private String author;

    @PrePersist
    protected void onCreate(){
        this.timestamp = Instant.now();
        this. author = SecurityContextHolder.getContext().getAuthentication().getName();
        if (this.posted == null){
            this.posted = false;
        }
    }

}
