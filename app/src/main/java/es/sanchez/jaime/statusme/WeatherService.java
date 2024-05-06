package es.sanchez.jaime.statusme;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherService {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "ebd6802b699e7d4e9b47737bb0cc0623";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

    public static WeatherAPI getWeatherAPI() {
        return weatherAPI;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String buildUrl(double latitude, double longitude) {
        String latitudeParam = "lat=" + latitude;
        String longitudeParam = "&lon=" + longitude;
        String apiKeyParam = "&appid=" + API_KEY;

        return BASE_URL + "weather?" + latitudeParam + longitudeParam + apiKeyParam;
    }
}