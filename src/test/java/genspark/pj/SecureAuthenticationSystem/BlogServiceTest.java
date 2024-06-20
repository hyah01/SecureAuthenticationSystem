package genspark.pj.SecureAuthenticationSystem;


import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Repository.BlogDAO;
import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import genspark.pj.SecureAuthenticationSystem.Services.BlogServiceImpl;
import genspark.pj.SecureAuthenticationSystem.Services.UserServiceImpl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ActiveProfiles;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
public class BlogServiceTest {
    @Autowired
    BlogDAO blogDAO;
    @Autowired
    UserDAO userDAO;
    UserServiceImpl usi;
    BlogServiceImpl bsi;

    @BeforeEach
    public void setUp(){
        usi = new UserServiceImpl();
        bsi = new BlogServiceImpl();
    }


    @Test
    public void test_add_blog_with_valid_data() {
        Blog blog1 = new Blog("Valid Title1", "Valid Content", Arrays.asList("tag1", "tag2"));
        Blog blog2 = new Blog("Valid Title2", "Valid Content", Arrays.asList("tag1", "tag2"));

        Blog result1 = bsi.addBlog(blog1);
        Blog result2 = bsi.addBlog(blog2);

        assertEquals("Valid Title1", result1.getTitle());
        assertEquals("Valid Title2", result2.getTitle());
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


//    @Test(expected = RuntimeException.class)
//    public void test_blog_not_found_for_given_id() {
//        BlogDAO blogDAO = mock(BlogDAO.class);
//        UserDAO userDAO = mock(UserDAO.class);
//        BlogServiceImpl blogService = new BlogServiceImpl();
//        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);
//        ReflectionTestUtils.setField(blogService, "userDAO", userDAO);
//
//        Blog updatedBlog = new Blog();
//        updatedBlog.setId(1L);
//
//        when(blogDAO.findById(1L)).thenReturn(Optional.empty());
//
//        blogService.updateBlog(updatedBlog);
//    }


    @Test
    public void test_delete_blog_when_user_is_author() {
        BlogDAO blogDAO = mock(BlogDAO.class);
        UserDAO userDAO = mock(UserDAO.class);
        BlogServiceImpl blogService = new BlogServiceImpl();
        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);
        ReflectionTestUtils.setField(blogService, "userDAO", userDAO);

        Blog blog = new Blog();
        blog.setId(1L);
        blog.setAuthor("author");

        User user = new User();
        user.setUsername("author");
        user.setRoles("USER");

        when(blogDAO.findById(1L)).thenReturn(Optional.of(blog));
        when(userDAO.findByUsername("author")).thenReturn(Optional.of(user));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("author", null));

        String result = blogService.deleteBlog(1L);

        verify(blogDAO).deleteById(1L);
        assertEquals("Blog Deleted Successfully", result);
    }

//    @Test(expected = RuntimeException.class)
//    public void test_delete_blog_that_does_not_exist() {
//        BlogDAO blogDAO = mock(BlogDAO.class);
//        UserDAO userDAO = mock(UserDAO.class);
//        BlogServiceImpl blogService = new BlogServiceImpl();
//        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);
//        ReflectionTestUtils.setField(blogService, "userDAO", userDAO);
//
//        when(blogDAO.findById(1L)).thenReturn(Optional.empty());
//
//        blogService.deleteBlog(1L);
//    }

    @Test
    public void test_returns_list_of_blogs_when_matching_title_exists() {
        BlogDAO blogDAO = mock(BlogDAO.class);
        BlogServiceImpl blogService = new BlogServiceImpl();
        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);

        Blog blog1 = new Blog();
        blog1.setTitle("Test Title");
        blog1.setContent("Test Content");
        List<Blog> expectedBlogs = new ArrayList<>();
        expectedBlogs.add(blog1);
        when(blogDAO.findByTitle("Test Title")).thenReturn(expectedBlogs);

        List<Blog> actualBlogs = blogService.getByTitle("Test Title");
        assertEquals(expectedBlogs, actualBlogs);
    }

    @Test
    public void test_title_contains_special_characters() {
        BlogDAO blogDAO = mock(BlogDAO.class);
        BlogServiceImpl blogService = new BlogServiceImpl();
        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);

        Blog blog1 = new Blog();
        blog1.setTitle("Special!@#Title");
        blog1.setContent("Test Content");
        List<Blog> expectedBlogs = new ArrayList<>();
        expectedBlogs.add(blog1);
        when(blogDAO.findByTitle("Special!@#Title")).thenReturn(expectedBlogs);

        List<Blog> actualBlogs = blogService.getByTitle("Special!@#Title");
        assertEquals(expectedBlogs, actualBlogs);
    }

    @Test
    public void test_returns_unposted_blogs_when_present() {
        BlogDAO blogDAO = mock(BlogDAO.class);
        BlogServiceImpl blogService = new BlogServiceImpl();
        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);

        Blog blog1 = new Blog("Title1", "Content1", new ArrayList<>());
        Blog blog2 = new Blog("Title2", "Content2", new ArrayList<>());
        List<Blog> expectedBlogs = new ArrayList<>();
        expectedBlogs.add(blog1);
        expectedBlogs.add(blog2);

        when(blogDAO.getUnPostedBlogs()).thenReturn(expectedBlogs);

        List<Blog> actualBlogs = blogService.getUnPostedBlogs();
        assertEquals(expectedBlogs, actualBlogs);
    }

    @Test
    public void test_handles_database_connection_lost() {
        BlogDAO blogDAO = mock(BlogDAO.class);
        BlogServiceImpl blogService = new BlogServiceImpl();
        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);

        when(blogDAO.getUnPostedBlogs()).thenThrow(new RuntimeException("Database connection lost"));

        try {
            blogService.getUnPostedBlogs();
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Database connection lost", e.getMessage());
        }
    }

    @Test
    public void test_returns_list_of_unposted_blogs_for_authenticated_user() {
        BlogDAO blogDAO = mock(BlogDAO.class);
        UserDAO userDAO = mock(UserDAO.class);
        BlogServiceImpl blogService = new BlogServiceImpl();
        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);
        ReflectionTestUtils.setField(blogService, "userDAO", userDAO);

        String username = "testUser";
        List<Blog> expectedBlogs = new ArrayList<>();
        Blog blog1 = new Blog("Title1", "Content1", new ArrayList<>());
        expectedBlogs.add(blog1);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(blogDAO.getMyUnPostedBlogs(username)).thenReturn(expectedBlogs);

        List<Blog> actualBlogs = blogService.getMyUnPostedBlogs();
        assertEquals(expectedBlogs, actualBlogs);
    }

    @Test
    public void test_handles_case_when_authenticated_user_is_null_or_not_found() {
        BlogDAO blogDAO = mock(BlogDAO.class);
        UserDAO userDAO = mock(UserDAO.class);
        BlogServiceImpl blogService = new BlogServiceImpl();
        ReflectionTestUtils.setField(blogService, "blogDAO", blogDAO);
        ReflectionTestUtils.setField(blogService, "userDAO", userDAO);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn(null);
        when(blogDAO.getMyUnPostedBlogs(null)).thenReturn(new ArrayList<>());
        List<Blog> actualBlogs = blogService.getMyUnPostedBlogs();

        assertTrue(actualBlogs.isEmpty());
    }

}
