package com.example.ddmdemo.utils;

import com.example.ddmdemo.dto.GeoLocationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GeoSearchAPI {

    private final static String url = "https://us1.locationiq.com/v1/search";
    private final static String API_KEY = "API_KEY";

    // create static function
    public static GeoLocationDTO getGeoLocation(String address){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://us1.locationiq.com/v1/search?q="+ URLEncoder.encode(address, StandardCharsets.UTF_8)+"&format=json&key="+API_KEY)
                .get()
                .addHeader("accept", "application/json")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String bodyString = null;
        try {
            bodyString = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var parts = bodyString.split(",");
        int i = 0;
        double lat = 0;
        double lon = 0;
        for(String part : parts){
            if(i==2){
                break;
            }
            if(part.contains("\"lat\"")){
                lat = Double.parseDouble((part.split(":")[1]).replace("\"", ""));
                i++;
            }
            if(part.contains("\"lon\"")){
                lon = Double.parseDouble((part.split(":")[1]).replace("\"", ""));
                i++;
            }
        }


        System.out.println("GEO response: locationq: " + bodyString);

        return new GeoLocationDTO(lon, lat, address, true);
    }

}
