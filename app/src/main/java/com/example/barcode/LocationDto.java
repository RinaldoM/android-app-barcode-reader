package com.example.barcode;

public class LocationDto {
    private final String id;
    private final String locationName;


    public LocationDto(String id, String locationName) {
        this.id = id;
        this.locationName = locationName;
    }

    public String getId() {
        return id;
    }

    public String getLocationName() {
        return locationName;
    }
}
