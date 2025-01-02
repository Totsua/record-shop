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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false)
    long id;

    @Column(nullable = false)
    String name;

    @JoinColumn
    @ManyToOne(cascade = CascadeType.PERSIST,optional = false)
    Artist artist;

    @Column(nullable = false)
    String genre;

    @Column(nullable = false)
    LocalDate releaseDate;

    @Column(nullable = false)
    Integer stock;

    @Column(nullable = false,precision = 2)
    Double price;

    @Column
    String url;

}
