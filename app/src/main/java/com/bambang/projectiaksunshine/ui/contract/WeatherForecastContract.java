package com.bambang.projectiaksunshine.ui.contract;

import com.bambang.projectiaksunshine.core.aggregation.WeatherForecastAggregation;

/**
 * Created by bambanghs on 5/24/2017.
 */

public interface WeatherForecastContract {

    public interface View {

        void setupWeatherForecast(WeatherForecastAggregation aggregation);

        void showLoadingLayout();

        void showErrorLayout();

        void showSuccessLayout();

        void showEmptyLayout();

    }

    public interface Presenter {

        WeatherForecastAggregation onSaveInstanceState();

        void onLoadInstanceState(WeatherForecastAggregation aggregation);

        void loadForecasts(String cityId);

        void refreshUi();

        void retryButtonClick(String cityId);

    }

}
