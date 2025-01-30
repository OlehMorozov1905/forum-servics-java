package ait.forum.accounting.service;

import ait.forum.accounting.dao.UserAccountRepository;
import ait.forum.accounting.dto.UserDto;
import ait.forum.accounting.dto.UserEditDto;
import ait.forum.accounting.dto.UserRegisterDto;
import ait.forum.accounting.dto.exceptions.UserExistsException;
import ait.forum.accounting.dto.exceptions.UserNotFoundException;
import ait.forum.accounting.model.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserAccount userAccount;
    private UserDto userDto;
    private UserRegisterDto userRegisterDto;

    @BeforeEach
    void setUp() {
        userAccount = new UserAccount("testUser", "password", "John", "Doe");
        userDto = new UserDto("testUser", "John", "Doe", Set.of("USER"));
        userRegisterDto = new UserRegisterDto("testUser", "password", "John", "Doe");
    }

    @Test
    void testRegister_Success() {
        when(userAccountRepository.existsById(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(modelMapper.map(any(UserRegisterDto.class), eq(UserAccount.class))).thenReturn(userAccount);
        when(modelMapper.map(any(UserAccount.class), eq(UserDto.class))).thenReturn(userDto);
        when(userAccountRepository.save(any())).thenReturn(userAccount);

        UserDto result = userAccountService.register(userRegisterDto);

        assertNotNull(result);
        assertEquals("testUser", result.getLogin());
        verify(userAccountRepository, times(1)).save(any());
    }

    @Test
    void testRegister_UserAlreadyExists() {
        when(userAccountRepository.existsById(any())).thenReturn(true);
        assertThrows(UserExistsException.class, () -> userAccountService.register(userRegisterDto));
    }

    @Test
    void testGetUser_Success() {
        when(userAccountRepository.findById(any())).thenReturn(Optional.of(userAccount));
        when(modelMapper.map(any(UserAccount.class), eq(UserDto.class))).thenReturn(userDto);

        UserDto result = userAccountService.getUser("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getLogin());
    }

    @Test
    void testGetUser_NotFound() {
        when(userAccountRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userAccountService.getUser("testUser"));
    }

    @Test
    void testRemoveUser_Success() {
        when(userAccountRepository.findById(any())).thenReturn(Optional.of(userAccount));
        when(modelMapper.map(any(UserAccount.class), eq(UserDto.class))).thenReturn(userDto);

        UserDto result = userAccountService.removeUser("testUser");

        assertNotNull(result);
        verify(userAccountRepository, times(1)).delete(any());
    }

    @Test
    void testUpdateUser_Success() {
        UserEditDto userEditDto = new UserEditDto("Jane", "Doe");
        when(userAccountRepository.findById(any())).thenReturn(Optional.of(userAccount));
        when(modelMapper.map(any(UserAccount.class), eq(UserDto.class))).thenReturn(userDto);

        UserDto result = userAccountService.updateUser("testUser", userEditDto);

        assertNotNull(result);
        verify(userAccountRepository, times(1)).save(any());
    }

    @Test
    void testChangePassword_Success() {
        when(userAccountRepository.findById(any())).thenReturn(Optional.of(userAccount));
        when(passwordEncoder.encode(any())).thenReturn("newEncodedPassword");

        userAccountService.changePassword("testUser", "newPassword");

        verify(userAccountRepository, times(1)).save(any());
    }
}

