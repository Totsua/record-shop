package com.northcoders.jv_record_shop.model;


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
    LocalDate releaseDate; // format YYYY-MM-dd

}
