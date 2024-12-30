package com.northcoders.jv_record_shop.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    long id;

    @Column (nullable = false)
    String name;


    /* todo:
        Make the artists keep track of all their albums
        @OneToMany(mappedBy = "artist")
        List<Album> albums;
    */

}
