package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whyzpotato.gamjacamp.controller.dto.CampDto.SearchItem;
import whyzpotato.gamjacamp.controller.dto.Utility.PageResult;
import whyzpotato.gamjacamp.service.CampService;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/camps")
public class CampController {
    private final CampService campService;

    @GetMapping("/search")
    public PageResult<SearchItem> search(@RequestParam String query,
                                         @RequestParam("ne-lat") Double neLatitude, @RequestParam("sw-lat") Double swLatitude,
                                         @RequestParam("ne-lng") Double neLongitude, @RequestParam("sw-lng") Double swLongitude,
                                         @RequestParam("check-in") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                         @RequestParam("check-out") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                         @RequestParam("guests") int numGuests,
                                         @PageableDefault Pageable pageable) {

        return campService.findAll(checkIn, checkOut, pageable);
    }

}
