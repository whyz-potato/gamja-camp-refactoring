package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.controller.dto.camp.CampUpdateRequestDto;

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
    private double longitude;

    @Column(nullable = false)
    private double latitude;

    @Column
    private LocalTime campOperationStart;

    @Column
    private LocalTime campOperationEnd;

    @OneToMany(mappedBy="camp")
    private List<Room> rooms = new ArrayList<Room>();

    @OneToMany(mappedBy="camp")
    private List<Review> reviews = new ArrayList<>();

    // images
    
    @Builder
    public Camp(Member member, String name, String address, String phone, String campIntroduction, double longitude, double latitude, LocalTime campOperationStart, LocalTime campOperationEnd) {
        this.member = member;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.campIntroduction = campIntroduction;
        this.longitude = longitude;
        this.latitude = latitude;
        this.campOperationStart = campOperationStart;
        this.campOperationEnd = campOperationEnd;
    }

    public Camp update(CampUpdateRequestDto campUpdateRequestDto) {
        this.name = campUpdateRequestDto.getName();
        this.phone = campUpdateRequestDto.getPhone();
        this.campIntroduction = campUpdateRequestDto.getCampIntroduction();
        return this;
    }

    public Camp updateAddress(String address, Coordinate coordinate) {
        this.address = address;
        this.longitude = coordinate.getLongitude();
        this.latitude = coordinate.getLatitude();
        return this;
    }

    public Camp updateOperatingHours(LocalTime start, LocalTime end) {
        this.campOperationStart = start;
        this.campOperationEnd = end;
        return this;
    }
    
    public double getRate(){
        return reviews.stream()
                .mapToInt(r -> r.getRate())
                .average().orElse(0);
    }

}
