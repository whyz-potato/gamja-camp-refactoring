package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.ScrapCamp;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.ScrapCampRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class ScrapCampService {

    private final ScrapCampRepository scrapCampRepository;
    private final MemberRepository memberRepository;
    private final CampRepository campRepository;

    public ScrapCamp createScrap(Long memberId, Long campId) {
        Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
        Camp camp = campRepository.findById(campId).orElseThrow(IllegalArgumentException::new);

        return scrapCampRepository.save(new ScrapCamp(member, camp));
    }


}
