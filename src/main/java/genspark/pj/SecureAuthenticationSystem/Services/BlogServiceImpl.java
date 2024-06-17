package genspark.pj.SecureAuthenticationSystem.Services;

import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import genspark.pj.SecureAuthenticationSystem.Repository.BlogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService{
    @Autowired
    private BlogDAO blogDAO;
    @Override
    public List<Blog> getAllBlogs() {
        return this.blogDAO.findAll();
    }

    @Override
    public List<Blog> getPostedBlogs() {
        return this.blogDAO.getPostedBlogs();
    }

    @Override
    public List<Blog> getUnPostedBlogs() {
        return this.blogDAO.getUnPostedBlogs();
    }

    @Override
    public Blog getById(long blogid) {
        Optional<Blog> b = this.blogDAO.findById(blogid);
        Blog blog = null;
        if (b.isPresent()) {
            blog = b.get();
        }
        return blog;
    }

    @Override
    public List<Blog> getByTitle(String title) {
        return this.blogDAO.findByTitle(title);
    }

    @Override
    public List<Blog> getByAuthor(String author) {
        return this.blogDAO.findByAuthor(author);
    }

    @Override
    public List<Blog> getByTag(String tag) {
        List<Blog> allBLogs = this.blogDAO.findAll();
        List<Blog> matchTags = new ArrayList<>();

        for( Blog blog: allBLogs){
            for(String blogTag : blog.getTags()){
                if(blogTag.toLowerCase().contains(tag.toLowerCase())){
                    matchTags.add(blog);
                    break;
                }
            }
        }
        return matchTags;
    }

    @Override
    public List<Blog> getBySortedTitle() {
        return this.blogDAO.findBlogSortedTitle();
    }

    @Override
    public Blog addBlog(Blog blog) {
        return this.blogDAO.save(blog);
    }

    @Override
    public Blog updateBlog(Blog blog) {
        return this.blogDAO.save(blog);
    }

    @Override
    public String deleteBlog(long id) {
        this.blogDAO.deleteById(id);
        return "Product Deleted Successfully";
    }

    @Override
    public String publishBlog(long id) {
        this.blogDAO.togglePostedStatus(id);
        return "Toggled Published";
    }
}
