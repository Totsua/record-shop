package com.northcoders.jv_record_shop.service;

import com.northcoders.jv_record_shop.dto.AlbumDTO;
import com.northcoders.jv_record_shop.dto.ArtistDTO;
import com.northcoders.jv_record_shop.exception.InvalidInputException;
import com.northcoders.jv_record_shop.exception.ItemNotFoundException;
import com.northcoders.jv_record_shop.model.Album;
import com.northcoders.jv_record_shop.model.Artist;
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
    @Autowired
    AlbumCoverAPIServiceLayer albumCoverAPIServiceLayer;


    @Override
    public List<AlbumDTO> getAllAlbums() {
        List<Album> albumList = new ArrayList<>();
        recordShopRepository.findAll().forEach(albumList::add);
        return albumList.stream().map(this::mapAlbumToDTO).toList();
    }

    @Override
    public AlbumDTO getAlbumById(String id) {
        long longId;
        try{
            longId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new InvalidInputException(id+ " is not a valid id");
        }
        Optional<Album> potentialAlbum = recordShopRepository.findById(longId);
        if(potentialAlbum.isPresent()){
            return mapAlbumToDTO(potentialAlbum.get());
        }
        else{
            throw new ItemNotFoundException("No album with id " + id);
        }
    }

    @Override
    public AlbumDTO addAlbum(AlbumDTO albumInput) {
        // Album and Artist id's must be set to 0 to ensure that a detached entity error won't occur
        albumInput.setId(0);
        albumInput.getArtist().setId(0);
        Album album = mapDTOToAlbum(albumInput);

        Optional<Artist> artist = artistRepository.findByName(album.getArtist().getName());
        artist.ifPresent(album::setArtist);

        String albumCoverURL = albumCoverAPIServiceLayer.findAlbumCoverURL(album.getName(), album.getArtist().getName());
        album.setUrl(albumCoverURL);

        Album albumDTO = recordShopRepository.save(album);
        return mapAlbumToDTO(albumDTO);
    }

    @Override
    public AlbumDTO updateAlbumDetails(String id, AlbumDTO albumUpdateInfo) {
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
                albumInDb.setArtist(mapDTOToArtist(albumUpdateInfo.getArtist()));
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


            // todo:
            //  Be able to update the url of an Album
            return mapAlbumToDTO(recordShopRepository.save(albumInDb));


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

    private AlbumDTO mapAlbumToDTO(Album album){

        return AlbumDTO.builder()
                .id(album.getId())
                .name(album.getName())
                .artist(mapArtistToDTO(album.getArtist()))
                .genre(album.getGenre())
                .releaseDate(album.getReleaseDate())
                .price(album.getPrice())
                .stock(album.getStock())
                .url(album.getUrl())
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
                .url(albumDTO.getUrl())
                .build();
    }

    private Artist mapDTOToArtist(ArtistDTO artistDTO){
        return Artist.builder()
                .id(artistDTO.getId())
                .name(artistDTO.getName())
                .build();
    }



}
