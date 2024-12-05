package com.northcoders.jv_record_shop.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column
    String name;

    @Column
    Artist artist;

    @Column
    Genre genre;


}
