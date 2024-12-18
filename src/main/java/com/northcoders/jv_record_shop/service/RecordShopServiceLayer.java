package com.northcoders.jv_record_shop.service;

import com.northcoders.jv_record_shop.dto.AlbumDTO;
import com.northcoders.jv_record_shop.model.Album;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RecordShopServiceLayer {
    List<AlbumDTO> getAllAlbums();
    AlbumDTO getAlbumById(String id);
    AlbumDTO addAlbum(AlbumDTO album);
    AlbumDTO updateAlbumDetails(String id,AlbumDTO album);
    void deleteAlbumById(String id);
}
