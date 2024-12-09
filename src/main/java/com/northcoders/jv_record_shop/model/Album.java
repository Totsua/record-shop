package com.northcoders.jv_record_shop.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.northcoders.jv_record_shop.customvalidator.GenreValidator;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false)
    long id;

    @Column(nullable = false)
    @NotBlank(message = "name cannot be blank")
    @NotNull
    String name;

    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL,optional = false)
    @NotNull(message = "An artist must be included")
    @Valid
    Artist artist;

    @Column(nullable = false)
    @GenreValidator(enumClass = Genre.class)
    String genre;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "releaseDate must contain an entry in format \"dd-MM-yyyy\"")
    LocalDate releaseDate;

    @Column(nullable = false)
    //@NotBlank(message = "stock cannot be blank")
    @NotNull(message = "stock cannot be empty")
    @Range(min=0,message = "stock cannot be negative")
    Integer stock;

    @Column(nullable = false,precision = 2)
    @NotNull(message = "price must not be empty")
    @Range(min=0,message = "price cannot be negative because we aren't paying people to take the album")
    Double price;

}
