package com.iwamotoraphael.todosimple.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDTO {
    
    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 8, max=255)
    private String password;

}
