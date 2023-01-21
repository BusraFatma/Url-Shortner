package com.shortlink.urlShortener.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Url {
    @Id
    private String id;

    private String originalUrl;
    private String shortenedUrl;

    private LocalDateTime creationDate;
}
