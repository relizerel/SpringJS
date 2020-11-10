package com.spring.service.interfaces;

import com.spring.model.DTOUser;
import com.spring.model.User;

import java.util.List;

public interface DTOService {
    DTOUser userConvertToDTOUser(User user);
    DTOUser getAuthUserInfo(User user);
    User dtoUserConvertToUser(DTOUser dtoUser);
    List<DTOUser> userListConvertToDTO(List<User> users);

}
