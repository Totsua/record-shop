package com.northcoders.jv_record_shop.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    String name;

    @JoinColumn
    @ManyToOne(cascade = CascadeType.ALL,optional = false)
    Artist artist;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Puts the enum in text form in the database
    Genre genre;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate releaseDate; // format YYYY-MM-dd

    @Column(nullable = false)
    int stock;

    @Column(nullable = false,precision = 2)
    double price;

}
