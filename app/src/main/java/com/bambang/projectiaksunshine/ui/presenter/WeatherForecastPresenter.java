package com.bambang.projectiaksunshine.ui.presenter;

import android.util.Log;

import com.bambang.projectiaksunshine.core.aggregation.WeatherForecastAggregation;
import com.bambang.projectiaksunshine.data.local.repository.WeatherRepository;
import com.bambang.projectiaksunshine.ui.contract.WeatherForecastContract;

import rx.Subscriber;

/**
 * Created by bambanghs on 5/24/2017.
 */

public class WeatherForecastPresenter implements WeatherForecastContract.Presenter {

    private static final String TAG = "ForecastPresenter";

    private WeatherForecastContract.View view;

    private WeatherRepository repository;

    private WeatherForecastAggregation aggregation;

    public WeatherForecastPresenter(WeatherForecastContract.View view, WeatherRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public WeatherForecastAggregation onSaveInstanceState() {
        return aggregation;
    }

    @Override
    public void onLoadInstanceState(WeatherForecastAggregation aggregation) {
        this.aggregation = aggregation;
    }

    @Override
    public void loadForecasts(String cityId) {
        view.showLoadingLayout();
        repository.synchronizeWeatherForecast(cityId).subscribe(new Subscriber<WeatherForecastAggregation>() {
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
            public void onNext(WeatherForecastAggregation aggregation) {
                WeatherForecastPresenter.this.aggregation = aggregation;
                refreshUi();
            }
        });
    }

    @Override
    public void refreshUi() {
        if (aggregation != null && aggregation.getForecasts().isEmpty()) {
            view.showEmptyLayout();
        } else {
            view.showSuccessLayout();
            view.setupWeatherForecast(aggregation);
        }
    }

    @Override
    public void retryButtonClick(String cityId) {
        loadForecasts(cityId);
    }
}
