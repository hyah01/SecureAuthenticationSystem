package genspark.pj.SecureAuthenticationSystem;

import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
	@Autowired
	UserDAO userDAO;

	@Test
	void contextLoads() {
	}

	@Test
	void TestCreateUser(){
		
	}

}
