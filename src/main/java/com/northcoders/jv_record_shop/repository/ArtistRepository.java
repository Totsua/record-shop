package com.northcoders.jv_record_shop.repository;

import com.northcoders.jv_record_shop.model.Artist;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArtistRepository extends CrudRepository<Artist,Long> {
    Optional<Artist> findByName(String name);
}
