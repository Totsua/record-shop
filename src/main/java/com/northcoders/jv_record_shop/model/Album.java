package com.northcoders.jv_record_shop.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @GeneratedValue
    @Column(updatable = false,nullable = false)
    long id;

    @Column(nullable = false)
    @NotBlank(message = "name cannot be blank")
    String name;

    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL,optional = false)
    Artist artist;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Puts the enum in text form in the database
    Genre genre;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
            @NotBlank(message = "releaseDate must contain an entry in format \"dd-MM-yyyy\"")
    LocalDate releaseDate;

    @Column(nullable = false)
    @NotBlank(message = "stock cannot be blank")
    @Range(min=0,message = "stock cannot be negative")
    int stock;

    @Column(nullable = false,precision = 2)
    @Range(min=0,message = "price cannot be negative, we aren't paying people to take the album")
    double price;

}
