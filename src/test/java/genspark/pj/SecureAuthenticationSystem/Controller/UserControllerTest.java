package genspark.pj.SecureAuthenticationSystem.Controller;


import genspark.pj.SecureAuthenticationSystem.Entity.User;
import genspark.pj.SecureAuthenticationSystem.Services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    public void saveUser() throws Exception {
        User user = new User();
        user.setUsername("testUser1");
        user.setPassword("testPass1");
        user.setId(1);
        when(userServiceImpl.addUser(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/user/save")
                .contentType("application/json")
                .content("{\"username\":\"testUser1\",\"password\":\"testPass1\"}"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("User Was Registered", result.getResponse().getContentAsString()));

        verify(userServiceImpl, times(1)).addUser(any(User.class));
    }
    @Test
    public void saveUserNoValidId() throws Exception {
        User user = new User();
        user.setUsername("testUser1");
        user.setPassword("testPass1");
        when(userServiceImpl.addUser(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/user/save")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser1\",\"password\":\"testPass1\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertEquals("Error: User Was Not Saved", result.getResponse().getContentAsString()));

        verify(userServiceImpl, times(1)).addUser(any(User.class));
    }

    @Test
    public void saveUserWithEmptyHTTPBody() throws Exception {
        User user = new User();
        user.setUsername("testUser1");
        user.setPassword("testPass1");
        when(userServiceImpl.addUser(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/user/save")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertEquals("Error: User Was Not Saved", result.getResponse().getContentAsString()));

        verify(userServiceImpl, times(1)).addUser(any(User.class));
    }

    @Test
    public void saveUserAdmin() throws Exception{
        User admin = new User();
        admin.setUsername("testUser1");
        admin.setPassword("testPass1");
        admin.setId(1);
        when(userServiceImpl.addAdmin(any(User.class))).thenReturn(admin);
        mockMvc.perform(post("/user/save/admin")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser1\",\"password\":\"testPass1\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("Admin Was Registered", result.getResponse().getContentAsString()));


        verify(userServiceImpl, times(1)).addAdmin(any(User.class));
    }

    @Test
    public void deleteUser() throws Exception{
        User user = new User();
        user.setUsername("testUser1");
        user.setPassword("testPass1");
        user.setId(1);
        when(userServiceImpl.deleteUserById(any(Integer.class))).thenReturn("User Deleted Successfully");
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("User Deleted Successfully", result.getResponse().getContentAsString()));

        verify(userServiceImpl, times(1)).deleteUserById(any(Integer.class));
    }

    @Test
    public void deleteUserWithNoSuchID() throws Exception{
        when(userServiceImpl.deleteUserById(any(Integer.class))).thenReturn("User Not Found");
        mockMvc.perform(delete("/user/0"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("User Not Found", result.getResponse().getContentAsString()));

        verify(userServiceImpl, times(1)).deleteUserById(any(Integer.class));
    }

    @Test
    public void deleteUserWithNoAuthority() throws Exception{
        User user = new User();
        when(userServiceImpl.deleteUserById(any(Integer.class))).thenReturn("No Permission");
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("No Permission", result.getResponse().getContentAsString()));

        verify(userServiceImpl, times(1)).deleteUserById(any(Integer.class));
    }

    @Test
    public void getAllUsers() throws Exception{
        List<User> users = new ArrayList<>();
        users.add(new User("name1","pass1"));
        users.add(new User("name2","pass2"));
        when(userServiceImpl.getAllUsers()).thenReturn(users);
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("name1"))
                .andExpect(jsonPath("$[1].username").value("name2"));

        verify(userServiceImpl, times(1)).getAllUsers();
    }

    @Test
    public void getMyDetails() throws Exception{
        User user = new User("name","pass");
        when(userServiceImpl.getSingleUser()).thenReturn(user);
        mockMvc.perform(get("/user/single"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("name"));

        verify(userServiceImpl, times(1)).getSingleUser();
    }

}