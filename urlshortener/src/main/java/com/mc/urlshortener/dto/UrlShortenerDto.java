package com.mc.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlShortenerDto implements Serializable {

    private static final long serialVersionUID = -4356649271072686976L;

    private BigInteger id;
    private String urlOrigin;
    private String urlShort;
    private Boolean active;
    private LocalDateTime creationDate;
    private LocalDateTime updatedDate;

}
