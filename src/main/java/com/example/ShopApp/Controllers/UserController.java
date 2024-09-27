package com.example.ShopApp.Controllers;

import com.example.ShopApp.dtos.*;
import com.example.ShopApp.models.User;
import com.example.ShopApp.services.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;
    //Tạo mới một User
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result) {
        try {
            if(result.hasErrors()){
                List<String> errorMessager = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessager);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Password does not match");
            }
            User user = userService.crateUser(userDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
//       //Kiểm tra thông tin đăng nhập và sinh token
        try{
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            return ResponseEntity.ok(token);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("delete user with id: " + id);
    }
}
