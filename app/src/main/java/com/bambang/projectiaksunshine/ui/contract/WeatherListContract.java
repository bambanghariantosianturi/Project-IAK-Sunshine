package com.bambang.projectiaksunshine.ui.contract;

import com.bambang.projectiaksunshine.core.aggregation.WeatherAggregation;
import com.bambang.projectiaksunshine.model.CityWeatherCondition;

import java.util.List;

/**
 * Created by bambanghs on 5/24/2017.
 */

public interface WeatherListContract {

    interface View {

        void setupWeatherList(List<CityWeatherCondition> cities);

        void showLoadingLayout();

        void showErrorLayout();

        void showSuccessLayout();

        void showEmptyLayout();

        void checkSwipeRefreshing();

        void showWeatherForecast(CityWeatherCondition city);

        void findCity();

    }

    interface Presenter {

        WeatherAggregation onSaveInstanceState();

        void onLoadInstanceState(WeatherAggregation aggregation);

        void loadCities();

        void refreshUi();

        void retryButtonClick();

        void weatherItemClick(int position);

        void removeCity(int position);

    }
}
