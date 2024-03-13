package whyzpotato.gamjacamp.config;

import org.springframework.core.convert.converter.Converter;
import whyzpotato.gamjacamp.domain.ReservationStatus;

public class ReservationStatusConverter implements Converter<String, ReservationStatus> {
    @Override
    public ReservationStatus convert(String source) {
        return ReservationStatus.valueOf(source.toUpperCase());
    }
}
