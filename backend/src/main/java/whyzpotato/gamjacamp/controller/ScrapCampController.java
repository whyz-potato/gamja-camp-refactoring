package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSimple;
import whyzpotato.gamjacamp.controller.dto.Utility.PageResult;
import whyzpotato.gamjacamp.service.ScrapCampService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ScrapCampController {

    private final ScrapCampService scrapCampService;

    @PostMapping("/customer/my-camp")
    public ResponseEntity scrapCamp(@LoginMember SessionMember member,
                                    @RequestParam(value = "camp") Long campId) {

        scrapCampService.createScrap(member.getId(), campId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/my-camp")
    public PageResult<CampSimple> scrapCampList(@LoginMember SessionMember member,
                                                @PageableDefault(size = 10) Pageable pageable) {

        return new PageResult<CampSimple>(scrapCampService.scraps(member.getId(), pageable));
    }


}
