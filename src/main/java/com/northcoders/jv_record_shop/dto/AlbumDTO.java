package com.northcoders.jv_record_shop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.northcoders.jv_record_shop.customvalidator.GenreValidator;
import com.northcoders.jv_record_shop.model.Genre;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDTO {

    long id;

    @NotBlank(message = "name cannot be blank")
    @NotNull
    String name;

    @NotNull(message = "An artist must be included")
    @Valid
    ArtistDTO artist;

    @GenreValidator(enumClass = Genre.class)
    String genre;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "releaseDate must contain an entry in format 'dd-MM-yyyy'")
    LocalDate releaseDate;

    @NotNull(message = "stock cannot be empty")
    @Range(min=0,message = "stock cannot be negative")
    Integer stock;

    @NotNull(message = "price must not be empty")
    @Range(min=0,message = "price cannot be negative because we aren't paying people to take the album")
    Double price;

    String url;
}
