package genspark.pj.SecureAuthenticationSystem.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

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
    private String author;
    private String content;
    private List<String> tags;
    private Instant timestamp;
    private Boolean posted;

    public Blog(String title, String author, List<String> tags, String category) {
        this.title = title;
        this.content = author;
        this.tags = tags;
        this.author = category;
        this.posted = false;
    }

}
