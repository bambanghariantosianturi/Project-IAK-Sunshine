package com.bambang.projectiaksunshine.ui.contract;

import android.content.Intent;

import com.bambang.projectiaksunshine.model.CityWeatherCondition;

/**
 * Created by bambanghs on 5/24/2017.
 */

public class SearchCityContract {

    public interface View {

        void setupCurrentWeather(CityWeatherCondition cityWeatherCondition);

        void handleIntent(Intent intent);

        void showLoadingLayout();

        void showErrorLayout();

        void showSuccessLayout();

        void showEmptyLayout();

        void goBackToWeatherList();

    }

    public interface Presenter {

        void handleIntent(Intent intent);

        void loadCity(String cityName);

        void refreshUi();

        void addCity(CityWeatherCondition cityWeatherCondition);

    }
}
