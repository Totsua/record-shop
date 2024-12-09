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
    public Album updateAlbumDetails(String id, Album albumUpdateInfo) {
        long longId;

        try{
            longId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new InvalidInputException(id + " is not a valid id");
        }



        if(recordShopRepository.existsById(longId)){
            // Mockito doesn't seem to like getting the Album via ".get()" on the Optional Album in one line
            // Therefore it's done in two
            Optional<Album> optionalAlbumInDb = recordShopRepository.findById(longId);
            Album albumInDb = optionalAlbumInDb.get();
            if(albumUpdateInfo.getName() != null){
                albumInDb.setName(albumUpdateInfo.getName());
            }
            if(albumUpdateInfo.getArtist() != null){
                albumInDb.setArtist(albumUpdateInfo.getArtist());
            }
            if(albumUpdateInfo.getGenre() != null){
                albumInDb.setGenre(albumUpdateInfo.getGenre());
            }
            if(albumUpdateInfo.getReleaseDate() != null){
                albumInDb.setReleaseDate(albumUpdateInfo.getReleaseDate());
            }
            if(albumUpdateInfo.getStock() != null){
                albumInDb.setStock(albumUpdateInfo.getStock());
            }
            if(albumUpdateInfo.getPrice() != null){
                albumInDb.setPrice(albumUpdateInfo.getPrice());
            }
            return recordShopRepository.save(albumInDb);


        }else{
            throw new ItemNotFoundException("There is no album with id: " + id);
        }


    }

    @Override
    public void deleteAlbumById(String id) {
        long longId;
        try{
            longId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new InvalidInputException(id + "is not a valid id");
        }

        if(recordShopRepository.existsById(longId)){
            recordShopRepository.deleteById(longId);
        }else{
            throw new ItemNotFoundException("There is no album with id: " + id);
        }


    }

}
