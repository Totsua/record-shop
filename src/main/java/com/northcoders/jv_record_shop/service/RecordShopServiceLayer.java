package com.northcoders.jv_record_shop.service;

import com.northcoders.jv_record_shop.dto.AlbumDTO;


import java.util.List;

public interface RecordShopServiceLayer {
    List<AlbumDTO> getAllAlbums();
    AlbumDTO getAlbumById(String id);
    AlbumDTO addAlbum(AlbumDTO album);
    AlbumDTO updateAlbumDetails(String id,AlbumDTO album);
    void deleteAlbumById(String id);
}
