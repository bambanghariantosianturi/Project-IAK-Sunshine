package com.bambang.projectiaksunshine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by bambanghs on 5/24/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rain implements Serializable {

    @JsonProperty("1h")
    private float volumeInLastHour;

    @JsonProperty("3h")
    private float volumeInLast3Hours;

    public float getVolumeInLastHour() {
        return volumeInLastHour;
    }

    public void setVolumeInLastHour(float volumeInLastHour) {
        this.volumeInLastHour = volumeInLastHour;
    }

    public float getVolumeInLast3Hours() {
        return volumeInLast3Hours;
    }

    public void setVolumeInLast3Hours(float volumeInLast3Hours) {
        this.volumeInLast3Hours = volumeInLast3Hours;
    }

}
