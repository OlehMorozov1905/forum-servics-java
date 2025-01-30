package ait.cohort34.accounting.controller;

import ait.cohort34.accounting.dto.*;
import ait.cohort34.accounting.service.UserAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserAccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserAccountService userAccountService;

    @InjectMocks
    private UserAccountController userAccountController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userAccountController).build();
    }

    //    этот тест не проходит
    @Test
    public void testRegister() throws Exception {
        UserRegisterDto registerDto = new UserRegisterDto("testuser", "password", "John", "Doe");
        UserDto userDto = new UserDto("testuser", "John", "Doe", Set.of("USER"));
        when(userAccountService.register(registerDto)).thenReturn(userDto);

        mockMvc.perform(post("/account/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    public void testLogin() throws Exception {
        UserDto userDto = new UserDto("testuser", "John", "Doe", Set.of("USER"));
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");
        when(userAccountService.getUser("testuser")).thenReturn(userDto);

        mockMvc.perform(post("/account/login")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    public void testGetUser() throws Exception {
        UserDto userDto = new UserDto("testuser", "John", "Doe", Set.of("USER"));
        when(userAccountService.getUser("testuser")).thenReturn(userDto);

        mockMvc.perform(get("/account/user/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    public void testRemoveUser() throws Exception {
        UserDto userDto = new UserDto("testuser", "John", "Doe", Set.of("USER"));
        when(userAccountService.removeUser("testuser")).thenReturn(userDto);

        mockMvc.perform(delete("/account/user/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    //    этот тест не проходит
    @Test
    public void testUpdateUser() throws Exception {
        UserEditDto editDto = new UserEditDto("Jane", "Doe");
        UserDto userDto = new UserDto("testuser", "Jane", "Doe", Set.of("USER"));
        when(userAccountService.updateUser("testuser", editDto)).thenReturn(userDto);

        mockMvc.perform(put("/account/user/testuser")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(editDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    public void testAddRole() throws Exception {
        RolesDto rolesDto = new RolesDto("testuser", Set.of("USER", "MODERATOR"));
        when(userAccountService.changeRolesList("testuser", "MODERATOR", true)).thenReturn(rolesDto);

        mockMvc.perform(put("/account/user/testuser/role/MODERATOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles[1]").value("MODERATOR"));
    }

//    этот тест не проходит
    @Test
    public void testDeleteRole() throws Exception {
        RolesDto rolesDto = new RolesDto("testuser", Set.of("USER"));
        when(userAccountService.changeRolesList("testuser", "MODERATOR", false)).thenReturn(rolesDto);

        mockMvc.perform(delete("/account/user/testuser/role/MODERATOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles").doesNotExist());
    }

    @Test
    public void testChangePassword() throws Exception {
        mockMvc.perform(put("/account/password")
                        .header("X-Password", "newPassword")
                        .principal(() -> "testuser"))
                .andExpect(status().isNoContent());
    }
}
