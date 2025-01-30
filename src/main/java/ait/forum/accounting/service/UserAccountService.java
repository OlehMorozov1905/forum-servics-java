package ait.forum.accounting.service;

import ait.forum.accounting.dto.RolesDto;
import ait.forum.accounting.dto.UserDto;
import ait.forum.accounting.dto.UserEditDto;
import ait.forum.accounting.dto.UserRegisterDto;

public interface UserAccountService {
    UserDto register(UserRegisterDto userRegisterDto);

    UserDto getUser(String login);

    UserDto removeUser(String login);

    UserDto updateUser(String login, UserEditDto userEditDto);

    RolesDto changeRolesList(String login, String role, boolean isAddRole);

    void changePassword(String login, String newPassword);
}
