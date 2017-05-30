package com.bambang.projectiaksunshine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bambanghs on 5/24/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Temperature {

    @JsonProperty("day")
    private float day;

    @JsonProperty("min")
    private float minimunDaily;

    @JsonProperty("max")
    private float maximunDaily;

    @JsonProperty("night")
    private float night;

    @JsonProperty("eve")
    private float evening;

    @JsonProperty("morn")
    private float morning;

    public float getDay() {
        return day;
    }

    public void setDay(float day) {
        this.day = day;
    }

    public float getMinimunDaily() {
        return minimunDaily;
    }

    public void setMinimunDaily(float minimunDaily) {
        this.minimunDaily = minimunDaily;
    }

    public float getMaximunDaily() {
        return maximunDaily;
    }

    public void setMaximunDaily(float maximunDaily) {
        this.maximunDaily = maximunDaily;
    }

    public float getNight() {
        return night;
    }

    public void setNight(float night) {
        this.night = night;
    }

    public float getEvening() {
        return evening;
    }

    public void setEvening(float evening) {
        this.evening = evening;
    }

    public float getMorning() {
        return morning;
    }

    public void setMorning(float morning) {
        this.morning = morning;
    }
}
