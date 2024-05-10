package com.mc.urlshortener.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info=
   @Info(
      title="UrlShortener-Meli",
      version="1.0.0",
      description = "This is application for URL Shortener"
   )
)
public class OpenAPIConfig {
}
