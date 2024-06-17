package genspark.pj.SecureAuthenticationSystem;

import genspark.pj.SecureAuthenticationSystem.Controller.UserController;
import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
//@ActiveProfiles("test")
class UserServiceTest {
	@Autowired
	UserDAO userDAO;

	UserController uc;

	@Test
	void contextLoads() {
	}

//	@Test
//	void TestCreateUser(){
//		User user = new User(1,"user","user","USER");
//		uc.getAllUsers();
//	}

}
