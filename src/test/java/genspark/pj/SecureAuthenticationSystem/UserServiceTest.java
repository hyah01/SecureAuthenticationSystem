package genspark.pj.SecureAuthenticationSystem;


import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Repository.UserDAO;
import genspark.pj.SecureAuthenticationSystem.Services.UserServiceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
	@Mock
	UserDAO userDAO;
	@InjectMocks
	UserServiceImpl usi;

	@Test
    public void contextLoads() {
	}

	@Test
	public void test_add_user_successfully() {
		UserDAO userDAO = mock(UserDAO.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		UserServiceImpl userService = new UserServiceImpl();
		ReflectionTestUtils.setField(userService, "userDAO", userDAO);
		ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);

		User user = new User("testuser", "password123");
		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
		when(userDAO.save(any(User.class))).thenReturn(user);

		User result = userService.addUser(user);

		verify(userDAO).save(user);
		assertEquals("encodedPassword123", result.getPassword());
		assertEquals("USER", result.getRoles());
	}

	@Test
	public void test_add_user_with_existing_username() {
		UserDAO userDAO = mock(UserDAO.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		UserServiceImpl userService = new UserServiceImpl();
		ReflectionTestUtils.setField(userService, "userDAO", userDAO);
		ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);

		User user = new User("existinguser", "password123");
		when(userDAO.save(any(User.class))).thenThrow(new RuntimeException("Username already exists"));

		try {
			userService.addUser(user);
			fail("Expected RuntimeException to be thrown");
		} catch (RuntimeException e) {
			assertEquals("Username already exists", e.getMessage());
		}
	}

	@Test
	public void test_add_admin_with_valid_details() {
		UserDAO userDAO = mock(UserDAO.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		UserServiceImpl userService = new UserServiceImpl();
		ReflectionTestUtils.setField(userService, "userDAO", userDAO);
		ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);

		User admin = new User("adminUser", "adminPass");
		when(passwordEncoder.encode("adminPass")).thenReturn("encodedAdminPass");
		when(userDAO.save(admin)).thenReturn(admin);

		User result = userService.addAdmin(admin);

		verify(passwordEncoder).encode("adminPass");
		verify(userDAO).save(admin);
		assertEquals("encodedAdminPass", result.getPassword());
		assertEquals("ADMIN", result.getRoles());
	}

	@Test
	public void test_delete_user_when_admin() {
		UserDAO userDAO = mock(UserDAO.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		UserServiceImpl userService = new UserServiceImpl();
		ReflectionTestUtils.setField(userService, "userDAO", userDAO);
		ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);

		User adminUser = new User();
		adminUser.setId(1);
		adminUser.setUsername("admin");
		adminUser.setRoles("ADMIN");

		Authentication authentication = mock(Authentication.class);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		when(authentication.getName()).thenReturn("admin");
		when(userDAO.findByUsername("admin")).thenReturn(Optional.of(adminUser));

		String result = userService.deleteUserById(2);
		verify(userDAO).deleteById(2);
		assertEquals("User Deleted Successfully", result);
	}

//	@Test(expected = RuntimeException.class)
//	public void test_throw_exception_when_user_not_found() {
//		UserDAO userDAO = mock(UserDAO.class);
//		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
//		UserServiceImpl userService = new UserServiceImpl();
//		ReflectionTestUtils.setField(userService, "userDAO", userDAO);
//		ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
//
//		Authentication authentication = mock(Authentication.class);
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		when(authentication.getName()).thenReturn("nonexistentUser");
//		when(userDAO.findByUsername("nonexistentUser")).thenReturn(Optional.empty());
//
//		userService.deleteUserById(2);
//	}

	@Test
	public void test_returns_correct_user_when_valid_user_logged_in() {
		UserDAO userDAO = mock(UserDAO.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		UserServiceImpl userService = new UserServiceImpl();
		ReflectionTestUtils.setField(userService, "userDAO", userDAO);
		ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);

		UserDetails userDetails = mock(UserDetails.class);
		when(userDetails.getUsername()).thenReturn("testUser");
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		User expectedUser = new User("testUser", "password");
		when(userDAO.findByUsername("testUser")).thenReturn(Optional.of(expectedUser));

		User actualUser = userService.getSingleUser();
		assertEquals(expectedUser, actualUser);
	}
	@Test
	public void test_handles_scenario_where_no_user_is_logged_in() {
		UserDAO userDAO = mock(UserDAO.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		UserServiceImpl userService = new UserServiceImpl();
		ReflectionTestUtils.setField(userService, "userDAO", userDAO);
		ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);

		SecurityContextHolder.getContext().setAuthentication(null);

		try {
			userService.getSingleUser();
			fail("Expected an exception to be thrown");
		} catch (NullPointerException e) {
			assertEquals("Cannot invoke \"org.springframework.security.core.userdetails.UserDetails.getUsername()\" because the return value of \"genspark.pj.SecureAuthenticationSystem.Services.UserServiceImpl.getLoggedInUserDetails()\" is null", e.getMessage());
		}
	}

}
