package dtu.dk.introDistributedProjectApp.server.Services;

import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class API {

    private static final String BASE_URL = "https://unofficialurbandictionaryapi.com/api/random?";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    private static API instance;

    // Private constructor to prevent instantiation
    private API() {}

    // Public method to provide access to the instance (Singleton Pattern)
    public static API getInstance() {
        if (instance == null) {
            instance = new API();
        }
        return instance;
    }

    public List<WordDefinition> callUrbanDictionaryAPI(Params params) {
        // Construct the URL with query parameters
        String url = BASE_URL + params.toQueryString();

        // Create the GET request
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // Parse JSON response
                String responseBody = response.body().string();
                return gson.fromJson(responseBody, ApiResponse.class).getData();
            } else {
                System.err.println("Error: " + response.code());
            }
        } catch (IOException e) {
            System.out.println("Error when calling API");
            e.printStackTrace();
        }

        return Collections.emptyList(); // If it fails, return empty list
    }
}