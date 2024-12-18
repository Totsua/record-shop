package com.northcoders.jv_record_shop.service;


import com.northcoders.jv_record_shop.dto.AlbumDTO;
import com.northcoders.jv_record_shop.dto.ArtistDTO;
import com.northcoders.jv_record_shop.exception.InvalidInputException;
import com.northcoders.jv_record_shop.exception.ItemNotFoundException;
import com.northcoders.jv_record_shop.model.Album;
import com.northcoders.jv_record_shop.model.Artist;
import com.northcoders.jv_record_shop.model.Genre;
import com.northcoders.jv_record_shop.repository.ArtistRepository;
import com.northcoders.jv_record_shop.repository.RecordShopRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class ServiceLayerImplTest {
    @Mock
    private RecordShopRepository mockRecordShopRepository;
    @Mock
    private ArtistRepository artistRepository;

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

        List<AlbumDTO> result = recordShopServiceLayer.getAllAlbums();

        Assertions.assertAll(
                () ->Assertions.assertEquals(3,result.size())
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
        AlbumDTO result = recordShopServiceLayer.getAlbumById(testId);


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


    // Set up an Answer object that will set the id of an Album after the repository call for adding an Album
    @Before("addAlbum_ValidTest")
    Answer<Album> setUpAnswer() {
        return invocationOnMock -> {
            Album album = invocationOnMock.getArgument(0, Album.class);
            album.setId(1);
            return album;
        };
    }

    @Test
    @DisplayName("addAlbum returns an added album with an Id")
    void addAlbum_test(){
        Artist testArtist = new Artist(1, "THE Artist");
        Album testAlbum = Album.builder()
                .id(0)
                .name("TestAlbumOne")
                .genre(Genre.RAP.toString())
                .stock(131)
                .artist(testArtist)
                .price(10.00)
                .releaseDate(LocalDate.of(2024, 12, 5))
                .build();

        ArtistDTO testArtistDTO = new ArtistDTO(1, "THE Artist");
        AlbumDTO testAlbumDTO = AlbumDTO.builder()
                .name("TestAlbumOne")
                .genre(Genre.RAP.toString())
                .stock(131)
                .artist(testArtistDTO)
                .price(10.00)
                .releaseDate(LocalDate.of(2024, 12, 5))
                .build();

        Mockito.when(artistRepository.existsById(testArtist.getId())).thenReturn(true);
        Mockito.when(mockRecordShopRepository.save(testAlbum)).thenAnswer(setUpAnswer());
        Mockito.when(artistRepository.findById(testArtist.getId())).thenReturn(Optional.of(testArtist));


        AlbumDTO result = recordShopServiceLayer.addAlbum(testAlbumDTO);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1,result.getId()),
                () -> Assertions.assertEquals("TestAlbumOne",result.getName())
        );
    }


    @Test
    @DisplayName("addAlbum assigns an artist with the same name if given an artist without id")
    void addAlbum_NoIdArtist(){
    }



    @Test
    @DisplayName("updateAlbumDetails returns the modified album with valid Id and inputs")
    void updateAlbumDetails_ValidIdInputsTest(){
        Artist testArtist = new Artist(1, "THE Artist");
        AlbumDTO testAlbumInfo = AlbumDTO.builder().name("TestAlbumOne").price(10.00).genre(Genre.RAP.toString()).build();
        String testId = "1";

        Album testAlbumInDb =  Album.builder().id(1)
                .name("THE ALBUM IS IN THE DB")
                .genre(Genre.JAZZ.toString())
                .stock(131)
                .artist(testArtist)
                .price(50.00)
                .releaseDate(LocalDate.of(2024, 12, 5))
                .build();

        Album testModifiedAlbum = Album.builder().id(1)
                .name("TestAlbumOne")
                .genre(Genre.RAP.toString())
                .stock(131)
                .artist(testArtist)
                .price(10.00)
                .releaseDate(LocalDate.of(2024, 12, 5))
                .build();

        Mockito.when(mockRecordShopRepository.existsById(Long.parseLong(testId))).thenReturn(true);
        Mockito.when(mockRecordShopRepository.findById(Long.parseLong(testId))).thenReturn(Optional.ofNullable(testAlbumInDb));
        Mockito.when(mockRecordShopRepository.save(testModifiedAlbum)).thenReturn(testModifiedAlbum);


        AlbumDTO result = recordShopServiceLayer.updateAlbumDetails(testId,testAlbumInfo);


        Assertions.assertAll(
                () -> Assertions.assertEquals("RAP",result.getGenre()),
                () -> Assertions.assertEquals("TestAlbumOne",result.getName()),
                () -> Assertions.assertEquals(10.00,result.getPrice())
        );
    }
    @Test
    @DisplayName("updateAlbumDetails throws ItemNotFoundException for a valid id with no associated album in database ")
    void updateAlbumDetails_ValidIdNoAlbumTest(){
       // Artist testArtist = new Artist(1, "THE Artist");
        AlbumDTO testAlbumInfo = AlbumDTO.builder().name("TestAlbumOne").price(10.00).build();
        String testId = "1";

        Assertions.assertThrows(ItemNotFoundException.class,() -> recordShopServiceLayer.updateAlbumDetails(testId,testAlbumInfo));
    }
    @Test
    @DisplayName("updateAlbumDetails throws InvalidInputException error for an invalid input")
    void updateAlbumDetails_InvalidInputTest(){
        //Artist testArtist = new Artist(1, "THE Artist");
        AlbumDTO testAlbumInfo = AlbumDTO.builder().name("  ").price(10.00).build();
        String testId = "L";

        Assertions.assertThrows(InvalidInputException.class,() -> recordShopServiceLayer.updateAlbumDetails(testId,testAlbumInfo));
    }

//    void deleteAlbumById(String id);
//    If the delete method is void then you should only test for when you expect the method to fail


    @Test
    @DisplayName("deleteAlbumById throws ItemNotFoundException if there's no album in database with that Id")
    void deleteAlbumById_ValidIdNoAlbumTest(){
        String testId = "1";

        Mockito.when(mockRecordShopRepository.existsById(Long.parseLong(testId))).thenReturn(false);

        Assertions.assertThrows(ItemNotFoundException.class, () -> recordShopServiceLayer.deleteAlbumById(testId));
    }

    @Test
    @DisplayName("deleteAlbumById throws InvalidInputException for an invalid id input")
    void deleteAlbumById_InvalidIdTest(){

        String testId = "B";

        Assertions.assertThrows(InvalidInputException.class, () -> recordShopServiceLayer.deleteAlbumById(testId));
    }

}