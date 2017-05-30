package com.bambang.projectiaksunshine.ui.presenter;

import android.content.Intent;
import android.util.Log;

import com.bambang.projectiaksunshine.data.local.repository.WeatherRepository;
import com.bambang.projectiaksunshine.model.CityWeatherCondition;
import com.bambang.projectiaksunshine.ui.contract.SearchCityContract;

import rx.Subscriber;

/**
 * Created by bambanghs on 5/24/2017.
 */

public class SearchCityPresenter implements SearchCityContract.Presenter {

    private static final String TAG = "SearchCityPresenter";

    private SearchCityContract.View view;

    private WeatherRepository repository;

    private CityWeatherCondition cityWeatherCondition;

    public SearchCityPresenter(SearchCityContract.View view, WeatherRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void handleIntent(Intent intent) {
        view.handleIntent(intent);
    }

    @Override
    public void loadCity(String cityName) {
        view.showLoadingLayout();
        repository.getCurrentWeatherByCityName(cityName).subscribe(new Subscriber<CityWeatherCondition>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "[onCompleted]");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.showErrorLayout();
            }

            @Override
            public void onNext(CityWeatherCondition cityWeatherCondition) {
                SearchCityPresenter.this.cityWeatherCondition = cityWeatherCondition;
                refreshUi();
            }
        });
    }

    @Override
    public void refreshUi() {
        if (cityWeatherCondition == null) {
            view.showEmptyLayout();
        } else {
            view.showSuccessLayout();
            view.setupCurrentWeather(cityWeatherCondition);
        }
    }

    @Override
    public void addCity(CityWeatherCondition cityWeatherCondition) {
        repository.addCityId(String.valueOf(cityWeatherCondition.getId()));
        view.goBackToWeatherList();
    }
}
