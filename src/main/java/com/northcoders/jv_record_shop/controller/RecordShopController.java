package com.northcoders.jv_record_shop.controller;

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

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums(){
        return new ResponseEntity<>(recordShopService.getAllAlbums(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable String id){
        return new ResponseEntity<>(recordShopService.getAlbumById(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Album> addAlbum(@RequestBody @Valid Album album){
        return new ResponseEntity<>(recordShopService.addAlbum(album),HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Album> updateAlbumDetails(@PathVariable String id, @RequestBody  Album album){
        return new ResponseEntity<>(recordShopService.updateAlbumDetails(id,album),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteAlbumByID(@PathVariable String id){
        boolean isValid = recordShopService.deleteAlbumById(id);
        return isValid ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}