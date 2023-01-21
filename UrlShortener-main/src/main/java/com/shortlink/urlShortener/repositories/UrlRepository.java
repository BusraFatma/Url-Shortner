package com.shortlink.urlShortener.repositories;


import com.shortlink.urlShortener.models.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UrlRepository extends MongoRepository<Url, String> {

    Url findUrlByShortenedUrl(String url);

    Url findUrlByOriginalUrl(String url);
}
