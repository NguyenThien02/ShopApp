package com.example.ShopApp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor // Hàm khởi tạo không có tham số

public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "Password cannot be black")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private long facebookAccountId;

    @JsonProperty("google_account_id")
    private long googleAccountId;

    @JsonProperty("role_id")
    @NotNull(message = "Role Id is required")
    private long roleId;
}
