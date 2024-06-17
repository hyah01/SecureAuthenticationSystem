package genspark.pj.SecureAuthenticationSystem.Services;

import genspark.pj.SecureAuthenticationSystem.Entity.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> getAllBlogs();
    List<Blog> getPostedBlogs();
    List<Blog> getUnPostedBlogs();
    List<Blog> getMyUnPostedBlogs();
    Blog getById(long id);
    List<Blog> getByTitle(String title);
    List<Blog> getByAuthor(String author);
    List<Blog> getByTag(String tag);
    List<Blog> getBySortedTitle();
    Blog addBlog(Blog blog);
    Blog updateBlog(Blog blog);
    String deleteBlog(long id);
    String publishBlog(long id);
}
