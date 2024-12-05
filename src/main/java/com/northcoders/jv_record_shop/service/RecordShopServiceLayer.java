package com.northcoders.jv_record_shop.service;

import com.northcoders.jv_record_shop.model.Album;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RecordShopServiceLayer {
    List<Album> getAllAlbums();
    Album getAlbumById(String id);
    Album addAlbum(Album album);
    Album updateAlbumDetails(String id,Album album);
    boolean deleteAlbumById(String id);
}
