package com.northcoders.jv_record_shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        return collectUrlFromApiResponseBody(apiCaller(albumName));
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

    /**
     * Method to find the URL from the response from the API request
     * @param response the response body from the API
     * @return the URL obtained from the response body, if none are found then "Default" is returned
     */
    private String collectUrlFromApiResponseBody(String response){
        ObjectMapper mapper = new ObjectMapper();

        if(response.equals("Default")){
            return response;
        }

        try{
            JsonNode root = mapper.readTree(response);
            JsonNode results = root.get("results");

            if(results.isEmpty()){
                return "Default";
            }

            // get the first result
            return results.get(0).get("artworkUrl100").asText();

        } catch (JsonProcessingException e) {
           return "Default";
        }

    }

}
