package com.northcoders.jv_record_shop.service;


import com.northcoders.jv_record_shop.exception.InvalidInputException;
import com.northcoders.jv_record_shop.exception.ItemNotFoundException;
import com.northcoders.jv_record_shop.model.Album;
import com.northcoders.jv_record_shop.model.Artist;
import com.northcoders.jv_record_shop.model.Genre;
import com.northcoders.jv_record_shop.repository.RecordShopRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class ServiceLayerImplTest {
    @Mock
    private RecordShopRepository mockRecordShopRepository;

    @InjectMocks
    private RecordShopServiceLayerImpl recordShopServiceLayer;

    //   List<Album> getAllAlbums();
    @Test
    @DisplayName("getAllAlbums returns a list of Albums")
    void getAllAlbums_test(){
        List<Album> albumList = new ArrayList<>();
        Artist testArtist = new Artist(1L, "THE Artist");
        Album testAlbum1 = new Album(1L, "testOne", testArtist, Genre.JAZZ.toString(),
                LocalDate.of(2024, 12, 5), 5, 10.50);
        Album testAlbum2 = new Album(2L, "testTwo", testArtist, Genre.POP.toString(),
                LocalDate.of(2024, 12, 5), 2, 12.12);
        Album testAlbum3 = new Album(3L, "testThree", testArtist, Genre.UNKNOWN.toString(),
                LocalDate.of(2024, 12, 5), 9, 5.0);

        albumList.add(testAlbum1);
        albumList.add(testAlbum2);
        albumList.add(testAlbum3);


        Mockito.when(mockRecordShopRepository.findAll()).thenReturn(albumList);

        List<Album> result = recordShopServiceLayer.getAllAlbums();

        Assertions.assertAll(
                () ->Assertions.assertEquals(3,result.size()),
                () ->Assertions.assertEquals(albumList,result)
        );

    }

    @Test
    @DisplayName("getAlbumById returns an Album when given a valid Id")
    void getAlbumById_ValidIdTest(){
        Artist testArtist = new Artist(1, "THE Artist");
        Album testAlbum = new Album(1, "testOne", testArtist, Genre.JAZZ.toString(),
                LocalDate.of(2024, 12, 5), 5, 10.50);

        String testId = "1";
        Mockito.when(mockRecordShopRepository.findById(1L)).thenReturn(Optional.of((testAlbum)));
        Album result = recordShopServiceLayer.getAlbumById(testId);


        Assertions.assertAll(
                () ->Assertions.assertEquals(1,result.getId()),
                () ->Assertions.assertEquals("testOne",result.getName())
        );
    }

    @Test
    @DisplayName("getAlbumById throws an InvalidInputException error for an invalid input")
    void getAlbumById_InvalidIdTest(){

        String testId = "B";
        Assertions.assertThrows(InvalidInputException.class, () -> recordShopServiceLayer.getAlbumById(testId));
    }

    @Test
    @DisplayName("getAlbumById throws an ItemNotFoundException for a valid id with no associated album in database")
    void getAlbumById_NoIdTest(){
        String testId = "1";
        Assertions.assertThrows(ItemNotFoundException.class, () -> recordShopServiceLayer.getAlbumById(testId));
    }



}