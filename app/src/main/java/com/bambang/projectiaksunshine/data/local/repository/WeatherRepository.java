package com.bambang.projectiaksunshine.data.local.repository;

import com.bambang.projectiaksunshine.core.aggregation.WeatherAggregation;
import com.bambang.projectiaksunshine.core.aggregation.WeatherForecastAggregation;
import com.bambang.projectiaksunshine.model.CityWeatherCondition;

import java.util.Set;

import rx.Observable;

/**
 * Created by bambanghs on 5/24/2017.
 */

public interface WeatherRepository {

    void addCityId(String cityId);

    Set<String> getCitiesIds();

    void removeCityIdByPosition(int position);

    Observable<CityWeatherCondition> getCurrentWeatherByCityName(String cityName);

    Observable<WeatherAggregation> getCitiesWeather();

    Observable<WeatherAggregation> synchronizeCityWeathers(String citiesId);

    Observable<WeatherForecastAggregation> getWeatherForecast();

    Observable<WeatherForecastAggregation> synchronizeWeatherForecast(String cityId);

}
