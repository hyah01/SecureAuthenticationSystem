package genspark.pj.SecureAuthenticationSystem.Services;

import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // encode password when storing into db
        user.setRoles("USER");
        return this.userDAO.save(user);
    }

    @Override
    public User addAdmin(User admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword())); // encode password when storing into db
        admin.setRoles("ADMIN");
        return this.userDAO.save(admin);
    }

    @Override
    public String deleteUserById(Integer id) {
        String curUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> curUser = userDAO.findByUsername(curUserName);
        if (curUser.isPresent()){
            User user = curUser.get();
            // Admin can also delete it too
            if (user.getRoles().equals("ADMIN") || user.getId() == id){
                this.userDAO.deleteById(id);
                return "User Deleted Successfully";
            } else {
                throw new RuntimeException("No Permission");
            }
        } else {
            throw new RuntimeException("User Not Found");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return this.userDAO.findAll();
    }

    @Override
    public User getSingleUser() {
        return this.userDAO.findByUsername(getLoggedInUserDetails().getUsername()).get();
    }

    public UserDetails getLoggedInUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }
}
