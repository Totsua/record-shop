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
    public String findAlbumCoverURL(String albumName, String artistName) {
        return collectIdFromApiResponseBody(musicbrainzApiCaller(albumName), artistName);
    }


    /**
     * Method to make a request to the Musicbrainz API to get the URL of the album being queried
     * @param albumName name of the album being searched
     * @return the response body from the API
     */

    private String musicbrainzApiCaller(String albumName){
        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://musicbrainz.org/ws/2/release?query="+albumName.replace(" ","+")+"&fmt=json"))
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
     * Method to find the Musicbrainz id of the correct album from the response body from the Musicbrainz api call
     * @param response the response body from the API
     * @return the method call of getCoverArtURL using the Musicbrainz id,  if none are found then "Default" is returned
     */
    private String collectIdFromApiResponseBody(String response, String artistName){
        ObjectMapper mapper = new ObjectMapper();

        if(response.equals("Default")){
            return response;
        }

        try{
            JsonNode root = mapper.readTree(response);
            JsonNode results = root.get("releases");


            if(results.isEmpty()){
                return "Default";
            }

            for(JsonNode node : results ){
                JsonNode artistCredNodePath = node.path("artist-credit");
                JsonNode artistDetails = artistCredNodePath.get(0);
                String artistNameJson = artistDetails.get("name").toString();

                if(artistNameJson.equalsIgnoreCase( "\""+artistName+ "\"")){

                    String id = node.get("id").asText();
                    /*System.out.println(id);*/
                    return getCoverArtURL(id);
                }

            }
            return "Default";

        } catch (JsonProcessingException e) {
            return "Default";
        }

    }


    /**
     * Method to obtain the set of cover art urls from the Cover art archive API
     * @param id the MusicBrainz API id of the album
     * @return the image url of the cover art, if none found "Default" is returned
     */

    private String getCoverArtURL(String id){
        // .followRedirects needed because the cover art archive api sends a redirect for an initial response
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://coverartarchive.org/release/" + id+ "?fmt=json"))
                .header("Accept","application/json")
                .GET()
                .build();

        String responseBody;
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
            return extractURL(responseBody);

        } catch (IOException | InterruptedException e) {
            System.out.println("Problem with the obtaining a response from the URL");
            e.printStackTrace();
            return "Default";
        }

    }

    /**
     * Method to obtain the image url from the Cover Art Archive API response body
     * @param response the response body of the Cover Art Archive API
     * @return the url of the cover art, if none found "Default" is returned
     */

    private String extractURL(String response){
        ObjectMapper mapper = new ObjectMapper();

        try{
            JsonNode results = mapper.readTree(response);
            JsonNode images = results.get("images");
            JsonNode imageNode = images.get(0);

            if(imageNode.get("image")!= null){
                // Url is obtained with speech marks, those need to be removed
                return imageNode.get("image").toString().split("\"")[1];
            }

            return "Default";

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Default";
        }

    }

}
