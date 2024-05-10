package com.mc.urlshortener.service.impl;

import com.mc.urlshortener.dto.UrlShortenerDto;
import com.mc.urlshortener.dto.UrlShortenerRequestDto;
import com.mc.urlshortener.model.UrlShortener;
import com.mc.urlshortener.repository.UrlShortenerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigInteger;
import java.util.List;

import static com.mc.urlshortener.UrlShortenerData.*;
import static com.mc.urlshortener.UrlShortenerData.newUrlDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UrlShortenerServiceImplTest {

    @Autowired
    UrlShortenerServiceImpl urlServiceImpl;

    @MockBean
    UrlShortenerRepository urlShortenerRepository;

    @Test
    public void testFindAll(){

        Page<UrlShortenerDto> mockUrlDto = new PageImpl<>(List.of(newUrlDto(), newUrlDtoOther()));
        Page<UrlShortener> mockUrl = new PageImpl<>(List.of(newUrl(), newUrlOther()));
        PageRequest page = PageRequest.of(0, 10);

        when(urlShortenerRepository.findAll(any(PageRequest.class))).thenReturn(mockUrl);

        Page<UrlShortenerDto> urls = urlServiceImpl.findAll(page);

        assertNotNull(urls);
        assertEquals(mockUrlDto.getContent().get(0).getUrlOrigin(), urls.getContent().get(0).getUrlOrigin());
        assertEquals(mockUrlDto.getTotalElements(), urls.getTotalElements());
    }

    @Test
    public void testFindById(){

        BigInteger id = BigInteger.ONE;
        UrlShortener mockUrlShortener = newUrl();

        when(urlShortenerRepository.findById(id)).thenReturn(mockUrlShortener);

        UrlShortenerDto url = urlServiceImpl.findById(id);

        assertNotNull(url);
        assertEquals(mockUrlShortener.getId(), url.getId());

    }

    @Test
    public void testFindByUrlShort(){

        String urlShort = "1234567";
        UrlShortener mockUrlShortener = newUrl();

        when(urlShortenerRepository.findByUrlShort(urlShort)).thenReturn(mockUrlShortener);

        UrlShortenerDto url = urlServiceImpl.findByUrlShort(urlShort);

        assertNotNull(url);
        assertEquals(mockUrlShortener.getId(), url.getId());

    }

    @Test
    public void testSave(){

        UrlShortener mockUrlShortener = newUrl();
        UrlShortenerRequestDto mockUrlDto = newUrlRequest();

        when(urlShortenerRepository.save(any())).thenReturn(mockUrlShortener);

        UrlShortenerDto url = urlServiceImpl.save(mockUrlDto);

        assertNotNull(url);
        assertEquals(mockUrlShortener.getId(), url.getId());

    }

    @Test
    public void testUpdate(){

        UrlShortener mockUrlShortener = newUrl();
        UrlShortenerDto mockUrlShortenerDto = newUrlDto();

        when(urlShortenerRepository.save(any())).thenReturn(mockUrlShortener);

        UrlShortenerDto url = urlServiceImpl.update(mockUrlShortenerDto);

        assertNotNull(url);
        assertEquals(mockUrlShortener.getId(), url.getId());

    }

}
