package com.bridgelabz.moviecatalogservice.resources;

import com.bridgelabz.moviecatalogservice.models.CatalogItem;

import com.bridgelabz.moviecatalogservice.models.Movie;
import com.bridgelabz.moviecatalogservice.models.Rating;
import com.bridgelabz.moviecatalogservice.models.UserRating;
import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private WebClient.Builder webClientBuilder;
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

       //     WebClient.Builder builder=WebClient.builder();
        UserRating ratings= restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating ->{

         Movie movie= restTemplate.getForObject("http://movie-info-service/movies/"+ rating.getMovieId(), Movie.class);

         return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        })
            .collect(Collectors.toList());
    }
}
//Alternative WebClient way
                /*
                 Movie movie = webClientBuilder.build()
                           .get()
                           .uri("http://localhost:8082/movies/"+ rating.getMovieId())
                           .retrieve()
                           .bodyToMono(Movie.class)
                           .block();
                */