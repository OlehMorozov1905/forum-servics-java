package ait.forum.accounting.controller;

import ait.forum.accounting.dto.RolesDto;
import ait.forum.accounting.dto.UserDto;
import ait.forum.accounting.dto.UserEditDto;
import ait.forum.accounting.dto.UserRegisterDto;
import ait.forum.accounting.service.UserAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
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

    @Test
    void testRegister() throws Exception {
        UserDto userDto = new UserDto("testUser", "Test", "User", Set.of("USER"));
        when(userAccountService.register(any(UserRegisterDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"testUser\",\"password\":\"password\",\"firstName\":\"Test\",\"lastName\":\"User\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testUser"));
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

    @Test
    void testUpdateUser() throws Exception {
        UserDto userDto = new UserDto("testUser", "Updated", "User", Set.of("USER"));
        when(userAccountService.updateUser(anyString(), any(UserEditDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/account/user/testUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Updated\",\"lastName\":\"User\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Эмулируем пользователя с ролью ADMIN
    void testAddRole() throws Exception {
        RolesDto rolesDto = new RolesDto("testUser", Set.of("USER", "MODERATOR")); // Ожидаемый результат

        when(userAccountService.changeRolesList(anyString(), anyString(), anyBoolean())).thenReturn(rolesDto);

        mockMvc.perform(put("/account/user/testUser/role/MODERATOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles", hasItem("MODERATOR"))); // Проверяем, что роль MODERATOR присутствует
    }



    @Test
    public void testChangePassword() throws Exception {
        mockMvc.perform(put("/account/password")
                        .header("X-Password", "newPassword")
                        .principal(() -> "testuser"))
                .andExpect(status().isNoContent());
    }
}
