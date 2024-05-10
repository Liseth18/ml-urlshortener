package com.mc.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mc.urlshortener.dto.UrlShortenerDto;
import com.mc.urlshortener.service.UrlShortenerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import static com.mc.urlshortener.UrlShortenerData.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlShortenerController.class)
@Slf4j
public class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UrlShortenerService urlShortenerService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    public void testFindAll() throws Exception {

        Page<UrlShortenerDto> mockUrl = new PageImpl<>(List.of(newUrlDto(), newUrlDtoOther()));
        PageRequest page = PageRequest.of(0, 10);

        when(urlShortenerService.findAll(any(PageRequest.class))).thenReturn(mockUrl);

        mvc.perform(get("/api/v1/urlshortener/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(BigInteger.ONE))
                .andExpect(jsonPath("$.content[1].id").value(BigInteger.TWO))
                .andExpect(jsonPath("$.content[0].urlOrigin").value("http://www.test.com"))
                .andExpect(jsonPath("$.content[1].urlOrigin").value("http://www.meli.com"));

        verify(urlShortenerService).findAll(page);
    }

    @Test
    public void testFindByUrlShort() throws Exception {

        when(urlShortenerService.findByUrlShort(any())).thenReturn(newUrlDto());

        mvc.perform(get("/api/v1/urlshortener/1234567").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));

        verify(urlShortenerService).findByUrlShort(newUrlDto().getUrlShort());
    }

    @Test
    public void testSave() throws Exception {
        UrlShortenerDto mockUrl = new UrlShortenerDto(BigInteger.ZERO, "http://www.testml.com", "7654321", true, LocalDateTime.now(), LocalDateTime.now());
        when(urlShortenerService.save(any())).then(invocation ->{
            mockUrl.setId(BigInteger.TEN);
            log.info("save test URL origin: {} {}", mockUrl.getUrlOrigin(), mockUrl.getUrlShort());
            return mockUrl;
        });

         mvc.perform(post("/api/v1/urlshortener/save").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockUrl)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.urlShort").value("7654321"));

        verify(urlShortenerService).save(any());
    }

    @Test
    public void testUpdate() throws Exception {
        BigInteger id = BigInteger.TWO;
        UrlShortenerDto existUrl = new UrlShortenerDto(id, "https://www.test1.com", "1234567", true, LocalDateTime.now(), LocalDateTime.now());
        UrlShortenerDto updateUrl = new UrlShortenerDto(id, "http://www.test.com", "1234567", true, LocalDateTime.now(), LocalDateTime.now());

        when(urlShortenerService.findByUrlShort(existUrl.getUrlShort())).thenReturn(existUrl);
        when(urlShortenerService.update(existUrl)).thenReturn(updateUrl);

        // when
        mvc.perform(put("/api/v1/urlshortener/update/1234567").contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(updateUrl)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.urlShort").value(updateUrl.getUrlShort()));

        verify(urlShortenerService).update(existUrl);
    }

}
