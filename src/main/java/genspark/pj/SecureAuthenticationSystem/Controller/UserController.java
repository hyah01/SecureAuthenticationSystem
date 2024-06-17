package genspark.pj.SecureAuthenticationSystem.Controller;

import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Repository.BlogDAO;
import genspark.pj.SecureAuthenticationSystem.Services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserServiceImpl usi;

    @Autowired
    private BlogDAO blogDAO;


    @PostMapping("/user/save")
    public ResponseEntity<Object> saveUser (@RequestBody User user){
        User result = usi.addUser(user);
        if (result.getId() > 0){
            return ResponseEntity.ok("User Was Registered");
        }
        return ResponseEntity.status(404).body("Error: User Was Not Saved");
    }
    @PostMapping("/user/save/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> saveUserAdmin (@RequestBody User user){
        User result = usi.addAdmin(user);
        if (result.getId() > 0){
            return ResponseEntity.ok("Admin Was Registered");
        }
        return ResponseEntity.status(404).body("Error: User Was Not Saved");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer id){
        return ResponseEntity.ok(usi.deleteUserById(id));
    }

    // Only admin can see all users
    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllUsers(){
        return ResponseEntity.ok(usi.getAllUsers());
    }

    // All User/Admin can see their own account
    @GetMapping("/user/single")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> getMyDetails(){
        return ResponseEntity.ok(usi.getSingleUser());
    }
    public UserDetails getLoggedInUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

}
