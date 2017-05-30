package com.bambang.projectiaksunshine.data.local.remote;

import com.bambang.projectiaksunshine.Constants;
import com.bambang.projectiaksunshine.core.aggregation.WeatherAggregation;
import com.bambang.projectiaksunshine.core.aggregation.WeatherForecastAggregation;
import com.bambang.projectiaksunshine.model.CityWeatherCondition;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bambanghs on 5/24/2017.
 */

public interface WeatherService {

    /**
     * Returns current weather for a city.
     *
     * @param city - The city name
     * @param units - Metric unit
     * @param apiKey - Open Weather Map's API key
     * @return City's current weather
     */
    @GET(Constants.WEATHER_PATH)
    Observable<CityWeatherCondition> getCurrentWeatherByCityName(
            @Query(Constants.Q_PARAMETER) String city,
            @Query(Constants.UNITS_PARAMETER) String units,
            @Query(Constants.API_KEY_PARAMETER) String apiKey);

    /**
     * Returns current weather for a group of cities according to informed ids.
     *
     * @param ids - Ids of cities separated by semicolon
     * @param units - Metric unit
     * @param apiKey - Open Weather Map's API key
     * @return Current Weather of each city
     */
    @GET(Constants.GROUP_PATH)
    Observable<WeatherAggregation> getCurrentWeatherForSeveralCityIds(
            @Query(Constants.ID_PARAMETER) String ids,
            @Query(Constants.UNITS_PARAMETER) String units,
            @Query(Constants.API_KEY_PARAMETER) String apiKey);


    // api.openweathermap.org/data/2.5/forecast/daily?id=7521912&units=metrics&APPID=0906b001b7ee999518a1a9cdfab10e78
    /**
     * Returns 6 day forecast includes current weather forecast.
     *
     * @param id - The city id
     * @param units - Metric unit
     * @param apiKey - Open Weather Map's API key
     * @return 5 day forecast includes weather data every 3 hours.
     */
    @GET(Constants.FORECAST_PATH)
    Observable<WeatherForecastAggregation> getWeatherForecastByCityId(
            @Query(Constants.ID_PARAMETER) String id,
            @Query(Constants.COUNT_PARAMETER) int count,
            @Query(Constants.UNITS_PARAMETER) String units,
            @Query(Constants.API_KEY_PARAMETER) String apiKey
    );

    /**
     * Builder class responsible to create new instances and provide RESTful services.
     */
    class Builder {

        private static HttpLoggingInterceptor getLoggingInterceptor() {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            return interceptor;
        }

        private static OkHttpClient getOkHttpClient(HttpLoggingInterceptor loggingInterceptor) {
            return new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }

        public static WeatherService build() {
            OkHttpClient client = getOkHttpClient(getLoggingInterceptor());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.ENDPOINT)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();

            return retrofit.create(WeatherService.class);
        }
    }
}
