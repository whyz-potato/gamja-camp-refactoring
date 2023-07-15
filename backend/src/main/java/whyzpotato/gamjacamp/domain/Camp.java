package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.dto.camp.CampUpdateRequestDto;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="camp")
public class Camp {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name="memberId")
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column
    private String phone;

    @Column
    private String campIntroduction;

    @Column(nullable = false)
    private Float campX;

    @Column(nullable = false)
    private Float campY;

    @Column
    private LocalTime campOperationStart;

    @Column
    private LocalTime campOperationEnd;

    @OneToMany(mappedBy="camp")
    private List<Room> rooms = new ArrayList<Room>();

    // reviews

    @OneToMany(mappedBy = "camp")
    private List<Image> images = new ArrayList<Image>();

    @Builder
    public Camp(Member member, String name, String address, String phone, String campIntroduction, Float campX, Float campY, LocalTime campOperationStart, LocalTime campOperationEnd, List<Image> images) {
        this.member = member;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.campIntroduction = campIntroduction;
        this.campX = campX;
        this.campY = campY;
        this.campOperationStart = campOperationStart;
        this.campOperationEnd = campOperationEnd;
        this.images = images;
    }

    public Camp update(CampUpdateRequestDto campUpdateRequestDto) {
        this.name = campUpdateRequestDto.getName();
        this.phone = campUpdateRequestDto.getPhone();
        this.campIntroduction = campUpdateRequestDto.getCampIntroduction();
        return this;
    }

    public Camp updateAddress(String address, Coordinate coordinate) {
        this.address = address;
        this.campX = coordinate.getCampX();
        this.campY = coordinate.getCampY();
        return this;
    }

    public Camp updateOperatingHours(LocalTime start, LocalTime end) {
        this.campOperationStart = start;
        this.campOperationEnd = end;
        return this;
    }
}
