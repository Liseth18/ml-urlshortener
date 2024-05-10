package com.mc.urlshortener.controller;

import com.mc.urlshortener.dto.UrlShortenerRequestDto;
import com.mc.urlshortener.dto.UrlShortenerDto;
import com.mc.urlshortener.model.UrlShortener;
import com.mc.urlshortener.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/urlshortener/")
@Tag(name = "UrlShortenerController", description = "Url management APIs")
@RequiredArgsConstructor
@Slf4j
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    private final String NOT_FOUND = "URL not fount";

    @Operation(
            summary = "Get all the Urls",
            description = "Get a list of Url object"
            )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(array = @ArraySchema(schema = @Schema(implementation = UrlShortener.class)), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/")
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int numberPage,
            @RequestParam(defaultValue = "10") int sizePage
    ){
        try{
            log.info("Get all the URLs");
            PageRequest page = PageRequest.of(numberPage, sizePage);
            return ResponseEntity.ok(urlShortenerService.findAll(page));
        }catch (Exception ex){
            log.error("ERROR - Get all the URLs", ex);
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Get Url information by Id",
            description = "Get a Url object by specifying its id. The response is Url object with id, urlOrigin, urlShort and active."
            )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UrlShortener.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(value="id") BigInteger id){
        try{
            log.info("Get URL by id: {}", id);
            return ResponseEntity.ok(urlShortenerService.findById(id));
        }catch (Exception ex){
            log.error("ERROR - Get URL by id: {}. {}",id, ex.getMessage(), ex);
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Save URL",
            description = "Save a URL with its shortener"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UrlShortener.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/save")
    public ResponseEntity<?> saveUrl(
            @RequestBody UrlShortenerRequestDto url
    ){
        try{
            UrlValidator urlValidator = new UrlValidator();
            if(!urlValidator.isValid(url.getUrlOrigin())){
                log.info("Save- Invalid URL ({})", url.getUrlOrigin());
                return new ResponseEntity<Object>("Invalid URL", HttpStatus.BAD_REQUEST);
            }
            log.info("Save new url: {}", url.getUrlOrigin());
            return ResponseEntity.ok(urlShortenerService.save(url));
        }catch (Exception ex){
            log.error("ERROR - save new url. {}", ex.getMessage(), ex);
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Update URL",
            description = "Update a URL (for urlOrigin fields and their shortening)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UrlShortener.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping("/update/{urlShort}")
    public ResponseEntity<?> updateUrl(
            @PathVariable(value="urlShort") String urlShort,
            @RequestBody UrlShortenerRequestDto url
    ){
        try{
            log.info("Update url - urlShort: {}", urlShort);
            UrlShortenerDto urlExist = urlShortenerService.findByUrlShort(urlShort);

            if(!urlExist.getActive()){
                log.info("URL disabled (urlShort: {})", urlShort);
                return new ResponseEntity<Object>("This URL is disabled", HttpStatus.BAD_REQUEST);
            }

            UrlValidator urlValidator = new UrlValidator();
            if(!urlValidator.isValid(url.getUrlOrigin())){
                log.info("Update ({}) - Invalid URL ({})", urlShort, url.getUrlOrigin());
                return new ResponseEntity<Object>("Invalid URL", HttpStatus.BAD_REQUEST);
            }
            urlExist.setUrlOrigin(url.getUrlOrigin());
            return ResponseEntity.ok(urlShortenerService.update(urlExist));
        }catch (NotFoundException ex){
            log.error("URL not found (urlShort: {})", urlShort);
            return new ResponseEntity<Object>(NOT_FOUND, HttpStatus.NOT_FOUND);
        }catch (Exception ex){
            log.error("ERROR - update url (urlShort: {}). {}", urlShort, ex.getMessage(), ex);
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Enable/Disable URL",
            description = "Enable/Disable a URL"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UrlShortener.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping("/active/{urlShort}")
    public ResponseEntity<Object> activeUrl(
            @PathVariable(value="urlShort") String urlShort,
            @RequestParam(value="active") Boolean active
    ){
        try{
            log.info("Enable/Disable url (urlShort: {})", urlShort);
            UrlShortenerDto urlExist = urlShortenerService.findByUrlShort(urlShort);
            urlExist.setActive(active);
            return ResponseEntity.ok(urlShortenerService.update(urlExist));
        }catch (NotFoundException ex){
            log.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception ex){
            log.error("ERROR - enable/disable url (urlShort: {} - enable: {}). {}", urlShort, active, ex.getMessage(), ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Redirect URL",
            description = "Redirect to the original URL (depending on its shortening)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/redirect/{url}")
    public ResponseEntity<?> redirectUrl(
            @PathVariable(value="url") String urlShort,
            HttpServletResponse response
    ){
        try{
            log.info("Redirect url (urlShort: {})", urlShort);
            UrlShortenerDto url = urlShortenerService.findByUrlShort(urlShort);
            if (!url.getActive()) {
                log.info("URL is not active (urlShort: {})", urlShort);
                return new ResponseEntity<Object>("URL is not active", HttpStatus.BAD_REQUEST);
            }

            response.sendRedirect(url.getUrlOrigin());
            return ResponseEntity.ok(null);
        }catch (NotFoundException ex){
            log.error("URL not found (urlShort: {})", urlShort);
            return new ResponseEntity<Object>(NOT_FOUND, HttpStatus.NOT_FOUND);
        }catch (Exception ex){
            log.error("ERROR - redirect url (urlShort: {}). {}", urlShort, ex.getMessage(), ex);
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
