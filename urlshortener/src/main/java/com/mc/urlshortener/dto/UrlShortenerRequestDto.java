package com.mc.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlShortenerRequestDto {

    private String urlOrigin;
    private String urlShort;
    private Boolean active;

}
