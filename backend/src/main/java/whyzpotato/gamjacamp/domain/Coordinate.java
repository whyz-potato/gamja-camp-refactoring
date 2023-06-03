package whyzpotato.gamjacamp.domain;

import lombok.Getter;

@Getter
public class Coordinate {
    private double longitude;
    private double latitude;

    public Coordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
