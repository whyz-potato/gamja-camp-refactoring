package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
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


}
