package genspark.pj.SecureAuthenticationSystem;


import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Repository.BlogDAO;
import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import genspark.pj.SecureAuthenticationSystem.Services.BlogServiceImpl;
import genspark.pj.SecureAuthenticationSystem.Services.UserServiceImpl;
import org.junit.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
public class BlogServiceTest {
    @Mock
    BlogDAO blogDAO;
    @Mock
    UserDAO userDAO;
    @InjectMocks
    UserServiceImpl usi;
    @InjectMocks
    BlogServiceImpl bsi;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test_add_blog_with_valid_data() {
        BlogDAO blogDAO = mock(BlogDAO.class);
        UserDAO userDAO = mock(UserDAO.class);
        BlogServiceImpl blogService = new BlogServiceImpl();
        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);
        ReflectionTestUtils.setField(blogService, "userDAO", userDAO);

        Blog blogToAdd = new Blog("Valid Title", "Valid Content", Arrays.asList("tag1", "tag2"));
        Blog savedBlog = new Blog("Valid Title", "Valid Content", Arrays.asList("tag1", "tag2"));
        when(blogDAO.save(any(Blog.class))).thenReturn(savedBlog);

        Blog result = blogService.addBlog(blogToAdd);

        assertNotNull(result);
        assertEquals("Valid Title", result.getTitle());
        assertEquals("Valid Content", result.getContent());
        assertEquals(Arrays.asList("tag1", "tag2"), result.getTags());
    }

    @Test
    public void test_add_blog_with_empty_title() {
        BlogDAO blogDAO = mock(BlogDAO.class);
        UserDAO userDAO = mock(UserDAO.class);
        BlogServiceImpl blogService = new BlogServiceImpl();
        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);
        ReflectionTestUtils.setField(blogService, "userDAO", userDAO);

        Blog blogToAdd = new Blog("", "Content with empty title", Arrays.asList("tag1"));
        Blog savedBlog = new Blog("", "Content with empty title", Arrays.asList("tag1"));
        when(blogDAO.save(any(Blog.class))).thenReturn(savedBlog);

        Blog result = blogService.addBlog(blogToAdd);

        assertNotNull(result);
        assertEquals("", result.getTitle());
        assertEquals("Content with empty title", result.getContent());
        assertEquals(Arrays.asList("tag1"), result.getTags());
    }

}
