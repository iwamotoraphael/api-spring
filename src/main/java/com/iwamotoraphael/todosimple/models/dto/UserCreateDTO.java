package com.iwamotoraphael.todosimple.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDTO {
    
    @NotBlank
    @Size(min = 6, max=50)
    private String username;

    @NotBlank
    @Size(min = 8, max=255)
    private String password;

}
