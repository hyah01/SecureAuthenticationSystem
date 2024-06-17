package genspark.pj.SecureAuthenticationSystem.Services;

import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Repository.BlogDAO;
import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService{
    @Autowired
    private BlogDAO blogDAO;
    @Autowired
    private UserDAO userDAO;
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
    public List<Blog> getMyUnPostedBlogs(){
        return this.blogDAO.getMyUnPostedBlogs(SecurityContextHolder.getContext().getAuthentication().getName());
    };

    @Override
    public Blog getById(long id) {
        Optional<Blog> b = this.blogDAO.findById(id);
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
    public Blog addBlog(Blog blogToAdd) {
        // only allow user to add post using title, content, and tags, everything else is ignored
        Blog blog = new Blog(blogToAdd.getTitle(), blogToAdd.getContent(),blogToAdd.getTags());
        return this.blogDAO.save(blog);
    }

    @Override
    public Blog updateBlog(Blog updateblog) {
        Blog blog = blogDAO.findById(updateblog.getId()).orElseThrow(() -> new RuntimeException("Blog not found"));
        // authenticate to make sure only user of that post can edit it
        String curUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> curUser = userDAO.findByUsername(curUserName);
        if (curUser.isPresent()){
            User user = curUser.get();
            if (user.getRoles().equals("ADMIN") || blog.getAuthor().equals(curUserName)){
                // Only allow user to edit title, content, tag and nothing else
                blog.setTitle(updateblog.getTitle());
                blog.setContent(updateblog.getContent());
                blog.setTags(updateblog.getTags());
                return this.blogDAO.save(blog);
            } else {
                throw new RuntimeException("No Permission");
            }
        } else {
            throw new RuntimeException("User Not Found");
        }
    }

    @Override
    public String deleteBlog(long id) {
        Blog blog = this.blogDAO.findById(id).orElseThrow(() -> new RuntimeException(" Blog not found"));
        // authenticate to make sure only user of that post can delete it
        String curUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> curUser = userDAO.findByUsername(curUserName);
        if (curUser.isPresent()){
            User user = curUser.get();
            // Admin can also delete it too
            if (user.getRoles().equals("ADMIN") || blog.getAuthor().equals(curUserName)){
                this.blogDAO.deleteById(id);
                return "Product Deleted Successfully";
            } else {
                throw new RuntimeException("No Permission");
            }
        } else {
            throw new RuntimeException("User Not Found");
        }

    }


    // allow for toggling of posts
    @Override
    public String publishBlog(long id) {
        this.blogDAO.togglePostedStatus(id);
        return "Toggled Published";
    }
}
