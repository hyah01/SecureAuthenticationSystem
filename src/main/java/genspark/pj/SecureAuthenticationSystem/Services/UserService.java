package genspark.pj.SecureAuthenticationSystem.Services;

import genspark.pj.SecureAuthenticationSystem.Entity.User;

import java.util.List;

public interface UserService {
    User addUser(User user);
    User addAdmin(User admin);
    String deleteUserById(Integer id);
    List<User> getAllUsers();
    User getSingleUser();

}
