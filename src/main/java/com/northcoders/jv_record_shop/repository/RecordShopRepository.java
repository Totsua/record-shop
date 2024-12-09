package com.northcoders.jv_record_shop.repository;

import com.northcoders.jv_record_shop.model.Album;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordShopRepository extends CrudRepository<Album,Long> {
    @Override
    @CacheEvict(cacheNames = "albums", key = "#result.id")
    <S extends Album> S save (S s);

    @Override
    @Cacheable("albums")
    Optional<Album> findById(Long aLong);

}
