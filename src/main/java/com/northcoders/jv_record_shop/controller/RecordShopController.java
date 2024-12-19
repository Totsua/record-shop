package com.northcoders.jv_record_shop.controller;

import com.northcoders.jv_record_shop.dto.AlbumDTO;
import com.northcoders.jv_record_shop.model.Album;
import com.northcoders.jv_record_shop.service.RecordShopServiceLayer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recordshop")
public class RecordShopController {

    @Autowired
    RecordShopServiceLayer recordShopService;

    @GetMapping("/albums")
    public ResponseEntity<List<AlbumDTO>> getAllAlbums(){
        return new ResponseEntity<>(recordShopService.getAllAlbums(), HttpStatus.OK);
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable String id){
        return new ResponseEntity<>(recordShopService.getAlbumById(id),HttpStatus.OK);
    }

    @PostMapping("/albums/create")
    public ResponseEntity<AlbumDTO> addAlbum(@RequestBody @Valid AlbumDTO album){
        return new ResponseEntity<>(recordShopService.addAlbum(album),HttpStatus.CREATED);
    }

    @PatchMapping("albums/{id}")
    public ResponseEntity<AlbumDTO> updateAlbumDetails(@PathVariable String id, @RequestBody  AlbumDTO album){
        return new ResponseEntity<>(recordShopService.updateAlbumDetails(id,album),HttpStatus.OK);
    }

    @DeleteMapping("albums/{id}")
    public ResponseEntity<Album> deleteAlbumByID(@PathVariable String id){
        recordShopService.deleteAlbumById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}