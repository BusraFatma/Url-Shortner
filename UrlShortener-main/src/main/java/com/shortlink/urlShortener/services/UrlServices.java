package com.shortlink.urlShortener.services;

import com.shortlink.urlShortener.dtos.requests.UrlDto;
import com.shortlink.urlShortener.dtos.responses.UrlResponse;
import com.shortlink.urlShortener.exception.UrlException;
import com.shortlink.urlShortener.models.Url;

public interface UrlServices {
    UrlResponse generateShortLink(UrlDto request) throws UrlException;

    String updateShortLink(String shortenedUrl, String customLink) throws UrlException;

    String getEncodedUrl(String url);


    void deleteShortLink(String url) throws UrlException;

    String getDecodedUrl(String url);

    void delete(Url url);

    void deleteUser(UrlDto request);
}
