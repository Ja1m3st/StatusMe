package es.sanchez.jaime.statusme;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather")
    Call<WeatherData> getWeatherByCoords(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String apiKey);
}
