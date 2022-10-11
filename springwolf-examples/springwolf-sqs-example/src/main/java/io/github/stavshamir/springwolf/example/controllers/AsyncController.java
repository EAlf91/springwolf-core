package io.github.stavshamir.springwolf.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.stavshamir.springwolf.asyncapi.AsyncApiSerializerService;
import io.github.stavshamir.springwolf.asyncapi.AsyncApiService;
import io.github.stavshamir.springwolf.asyncapi.types.ProducerData;
import io.github.stavshamir.springwolf.configuration.AsyncApiDocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Set;


@RestController
public class AsyncController {

    @Autowired
    private  AsyncApiDocket docket;
    @Autowired
    private  AsyncApiService asyncApiService;
    @Autowired
    private  AsyncApiSerializerService serializer;

    @GetMapping("/gen/report")
    public ResponseEntity<String> genReport() throws JsonProcessingException {
        System.out.println(serializer.toJsonString(asyncApiService.getAsyncAPI()));

        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(serializer.toJsonString(asyncApiService.getAsyncAPI()), httpHeaders, HttpStatus.OK);

    }
}
