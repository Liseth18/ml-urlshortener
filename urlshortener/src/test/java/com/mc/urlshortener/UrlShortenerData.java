package com.mc.urlshortener;

import com.mc.urlshortener.dto.UrlShortenerDto;
import com.mc.urlshortener.dto.UrlShortenerRequestDto;
import com.mc.urlshortener.model.UrlShortener;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class UrlShortenerData {

    public static UrlShortener newUrl(){
        return new UrlShortener(
                BigInteger.ONE,
                "http://www.test.com",
                "1234567",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static UrlShortenerDto newUrlDto(){
        return new UrlShortenerDto(
                BigInteger.ONE,
                "http://www.test.com",
                "1234567",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static UrlShortenerRequestDto newUrlRequest(){
        return new UrlShortenerRequestDto(
                "http://www.test.com",
                "1234560",
                true
        );
    }

    public static UrlShortener newUrlOther(){
        return new UrlShortener(
                BigInteger.TWO,
                "http://www.meli.com",
                "1234569",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static UrlShortenerDto newUrlDtoOther(){
        return new UrlShortenerDto(
                BigInteger.TWO,
                "http://www.meli.com",
                "1234569",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
