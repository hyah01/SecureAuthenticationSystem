package genspark.pj.SecureAuthenticationSystem.Controller;

import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Repository.BlogDAO;
import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import genspark.pj.SecureAuthenticationSystem.Services.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
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

    @PostMapping("/user/save")
    public ResponseEntity<Object> saveUser (@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User result = userDAO.save(user);
        if (result.getId() > 0){
            return ResponseEntity.ok("User Was Registered");
        }
        return ResponseEntity.status(404).body("Error: User Was Not Saved");
    }

    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllUsers(){
        return ResponseEntity.ok(userDAO.findAll());
    }

    @GetMapping("/users/single")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> getMyDetails(){
        return ResponseEntity.ok(userDAO.findByUsername(getLoggedInUserDetails().getUsername()));
    }

    @GetMapping("/blogs/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllBlogs(){
        return ResponseEntity.ok(bsl.getAllBlogs());
    }

    @GetMapping("/blogs")
    public ResponseEntity<Object> getAllPostedBlogs(){
        return ResponseEntity.ok(bsl.getPostedBlogs());
    }
    @GetMapping("/blogs/unposted")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllUnPostedBlogs(){
        return ResponseEntity.ok(bsl.getUnPostedBlogs());
    }

    @GetMapping("/blogs/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getBlogById(@PathVariable Long id){
        return ResponseEntity.ok(bsl.getById(id));
    }

    @GetMapping("/blogs/title")
    public ResponseEntity<Object> getBlogByTitle(@RequestParam String title){
        return ResponseEntity.ok(bsl.getByTitle(title));
    }
    @GetMapping("/blogs/author")
    public ResponseEntity<Object> getBlogByAuthor(@RequestParam String author){
        return ResponseEntity.ok(bsl.getByAuthor(author));
    }

    @GetMapping("/blogs/tag/{tag}")
    public ResponseEntity<Object> getBlogsByTag(@PathVariable String tag) {
        return ResponseEntity.ok(bsl.getByTag(tag));
    }

    @GetMapping("/blogs/sorted")
    public ResponseEntity<Object> getBlogsBySorted() {
        return ResponseEntity.ok(bsl.getBySortedTitle());
    }

    @PostMapping("/blogs")
    public ResponseEntity<Object> addBlog(@RequestBody Blog blog){
        return ResponseEntity.ok(bsl.addBlog(blog));
    }

    @PutMapping("/blogs")
    public ResponseEntity<Object> updateBlog(@RequestBody Blog blog){
        return ResponseEntity.ok(bsl.updateBlog(blog));
    }

    @DeleteMapping("blogs/{blogid}")
    public ResponseEntity<Object> delBlog(@PathVariable Long blogid){
        return ResponseEntity.ok(bsl.deleteBlog(blogid));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/blogs/tg/{id}")
    public ResponseEntity<Object> togglePostedStatus(@PathVariable Long id) {
        return ResponseEntity.ok(bsl.publishBlog(id));
    }


    public UserDetails getLoggedInUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

}
