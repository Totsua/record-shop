package com.northcoders.jv_record_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.jv_record_shop.dto.AlbumDTO;
import com.northcoders.jv_record_shop.dto.ArtistDTO;
import com.northcoders.jv_record_shop.exception.APIExceptionHandler;
import com.northcoders.jv_record_shop.exception.InvalidInputException;
import com.northcoders.jv_record_shop.exception.ItemNotFoundException;
import com.northcoders.jv_record_shop.model.Album;
import com.northcoders.jv_record_shop.model.Artist;
import com.northcoders.jv_record_shop.model.Genre;
import com.northcoders.jv_record_shop.service.RecordShopServiceLayerImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
class RecordShopControllerTests {

    @Mock
    private RecordShopServiceLayerImpl mockRecordShopServiceImpl;

    @InjectMocks
    private RecordShopController recordShopController;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(recordShopController)
                .setControllerAdvice(new APIExceptionHandler())
                .build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("GET all the albums in the database")
    void getAllAlbums_Test() throws Exception {
        ArtistDTO testArtist = new ArtistDTO(1L, "THE Artist");

        AlbumDTO testAlbum1 = new AlbumDTO(1L, "testOne", testArtist, Genre.JAZZ.toString(),
                LocalDate.of(2024, 12, 5), 5, 10.50);
        AlbumDTO testAlbum2 = new AlbumDTO(2L, "testTwo", testArtist, Genre.POP.toString(),
                LocalDate.of(2024, 12, 5), 2, 12.12);
        AlbumDTO testAlbum3 = new AlbumDTO(3L, "testThree", testArtist, Genre.UNKNOWN.toString(),
                LocalDate.of(2024, 12, 5), 9, 5.0);

        List<AlbumDTO> albumList = List.of(testAlbum1, testAlbum2, testAlbum3);

        Mockito.when(mockRecordShopServiceImpl.getAllAlbums()).thenReturn(albumList);


        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/recordshop/albums"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("testOne"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stock").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].genre").value("POP"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].artist.name").value("THE Artist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].price").value("5.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].releaseDate").value("2024-12-05"));
    }

    @Test
    @DisplayName("Get Album by the id given a valid id")
    void getAlbumById_ValidIdTest() throws Exception {
        ArtistDTO testArtist = new ArtistDTO(1, "THE Artist");
        AlbumDTO testAlbum = new AlbumDTO(1, "testOne", testArtist, Genre.JAZZ.toString(),
                LocalDate.of(2000, 10, 10), 5, 10.50);

        Mockito.when(mockRecordShopServiceImpl.getAlbumById("1")).thenReturn(testAlbum);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/recordshop/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("JAZZ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2000-10-10"));
    }


    @Test
    @DisplayName("getAlbumById throws an error 400 for an invalid Id")
    void getAlbumById_InvalidIdInputTest() throws Exception {

        Mockito.when(mockRecordShopServiceImpl.getAlbumById("B"))
                .thenThrow(new InvalidInputException("B is not a valid Id"));

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/recordshop/albums/B"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("getAlbumById throws an error 404 for a valid Id with no Album associated with that id")
    void getAlbumById_ValidIdNoAlbumInputTest() throws Exception {

        Mockito.when(mockRecordShopServiceImpl.getAlbumById("1"))
                .thenThrow(new ItemNotFoundException("There is no Album with id 1"));

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/recordshop/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // Set up an Answer object that will set the id of an Album after the Service call for adding an Album
    @Before("addAlbum_ValidTest")
    Answer<AlbumDTO> setUpAnswer() {
        return invocationOnMock -> {
            AlbumDTO album = invocationOnMock.getArgument(0, AlbumDTO.class);
            album.setId(1);
            return album;
        };
    }

    @Test
    @DisplayName("AddAlbum returns the created album with a valid input")
    void addAlbum_ValidTest() throws Exception {

        // Assigning Artist and Album objects
        ArtistDTO testArtist = new ArtistDTO(1, "THE Artist");
        AlbumDTO testAlbum = AlbumDTO.builder()
                .name("TestAlbumOne")
                .genre(Genre.RAP.toString())
                .stock(131)
                .artist(testArtist)
                .price(10.00)
                .releaseDate(LocalDate.of(2024, 12, 5))
                .build();

        // Mapping the object to a JSON
        String testJSON = mapper.writeValueAsString(testAlbum);

        // Returning the Album with an ID from the Mock Service
        Mockito.when(mockRecordShopServiceImpl.addAlbum(testAlbum)).thenAnswer(setUpAnswer());


        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/recordshop/albums/create")
                                .contentType(MediaType.APPLICATION_JSON).content(testJSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("RAP"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2024-12-05"));
    }

    @Test
    @DisplayName("AddAlbum returns error 400 for invalid inputs")
    void addAlbum_InvalidInputsTest() throws Exception {

        String testJSON = "{\"name\":\"\",\"artist\":{\"id\":1},\"genre\":\"E\"," +
                "\"releaseDate\":\"\",\"stock\":\"-3\",\"price\":\"-5\"}";


        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/recordshop/albums/create")
                                .contentType(MediaType.APPLICATION_JSON).content(testJSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name cannot be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("must be any of Genre {ROCK,HIPHOP,RAP,JAZZ,POP,UNKNOWN}"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock").value("stock cannot be negative"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("price cannot be negative because we aren't paying people to take the album"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("releaseDate must contain an entry in format 'dd-MM-yyyy'"))
                .andExpect(MockMvcResultMatchers.jsonPath("$['artist.name']").value("An artist's name must not be empty"));
    }


    @Test
    @DisplayName("AddAlbum returns the created album with a generated id from the database with a valid album input that comes with a different id")
    void addAlbum_WithAnIdTest() throws Exception {

        // Assigning Artist and Album objects
        ArtistDTO testArtist = new ArtistDTO(1, "THE Artist");
        AlbumDTO testAlbum = AlbumDTO.builder()
                .id(10)
                .name("TestAlbumOne")
                .genre(Genre.UNKNOWN.toString())
                .stock(4)
                .artist(testArtist)
                .price(10.00)
                .releaseDate(LocalDate.of(2020, 12, 6))
                .build();

        // Mapping the object to a JSON
        String testJSON = mapper.writeValueAsString(testAlbum);

        // Returning the Album with an ID from the Mock Service
        // Assuming this is the first Album in the database, should return with id of 1
        Mockito.when(mockRecordShopServiceImpl.addAlbum(testAlbum)).thenAnswer(setUpAnswer());


        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/recordshop/albums/create")
                                .contentType(MediaType.APPLICATION_JSON).content(testJSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("UNKNOWN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2020-12-06"));
    }


    @Test
    @DisplayName("updateAlbumDetails returns the updated Album when given valid input and id")
    void updateAlbumDetails_ValidInputs() throws Exception {

        AlbumDTO testAlbumUpdated = AlbumDTO.builder().id(3).stock(3).price(5.0).build();

        String testJSON = mapper.writeValueAsString(testAlbumUpdated);

        ArtistDTO testArtist = new ArtistDTO(1, "THE Artist");
        AlbumDTO testAlbum = AlbumDTO.builder().id(3)
                .name("TestAlbumOne")
                .artist(testArtist)
                .genre(Genre.HIPHOP.toString())
                .releaseDate(LocalDate.of(2014, 12, 12))
                .stock(3)
                .price(5.0).build();

        Mockito.when(mockRecordShopServiceImpl.updateAlbumDetails("3", testAlbumUpdated)).thenReturn(testAlbum);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.patch("/api/v1/recordshop/albums/3")
                                .contentType(MediaType.APPLICATION_JSON).content(testJSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("5.0"));

    }

    @Test
    @DisplayName("updateAlbumDetails returns error 404 for an valid id with no Album associated with the id")
    void updateAlbumDetails_ValidIdNoAlbum() throws Exception {

        AlbumDTO testAlbumUpdated = AlbumDTO.builder().id(3).stock(3).price(5.0).build();

        String testJSON = mapper.writeValueAsString(testAlbumUpdated);


        Mockito.when(mockRecordShopServiceImpl.updateAlbumDetails("3", testAlbumUpdated))
                .thenThrow(ItemNotFoundException.class);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.patch("/api/v1/recordshop/albums/3")
                                .contentType(MediaType.APPLICATION_JSON).content(testJSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
    @Test
    @DisplayName("updateAlbumDetails returns error 400 for valid id with invalid inputs")
    void updateAlbumDetails_ValidIdInvalidInputTest() throws Exception {

        String testJSON = "{\"name\":\"\"}";
        AlbumDTO testAlbum = AlbumDTO.builder().name("").build();


        Mockito.when(mockRecordShopServiceImpl.updateAlbumDetails("1",testAlbum))
                .thenThrow(new InvalidInputException("name cannot be blank"));

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.patch("/api/v1/recordshop/albums/1")
                                .contentType(MediaType.APPLICATION_JSON).content(testJSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    @DisplayName("updateAlbumDetails returns error 400 for invalid Id")
    void updateAlbumDetails_InvalidIdTest() throws Exception {

        String testJSON = "{\"id\":\"B\"}";

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.patch("/api/v1/recordshop/albums/B"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



    @Test
    @DisplayName("deleteAlbumByID returns Status 204 for a valid Id")
    void deleteAlbumByID_ValidId() throws Exception{

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.delete("/api/v1/recordshop/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }
    @Test
    @DisplayName("deleteAlbumByID returns error 404 for a valid Id with no Album associated with that id")
    void deleteAlbumByID_ValidIdNoAlbum() throws Exception{

        Mockito.doThrow(new ItemNotFoundException("There is no album with id 1")).when(mockRecordShopServiceImpl).deleteAlbumById("1");

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.delete("/api/v1/recordshop/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("deleteAlbumByID returns error 400 for an invalid Id")
    void deleteAlbumByID_InvalidId() throws Exception{

        Mockito.doThrow(new InvalidInputException("B is not a valid id")).when(mockRecordShopServiceImpl).deleteAlbumById("B");

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.delete("/api/v1/recordshop/albums/B"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}