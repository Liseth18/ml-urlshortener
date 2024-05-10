package com.mc.urlshortener.service.impl;

import com.google.common.hash.Hashing;
import com.mc.urlshortener.dto.UrlShortenerRequestDto;
import com.mc.urlshortener.dto.UrlShortenerDto;
import com.mc.urlshortener.exception.NotFoundException;
import com.mc.urlshortener.model.UrlShortener;
import com.mc.urlshortener.repository.UrlShortenerRepository;
import com.mc.urlshortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UrlShortenerServiceImpl implements UrlShortenerService {

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    @Override
    public Page<UrlShortenerDto> findAll(Pageable pageable){
        Page<UrlShortener> urlAll = urlShortenerRepository.findAll(pageable);
        return urlAll.map(this::mapToUrlResponse);
    }

    @Override
    public UrlShortenerDto save(UrlShortenerRequestDto urlRequest){
        UrlShortener urlShortener = UrlShortener.builder()
                .urlOrigin(urlRequest.getUrlOrigin())
                .urlShort(encodeUrl(urlRequest.getUrlOrigin()))
                .active(true)
                .creationDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        return mapToUrlResponse(urlShortenerRepository.save(urlShortener));
    }

    @CacheEvict(value ="Url", allEntries = true)
    @CachePut(value="Url", key="#urlShortenerDto.urlShort")
    @Override
    public UrlShortenerDto update(UrlShortenerDto urlShortenerDto){

        log.info("Update URL urlShort: {}", urlShortenerDto.getUrlShort());
        UrlShortener urlShortener = UrlShortener.builder()
                .id(urlShortenerDto.getId())
                .urlOrigin(urlShortenerDto.getUrlOrigin())
                .urlShort(urlShortenerDto.getUrlShort())
                .active(urlShortenerDto.getActive())
                .creationDate(urlShortenerDto.getCreationDate())
                .updatedDate(LocalDateTime.now())
                .build();

        return mapToUrlResponse(urlShortenerRepository.save(urlShortener));
    }

    @Cacheable(value="Url", key="#id")
    @Override
    public UrlShortenerDto findById(BigInteger id){
        log.info("URL - findById: {}", id);
        return mapToUrlResponse(urlShortenerRepository.findById(id));
    }

    @Cacheable(value="Url", key="#urlShort")
    @Override
    public UrlShortenerDto findByUrlShort(String urlShort){
        log.info("URL - findByUrlShort: {}", urlShort);
        UrlShortener urlShortener = urlShortenerRepository.findByUrlShort(urlShort);
        if(urlShortener == null){
            throw new NotFoundException("Url not found: "+ urlShort);
        }
        return mapToUrlResponse(urlShortener);
    }

    public String encodeUrl(String url){
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32_fixed()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return  encodedUrl;
    }

    public UrlShortenerDto mapToUrlResponse(UrlShortener urlShortener){
        return UrlShortenerDto.builder()
                .id(urlShortener.getId())
                .urlOrigin(urlShortener.getUrlOrigin())
                .urlShort(urlShortener.getUrlShort())
                .active(urlShortener.getActive())
                .creationDate(urlShortener.getCreationDate())
                .updatedDate(urlShortener.getUpdatedDate())
                .build();
    }

}
