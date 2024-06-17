package genspark.pj.SecureAuthenticationSystem.Controller;

import genspark.pj.SecureAuthenticationSystem.Entity.Blog;
import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Repository.BlogDAO;
import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserDAO userDAO;

    @Autowired
    private BlogDAO blogDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/save")
    public ResponseEntity<Object> saveUser (@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword())); // encode password when storing into db
        user.setRoles("USER");
        User result = userDAO.save(user);
        if (result.getId() > 0){
            return ResponseEntity.ok("User Was Registered");
        }
        return ResponseEntity.status(404).body("Error: User Was Not Saved");
    }
    @PostMapping("/save/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> saveUserAdmin (@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword())); // encode password when storing into db
        user.setRoles("ADMIN");
        User result = userDAO.save(user);
        if (result.getId() > 0){
            return ResponseEntity.ok("Admin Was Registered");
        }
        return ResponseEntity.status(404).body("Error: User Was Not Saved");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer id){
        String curUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> curUser = userDAO.findByUsername(curUserName);
        if (curUser.isPresent()){
            User user = curUser.get();
            // Admin can also delete it too
            if (user.getRoles().equals("ADMIN") || user.getId() == id){
                this.userDAO.deleteById(id);
                return ResponseEntity.ok("User Was was Deleted");
            } else {
                return ResponseEntity.ok("No Permission");
            }
        } else {
            return ResponseEntity.ok("No User of that ID");
        }
    }

    // Only admin can see all users
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllUsers(){
        return ResponseEntity.ok(userDAO.findAll());
    }

    // All User/Admin can see their own account
    @GetMapping("/single")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> getMyDetails(){
        return ResponseEntity.ok(userDAO.findByUsername(getLoggedInUserDetails().getUsername()));
    }
    public UserDetails getLoggedInUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

}
