package genspark.pj.SecureAuthenticationSystem.Controller;

import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import genspark.pj.SecureAuthenticationSystem.Repository.BlogDAO;
import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import genspark.pj.SecureAuthenticationSystem.Services.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/") // Home page end point
public class BlogController {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private BlogDAO blogDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BlogServiceImpl bsl;

    public String home() {
        return "Please Login";
    }


    // return all posted blogs
    @GetMapping("/blogs")
    public ResponseEntity<Object> getAllPostedBlogs(){
        return ResponseEntity.ok(bsl.getPostedBlogs());
    }

    // return all unposted blogs that only admin can see
    @GetMapping("/blogs/unposted")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllUnPostedBlogs(){
        return ResponseEntity.ok(bsl.getUnPostedBlogs());
    }

    // return current user's unposted blogs
    @GetMapping("blogs/myunpost")
    public ResponseEntity<Object> getMyUnPostedBlogs(){
        return ResponseEntity.ok(bsl.getMyUnPostedBlogs());
    }

    // only admin can see all the blogs using id
    @GetMapping("/blogs/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getBlogById(@PathVariable Long id){
        return ResponseEntity.ok(bsl.getById(id));
    }

    // allows everyone to look at posted blogs using title
    @GetMapping("/blogs/title")
    public ResponseEntity<Object> getBlogByTitle(@RequestParam String title){
        return ResponseEntity.ok(bsl.getByTitle(title));
    }

    // allows everyone to look at posted blogs using author name
    @GetMapping("/blogs/author")
    public ResponseEntity<Object> getBlogByAuthor(@RequestParam String author){
        return ResponseEntity.ok(bsl.getByAuthor(author));
    }

    // allows everyone to look at posted blogs using tags
    @GetMapping("/blogs/tag/{tag}")
    public ResponseEntity<Object> getBlogsByTag(@PathVariable String tag) {
        return ResponseEntity.ok(bsl.getByTag(tag));
    }

    // allows everyone to look at posted blogs in sorted order
    @GetMapping("/blogs/sorted")
    public ResponseEntity<Object> getBlogsBySorted() {
        return ResponseEntity.ok(bsl.getBySortedTitle());
    }

    // allows user to post a blog ( title, content, tags ), they will not be able to edit who posted or if it is posted
    @PostMapping("/blogs")
    public ResponseEntity<Object> addBlog(@RequestBody Blog blog){
        return ResponseEntity.ok(bsl.addBlog(blog));
    }

    // allows user to edit a blog ( title, content, tags ), they will only able to edit their own
    @PutMapping("/blogs")
    public ResponseEntity<Object> updateBlog(@RequestBody Blog blog){
        return ResponseEntity.ok(bsl.updateBlog(blog));
    }

    // allows user to delete their own blog or admin can delete all blogs
    @DeleteMapping("blogs/{blogid}")
    public ResponseEntity<Object> delBlog(@PathVariable Long blogid){
        return ResponseEntity.ok(bsl.deleteBlog(blogid));
    }

    // Admin can confirm if a plog can be posted
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/blogs/tg/{id}")
    public ResponseEntity<Object> togglePostedStatus(@PathVariable Long id) {
        return ResponseEntity.ok(bsl.publishBlog(id));
    }


}
