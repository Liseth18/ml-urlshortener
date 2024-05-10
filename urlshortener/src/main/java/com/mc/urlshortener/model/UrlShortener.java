package com.mc.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Document(collection = "url")
@JsonPropertyOrder({"id", "urlOrigin", "urlShort", "active"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlShortener implements Serializable {

    private static final long serialVersionUID = 1154217727621536077L;

    @Id
    private BigInteger id;

    @NotNull
    private String urlOrigin;

    @NotNull
    @Indexed(unique = true)
    private String urlShort;

    @NotNull
    private Boolean active;

    @CreatedDate
    private LocalDateTime creationDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

}
