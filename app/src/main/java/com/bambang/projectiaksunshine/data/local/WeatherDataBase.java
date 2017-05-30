package com.bambang.projectiaksunshine.data.local;

import com.bambang.projectiaksunshine.Constants;

import java.util.Set;

import io.paperdb.Paper;

/**
 * Created by bambanghs on 5/24/2017.
 */

public class WeatherDataBase {

    public static void saveCitiesIds(Set<String> citiesIds) {
        Paper.book().write(Constants.CITIES_IDS, citiesIds);
    }

    public static Set<String> getCitiesIds() {
        return Paper.book().read(Constants.CITIES_IDS);
    }

    public static void removeCityIdByPosition(int position) {
        Set<String> citiesIds = getCitiesIds();

        int i = 0;
        for (String cityId: citiesIds) {
            if (i == position) {
                citiesIds.remove(cityId);
                break;
            }
            i++;
        }

        saveCitiesIds(citiesIds);
    }
}
