package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSearchItem;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSearchResult;
import whyzpotato.gamjacamp.service.CampService;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/camps")
public class CampController {
    private final CampService campService;

    @GetMapping("/search")
    public CampSearchResult<CampSearchItem> search(@RequestParam String query,
                                                   @RequestParam("ne-lat") Double neLatitude,
                                                   @RequestParam("sw-lat") Double swLatitude,
                                                   @RequestParam("ne-lng") Double neLongitude,
                                                   @RequestParam("sw-lng") Double swLongitude,
                                                   @RequestParam(value = "check-in", defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                                   @RequestParam(value = "check-out", defaultValue = "#{T(java.time.LocalDate).now().plusDays(1)}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                                   @RequestParam(value = "guests", defaultValue = "2") int numGuests,
                                                   Pageable pageable) {

        return campService.search(query, neLatitude, swLatitude, neLongitude, swLongitude, checkIn, checkOut, numGuests, pageable);
    }
}
