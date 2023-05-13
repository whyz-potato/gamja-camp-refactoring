package whyzpotato.gamjacamp.domain;

import lombok.Getter;

@Getter
public class Coordinate {
    private Float campX;
    private Float campY;

    public Coordinate(Float campX, Float campY) {
        this.campX = campX;
        this.campY = campY;
    }
}
