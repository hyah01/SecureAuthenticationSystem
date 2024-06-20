package genspark.pj.SecureAuthenticationSystem.Services;

import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import genspark.pj.SecureAuthenticationSystem.Repository.BlogDAO;
import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BlogServiceImplTest {
    @Autowired
    private BlogServiceImpl blogServiceImpl;


    @BeforeEach
    public void setUp() {

        // Create a mock authentication object
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        // Create a mock SecurityContext
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Set the SecurityContext
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void contextLoads(){
        assertThat(blogServiceImpl).isNotNull();
    }

    @Test
    public void addBlogTest() {

        Blog blog1 = new Blog("Valid Title1", "Valid Content", Arrays.asList("tag1", "tag2"));
        Blog blog2 = new Blog("Valid Title2", "Valid Content", Arrays.asList("tag1", "tag2"));

        Blog result1 = blogServiceImpl.addBlog(blog1);
        Blog result2 = blogServiceImpl.addBlog(blog2);

        assertEquals("Valid Title1", result1.getTitle());
        assertEquals("Valid Title2", result2.getTitle());

        assertEquals("testUser", result1.getAuthor());
    }


}