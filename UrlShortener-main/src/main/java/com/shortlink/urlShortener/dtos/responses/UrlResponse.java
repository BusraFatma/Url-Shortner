package com.shortlink.urlShortener.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UrlResponse {
    private String originalUrl;
    private String shortenedUrl;
}
