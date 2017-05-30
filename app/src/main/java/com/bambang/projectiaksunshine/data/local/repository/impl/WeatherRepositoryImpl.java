package com.bambang.projectiaksunshine.data.local.repository.impl;

import com.bambang.projectiaksunshine.BuildConfig;
import com.bambang.projectiaksunshine.Constants;
import com.bambang.projectiaksunshine.core.aggregation.WeatherAggregation;
import com.bambang.projectiaksunshine.core.aggregation.WeatherForecastAggregation;
import com.bambang.projectiaksunshine.data.local.WeatherCache;
import com.bambang.projectiaksunshine.data.local.WeatherDataBase;
import com.bambang.projectiaksunshine.data.local.remote.WeatherService;
import com.bambang.projectiaksunshine.data.local.repository.WeatherRepository;
import com.bambang.projectiaksunshine.model.CityWeatherCondition;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bambanghs on 5/24/2017.
 */

public class WeatherRepositoryImpl implements WeatherRepository {

    private static final String TAG = "WeatherRepositoryImpl";

    private WeatherService service;
    private WeatherCache cache;
    private Set<String> citiesIds;

    public WeatherRepositoryImpl(WeatherService service, WeatherCache cache) {
        this.service = service;
        this.cache = cache;

        citiesIds = WeatherDataBase.getCitiesIds();
        if (citiesIds == null) {
            citiesIds = new HashSet<>();
        }
    }

    @Override
    public void addCityId(String cityId) {
        citiesIds.add(cityId);
        WeatherDataBase.saveCitiesIds(citiesIds);
    }

    @Override
    public Set<String> getCitiesIds() {
        return WeatherDataBase.getCitiesIds();
    }

    @Override
    public void removeCityIdByPosition(int position) {
        WeatherDataBase.removeCityIdByPosition(position);
    }

    @Override
    public Observable<CityWeatherCondition> getCurrentWeatherByCityName(final String cityName) {
        return service.getCurrentWeatherByCityName(cityName, Constants.METRIC_VALUE, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<WeatherAggregation> getCitiesWeather() {
        return cache.get(Constants.WEATHER_KEY);
    }

    @Override
    public Observable<WeatherAggregation> synchronizeCityWeathers(String citiesId) {
        return service.getCurrentWeatherForSeveralCityIds(citiesId, Constants.METRIC_VALUE, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(new Func1<WeatherAggregation, Observable<WeatherAggregation>>() {
                    @Override
                    public Observable<WeatherAggregation> call(WeatherAggregation aggregation) {
                        return cache.set(aggregation, Constants.WEATHER_KEY);
                    }
                });
    }

    public Observable<WeatherForecastAggregation> getWeatherForecast() {
        return cache.get(Constants.FORECAST_KEY);
    }

    @Override
    public Observable<WeatherForecastAggregation> synchronizeWeatherForecast(String cityId) {
        return service.getWeatherForecastByCityId(cityId, Constants.COUNT_VALUE+1, Constants.METRIC_VALUE, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(new Func1<WeatherForecastAggregation, Observable<? extends WeatherForecastAggregation>>() {
                    @Override
                    public Observable<? extends WeatherForecastAggregation> call(WeatherForecastAggregation aggregation) {
                        return cache.set(aggregation, Constants.FORECAST_KEY);
                    }
                });
    }
}
