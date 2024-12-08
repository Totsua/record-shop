package com.northcoders.jv_record_shop.service;

import com.northcoders.jv_record_shop.exception.InvalidInputException;
import com.northcoders.jv_record_shop.exception.ItemNotFoundException;
import com.northcoders.jv_record_shop.model.Album;
import com.northcoders.jv_record_shop.repository.RecordShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecordShopServiceLayerImpl implements RecordShopServiceLayer {
    @Autowired
    RecordShopRepository recordShopRepository;

    @Override
    public List<Album> getAllAlbums() {
        List<Album> albumList = new ArrayList<>();
        recordShopRepository.findAll().forEach(albumList::add);
        return albumList;
    }

    @Override
    public Album getAlbumById(String id) {
        long longId;
        try{
            longId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new InvalidInputException(id+ " is not a valid id");
        }
        Optional<Album> potentialAlbum = recordShopRepository.findById(longId);

        if(potentialAlbum.isPresent()){
            return potentialAlbum.get();
        }
        else{
            throw new ItemNotFoundException("No album with id " + id);
        }
    }

    @Override
    public Album addAlbum(Album album) {
        return recordShopRepository.save(album);

    }

    @Override
    public Album updateAlbumDetails(String id, Album album) {
        return null;
    }

    @Override
    public void deleteAlbumById(String id) {
    }

}
