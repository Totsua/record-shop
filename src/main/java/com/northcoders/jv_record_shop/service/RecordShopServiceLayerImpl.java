package com.northcoders.jv_record_shop.service;

import com.northcoders.jv_record_shop.exception.InvalidInputException;
import com.northcoders.jv_record_shop.exception.ItemNotFoundException;
import com.northcoders.jv_record_shop.model.Album;
import com.northcoders.jv_record_shop.repository.ArtistRepository;
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
    @Autowired
    ArtistRepository artistRepository;

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
        album.setId(0); // Set to 0 in case the user inputs an id

        if(artistRepository.existsById(album.getArtist().getId())){
            album.setArtist(artistRepository.findById(album.getArtist().getId()).get());
        }else{
            album.getArtist().setId(0); // Set to 0 in case the user inputs an artist id that's not in the database
        }

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


    private AlbumDTO mapAlbumToDTO(Album album){
        System.out.println(album);
        return AlbumDTO.builder()
                .id(album.getId())
                .name(album.getName())
                .artist(mapArtistToDTO(album.getArtist()))
                .genre(album.getGenre())
                .releaseDate(album.getReleaseDate())
                .price(album.getPrice())
                .stock(album.getStock())
                .build();
    }

    private ArtistDTO mapArtistToDTO(Artist artist){
        return ArtistDTO.builder()
                .id(artist.getId())
                .name(artist.getName())
                .build();
    }

    private Album mapDTOToAlbum(AlbumDTO albumDTO){
        return Album.builder()
                .id(albumDTO.getId())
                .name(albumDTO.getName())
                .artist(mapDTOToArtist(albumDTO.getArtist()))
                .genre(albumDTO.getGenre())
                .releaseDate(albumDTO.getReleaseDate())
                .price(albumDTO.getPrice())
                .stock(albumDTO.getStock())
                .build();
    }

    private Artist mapDTOToArtist(ArtistDTO artistDTO){
        return Artist.builder()
                .id(artistDTO.getId())
                .name(artistDTO.getName())
                .build();
    }



}
