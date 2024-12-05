package com.northcoders.jv_record_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.jv_record_shop.model.Album;
import com.northcoders.jv_record_shop.model.Artist;
import com.northcoders.jv_record_shop.model.Genre;
import com.northcoders.jv_record_shop.service.RecordShopServiceLayerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
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
        mockMvcController = MockMvcBuilders.standaloneSetup(recordShopController).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("GET all the albums in the database")
    void getAllAlbums_Test() throws Exception {
        Artist testArtist = new Artist(1L, "THE Artist");

        Album testAlbum1 = new Album(1L, "testOne", testArtist, Genre.JAZZ, LocalDate.now(),5,10.50);
        Album testAlbum2 = new Album(2L, "testTwo", testArtist, Genre.POP, LocalDate.now(),2,12.12);
        Album testAlbum3 = new Album(3L, "testThree", testArtist, Genre.UNKNOWN, LocalDate.now(),9,5.0);

        List<Album> albumList = List.of(testAlbum1, testAlbum2, testAlbum3);

        Mockito.when(mockRecordShopServiceImpl.getAllAlbums()).thenReturn(albumList);


        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/recordshop"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("testOne"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stock").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].genre").value("POP"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].artist.name").value("THE Artist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].price").value("5.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].releaseDate").value("05-12-2024"));
    }

    @Test
    @DisplayName("Get Album by the id given a valid id")
    void getAlbumById_ValidIdTest() throws Exception{
        Artist testArtist = new Artist(1, "THE Artist");
        Album testAlbum = new Album(1, "testOne", testArtist, Genre.JAZZ, LocalDate.now(),5,10.50);

        Mockito.when(mockRecordShopServiceImpl.getAlbumById("1")).thenReturn(testAlbum);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/recordshop/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("JAZZ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("05-12-2024"));
    }
}