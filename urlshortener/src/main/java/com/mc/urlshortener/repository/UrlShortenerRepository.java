package com.mc.urlshortener.repository;

import com.mc.urlshortener.model.UrlShortener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Repository
@Transactional
public interface UrlShortenerRepository extends MongoRepository<UrlShortener, String> {

    Page<UrlShortener> findAll(Pageable pageable);

    UrlShortener findById(BigInteger id);

    UrlShortener findByUrlShort(String urlShort);
}
