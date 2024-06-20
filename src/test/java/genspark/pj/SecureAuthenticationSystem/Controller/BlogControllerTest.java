package genspark.pj.SecureAuthenticationSystem.Controller;


import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Services.BlogServiceImpl;
import genspark.pj.SecureAuthenticationSystem.Services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BlogControllerTest {

    @Mock
    private BlogServiceImpl blogService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private BlogController blogController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(blogController).build();
    }
    @Test
    void getAllBlogs() throws Exception{
        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog("title1","content1", Arrays.asList("tag1", "tag2")));
        blogs.add(new Blog("title2","content2", Arrays.asList("tag1", "tag2")));
        when(blogService.getAllBlogs()).thenReturn(blogs);
        mockMvc.perform(get("/blogs/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[1].title").value("title2"));

        verify(blogService, times(1)).getAllBlogs();
    }

    @Test
    void getAllPostedBlogs() throws Exception{
        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog("title1","content1", Arrays.asList("tag1", "tag2")));
        blogs.add(new Blog("title2","content2", Arrays.asList("tag1", "tag2")));
        when(blogService.getPostedBlogs()).thenReturn(blogs);
        mockMvc.perform(get("/blogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[1].title").value("title2"));

        verify(blogService, times(1)).getPostedBlogs();
    }

    @Test
    void getAllUnPostedBlogs() throws Exception{
        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog("title1","content1", Arrays.asList("tag1", "tag2")));
        blogs.add(new Blog("title2","content2", Arrays.asList("tag1", "tag2")));
        when(blogService.getUnPostedBlogs()).thenReturn(blogs);
        mockMvc.perform(get("/blogs/unposted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[1].title").value("title2"));

        verify(blogService, times(1)).getUnPostedBlogs();
    }

    @Test
    void getMyUnPostedBlogs() throws Exception{
        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog("title1","content1", Arrays.asList("tag1", "tag2")));
        blogs.add(new Blog("title2","content2", Arrays.asList("tag1", "tag2")));
        when(blogService.getMyUnPostedBlogs()).thenReturn(blogs);
        mockMvc.perform(get("/blogs/myunpost"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[1].title").value("title2"));

        verify(blogService, times(1)).getMyUnPostedBlogs();
    }

    @Test
    void getBlogById() throws Exception{
        Blog blog = new Blog("title1","content1", Arrays.asList("tag1", "tag2"));
        blog.setId(1L);
        when(blogService.getById(any(Long.class))).thenReturn(blog);
        mockMvc.perform(get("/blogs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title1"))
                .andExpect(jsonPath("$.id").value(1L));

        verify(blogService, times(1)).getById(1L);
    }

    @Test
    void getBlogByTitle() throws Exception{
        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog("title1","content1", Arrays.asList("tag1", "tag2")));
        when(blogService.getByTitle(any(String.class))).thenReturn(blogs);
        mockMvc.perform(get("/blogs/title?title=title1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"));

        verify(blogService, times(1)).getByTitle("title1");
    }

    @Test
    void getBlogByAuthor() throws Exception{
        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog("title1","content1", Arrays.asList("tag1", "tag2")));
        when(blogService.getByAuthor(any(String.class))).thenReturn(blogs);
        mockMvc.perform(get("/blogs/author?author="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"));

        verify(blogService, times(1)).getByAuthor("");
    }

    @Test
    void getBlogsByTag() throws Exception{
        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog("title1","content1", Arrays.asList("tag1", "tag2")));
        when(blogService.getByTag(any(String.class))).thenReturn(blogs);
        mockMvc.perform(get("/blogs/tag/tag2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"));

        verify(blogService, times(1)).getByTag("tag2");
    }

    @Test
    void getBlogsBySorted() throws Exception{
        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog("title1","content1", Arrays.asList("tag1", "tag2")));
        blogs.add(new Blog("title2","content1", Arrays.asList("tag1", "tag2")));
        when(blogService.getBySortedTitle()).thenReturn(blogs);
        mockMvc.perform(get("/blogs/sorted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[1].title").value("title2"));

        verify(blogService, times(1)).getBySortedTitle();
    }

    @Test
    void addBlog() throws Exception{
        Blog blog = new Blog("title","content", Arrays.asList("tag1", "tag2"));
        blog.setId(1L);
        when(blogService.addBlog(any(Blog.class))).thenReturn(blog);
        mockMvc.perform(post("/blogs")
                    .contentType("application/json")
                    .content("{\"title\":\"title\",\"content\":\"content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.id").value(1L));

        verify(blogService, times(1)).addBlog(any(Blog.class));
    }

    @Test
    void updateBlog() throws Exception{
        Blog blog = new Blog("title","content", Arrays.asList("tag1", "tag2"));
        blog.setId(1L);
        when(blogService.updateBlog(any(Blog.class))).thenReturn(blog);
        mockMvc.perform(put("/blogs")
                        .contentType("application/json")
                        .content("{\"title\":\"title\",\"content\":\"content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.id").value(1L));

        verify(blogService, times(1)).updateBlog(any(Blog.class));
    }

    @Test
    void delBlog() throws Exception{
        when(blogService.deleteBlog(any(Long.class))).thenReturn("Blog Deleted Successfully");
        mockMvc.perform(delete("/blogs/1"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("Blog Deleted Successfully", result.getResponse().getContentAsString()));

        verify(blogService, times(1)).deleteBlog(any(Long.class));
    }

    @Test
    void togglePostedStatus() {
    }

}