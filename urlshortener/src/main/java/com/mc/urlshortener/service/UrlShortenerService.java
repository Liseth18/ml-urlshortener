package com.mc.urlshortener.service;

import com.mc.urlshortener.dto.UrlShortenerRequestDto;
import com.mc.urlshortener.dto.UrlShortenerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;

public interface UrlShortenerService {

    Page<UrlShortenerDto> findAll(Pageable pageable);

    UrlShortenerDto save(UrlShortenerRequestDto url);

    UrlShortenerDto update(UrlShortenerDto url);

    UrlShortenerDto findById(BigInteger id);

    UrlShortenerDto findByUrlShort(String urlShort);

}
