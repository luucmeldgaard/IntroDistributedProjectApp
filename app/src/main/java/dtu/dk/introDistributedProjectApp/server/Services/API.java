package dtu.dk.introDistributedProjectApp.server.Services;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import com.google.gson.Gson;
import okhttp3.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;

public class API {

    private static final String BASE_URL = "https://unofficialurbandictionaryapi.com/api/random?";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    private static API instance;

    // Private constructor to prevent instantiation
    private API() {}

    // Public method to provide access to the instance
    public static API getInstance() {
        if (instance == null) {
            instance = new API();
        }
        return instance;
    }

    public List<WordDefinition> callUrbanDictionaryAPI(Params params) {
        String url = BASE_URL + params.toQueryString();

        // Build the HTTP request
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            // Execute the request and get the response
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                // Parse the response JSON
                String responseBody = response.body().string();
                ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);
                return apiResponse.getData();
            } else {
                System.err.println("Error: " + (response.body() != null ? response.body().string() : "No response body"));
            }
        } catch (IOException e) {
            System.out.println("Error when calling API");
            e.printStackTrace();
        }
        return List.of();
    }
}