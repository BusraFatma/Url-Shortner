package com.shortlink.urlShortener.services;

import com.shortlink.urlShortener.dtos.requests.UrlDto;
import com.shortlink.urlShortener.dtos.responses.UrlResponse;
import com.shortlink.urlShortener.exception.UrlException;
import com.shortlink.urlShortener.models.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;


@DataMongoTest
class UrlServicesImplTest {


    @Autowired
    private UrlServices urlServices;

    UrlDto request;


    @BeforeEach
    void setUp() {
        request = new UrlDto();
        request.setUrl("https://www.facebook.com");
    }

    @Test
    void testToSetAndGetUrl(){
        Url url = new Url();
        url.setOriginalUrl("www.facebook.com");
        assertEquals("www.facebook.com", url.getOriginalUrl());
    }

    @Test
    void testToGetOriginalUrl() throws UrlException {

        UrlResponse response = urlServices.generateShortLink(request);

        assertEquals("https://www.facebook.com" , response.getOriginalUrl());
    }

    @Test
    void invalidUrlThrowsExceptionTest(){
        UrlDto request = new UrlDto();
        request.setUrl("htt://www.facebook.com");
        assertThrows(UrlException.class,()->urlServices.generateShortLink(request) );
    }

    @Test
    void testThatEmptyUrlCannotBeInputted(){
        UrlDto request = new UrlDto();
        request.setUrl(" ");
        assertThrows(UrlException.class,()->urlServices.generateShortLink(request) );
    }

    @Test
    void testThatUpdateUrl() throws UrlException {

        UrlResponse response = urlServices.generateShortLink(request);

        String shortUrl = urlServices.updateShortLink(response.getShortenedUrl(),"friend");
        String url = urlServices.getEncodedUrl("https://www.facebook.com");
        assertEquals(shortUrl, url);
    }
}