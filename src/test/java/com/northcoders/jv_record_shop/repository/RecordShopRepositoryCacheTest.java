package com.northcoders.jv_record_shop.repository;

import com.northcoders.jv_record_shop.model.Album;
import com.northcoders.jv_record_shop.model.Artist;
import com.northcoders.jv_record_shop.model.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
public class RecordShopRepositoryCacheTest {

    private Long bobsId;
    @Autowired
    RecordShopRepository repository;

    @BeforeEach
    void setup(){
        Album bobsAlbum = Album.builder()
                .name("Bob's album")
                .artist(Artist.builder().name("Bobby").build())
                .genre(Genre.HIPHOP.toString())
                .price(5.00)
                .stock(10)
                .releaseDate(LocalDate.now())
                .build();

        Album bob = repository.save(bobsAlbum);
        bobsId = bob.getId();
    }

    // Check to see if logs are repeated
    @Test
    void saveLoadMultipleTimes(){
        Optional<Album> bob = null;
        for (int i = 0; i < 10; i++) {
            bob = repository.findById(bobsId);
        }

        repository.save(bob.get());

        for (int i = 0; i < 10; i++) {
            bob = repository.findById(bobsId);
        }
    }

    @Test
    void getAlbumUpdated(){

        Optional<Album> bob;
        bob = repository.findById(bobsId);

        Album gotBob = bob.get();

        gotBob.setName("Changed man");

        repository.save(gotBob);

        bob = repository.findById(bobsId);

        Assertions.assertEquals(gotBob.getName(),bob.get().getName());

    }


}
