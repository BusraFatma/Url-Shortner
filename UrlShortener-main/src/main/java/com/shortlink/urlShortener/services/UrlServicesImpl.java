package com.shortlink.urlShortener.services;


import com.google.common.hash.Hashing;
import com.shortlink.urlShortener.dtos.requests.UrlDto;
import com.shortlink.urlShortener.dtos.responses.UrlResponse;
import com.shortlink.urlShortener.exception.UrlException;
import com.shortlink.urlShortener.models.Url;
import com.shortlink.urlShortener.repositories.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Service
public class UrlServicesImpl implements UrlServices {

    @Autowired
    private UrlRepository urlRepository;

    String encodedUrl = "";

    @Override
    public UrlResponse generateShortLink(UrlDto request) throws UrlException {
        UrlResponse response = new UrlResponse();


        if (!StringUtils.isEmpty(request.getUrl())) {
            if (!isUrlValid(request.getUrl())) {

                throw new UrlException("Not a Valid Url");
            } else {
                String encodedUrl = encodeUrl(request.getUrl());
                Url url = new Url();
                url.setOriginalUrl(request.getUrl());
                url.setCreationDate(LocalDateTime.now());
                url.setShortenedUrl(encodedUrl);

                Url savedUrl = urlRepository.save(url);


                response.setOriginalUrl(savedUrl.getOriginalUrl());
                response.setShortenedUrl(savedUrl.getShortenedUrl());

            }
            return response;

        }
        throw new UrlException("url cannot be empty");
    }

    @Override
    public String updateShortLink(String shortenedUrl, String customLink) throws UrlException {
        Optional<Url> foundUrl = Optional.ofNullable(urlRepository.findUrlByShortenedUrl(shortenedUrl));

        if (!foundUrl.isPresent()) {
            throw new UrlException("url not present");
        }

        foundUrl.get().setShortenedUrl(customLink);

        urlRepository.save(foundUrl.get());
        return foundUrl.get().getShortenedUrl();
    }

    @Override
    public String getEncodedUrl(String url) {
        Url urls = urlRepository.findUrlByOriginalUrl(url);
        return urls.getShortenedUrl();
    }

    @Override
    public void deleteShortLink(String url) throws UrlException {
        Optional<Url> foundUrl= Optional.ofNullable(urlRepository.findUrlByShortenedUrl(url));
        if (foundUrl.isPresent()){
            urlRepository.delete(foundUrl.get());
        }
        else {
            Optional<Url> foundUrl1= Optional.ofNullable(urlRepository.findUrlByOriginalUrl(url));
            if(foundUrl1.isPresent()) {
                urlRepository.delete(foundUrl.get());
            }
            else throw new UrlException("url not found");
        }
    }

    @Override
    public String getDecodedUrl(String url) {
        Url urls = urlRepository.findUrlByShortenedUrl(url);
        return urls.getOriginalUrl();
    }

    @Override
    public void delete(Url url) {
        urlRepository.delete(url);
    }

    @Override
    public void deleteUser(UrlDto request) {
        Url url = urlRepository.findUrlByOriginalUrl(request.getUrl());
        urlRepository.delete(url);
    }

    private String encodeUrl(String url) {

        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodedUrl;
    }

    private boolean isUrlValid(String url) {

        String regex = "((http|https)://)(www.)?"
                + "[a-zA-Z0-9@:%._\\+~#?&//=]"
                + "{2,256}\\.[a-z]"
                + "{2,6}\\b([-a-zA-Z0-9@:%"
                + "._\\+~#?&//=]*)";


        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(url);

        return m.matches();
    }
}
