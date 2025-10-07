package com.vivek.backend.Management.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionRequestDto {

    // @NotBlank
    private String options;

    private boolean correct;
}