package com.example.ShopApp.services;

import com.example.ShopApp.dtos.UserDTO;
import com.example.ShopApp.exceptions.DataNotFoundException;
import com.example.ShopApp.models.Role;
import com.example.ShopApp.models.User;
import com.example.ShopApp.repositories.RoleRepository;
import com.example.ShopApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public User crateUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getFullName())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);
        //Kiểm tra nếu có accountId, không yêu cầu password
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            String passwrod = userDTO.getPassword();
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        return null;
    }
}
