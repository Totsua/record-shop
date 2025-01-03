package com.northcoders.jv_record_shop.service;

import com.northcoders.jv_record_shop.model.Album;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AlbumCoverAPIServiceLayerImpl implements AlbumCoverAPIServiceLayer{
    @Override
    public String findAlbumCoverURL(String albumName) {
        return null;
    }


    /**
     * Method to make a request to the itunes API to get the URL of the album being queried
     * @param albumName name of the album being searched
     * @return the response body from the API
     */
    private String apiCaller(String albumName){
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://itunes.apple.com/search?term="+albumName+"&entity=album"))
                .GET()
                .build();

        String responseBody;
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
            return responseBody;
        } catch (IOException | InterruptedException e) {
            return "Default";
        }

    };

}
