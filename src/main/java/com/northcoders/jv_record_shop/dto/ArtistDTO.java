package com.northcoders.jv_record_shop.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistDTO {
    long id;

    @NotBlank(message = "An artist's name must not be empty")
    String name;
}
