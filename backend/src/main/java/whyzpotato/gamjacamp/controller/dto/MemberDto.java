package whyzpotato.gamjacamp.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;

public class MemberDto {

    @Getter
    @NoArgsConstructor
    public static class MemberSimple{

        private Long id;
        private String name;
        private String email;

        public MemberSimple(Member member){
            this.id = member.getId();
            this.name = member.getUsername();
            this.email = member.getAccount();
        }

    }



}
