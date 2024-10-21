package com.example.ShopApp.services;

import com.example.ShopApp.dtos.UserDTO;
import com.example.ShopApp.exceptions.DataNotFoundException;
import com.example.ShopApp.exceptions.InvalidParamException;
import com.example.ShopApp.models.User;

public interface IUserService {
    User crateUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password, Long roleId) throws Exception;// Trả về một Token key

    void deleteUser(Long id);
    User getUserById(long id) throws DataNotFoundException;
}
