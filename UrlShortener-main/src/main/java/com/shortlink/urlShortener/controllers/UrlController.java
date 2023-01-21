package com.shortlink.urlShortener.controllers;


import com.shortlink.urlShortener.dtos.requests.UrlDto;
import com.shortlink.urlShortener.exception.UrlException;
import com.shortlink.urlShortener.services.UrlServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/url")
public class UrlController {

    @Autowired
    UrlServices urlServices;

    @PostMapping("/getShortLink")
    public ResponseEntity<?> getShortLinkOfUrl(@RequestBody UrlDto request){
        try{
            return new ResponseEntity<>(urlServices.generateShortLink(request), HttpStatus.OK);
        }
        catch(UrlException ex){
            return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity <?> redirectToOriginalUrl(@PathVariable String shortUrl){
        return new ResponseEntity<>(urlServices.getDecodedUrl(shortUrl), HttpStatus.FOUND);
    }

    @GetMapping("/get/{shortUrl}")
    public void getFullUrl(HttpServletResponse response, @PathVariable String shortUrl) throws IOException {
        response.sendRedirect(urlServices.getDecodedUrl(shortUrl));
    }

    @GetMapping("/get/get/{shortUrl}")
    public RedirectView getFullUrl1(@PathVariable String shortUrl){
        RedirectView redirectView = new RedirectView();

        redirectView.setUrl( urlServices.getDecodedUrl(shortUrl));
        return redirectView;
    }

    @DeleteMapping("/{url}")
    public void deleteUrl(@PathVariable String url) throws UrlException {
        urlServices.deleteShortLink(url);
    }


    @PatchMapping("/customiseUrl")
    public ResponseEntity<?> customiseShortLink(@RequestParam String shortLink, @RequestParam String customiseUrl)
            throws UrlException {
        return new ResponseEntity<>(urlServices.updateShortLink(shortLink,customiseUrl), HttpStatus.ACCEPTED);
    }
}
