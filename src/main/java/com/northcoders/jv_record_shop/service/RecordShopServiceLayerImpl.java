package com.northcoders.jv_record_shop.service;

import com.northcoders.jv_record_shop.model.Album;
import com.northcoders.jv_record_shop.repository.RecordShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordShopServiceLayerImpl implements RecordShopServiceLayer {
    @Autowired
    RecordShopRepository recordShopRepository;

    @Override
    public List<Album> getAllAlbums() {
        return null;
    }

    @Override
    public Album getAlbumById(String id) {
        return null;
    }

    @Override
    public Album addAlbum(Album album) {
        return null;
    }

    @Override
    public Album updateAlbumDetails(String id, Album album) {
        return null;
    }

    @Override
    public boolean deleteAlbumById(String id) {
        return false;
    }

}
