package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampUpdateRequest;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "camp")
public class Camp {

    @Id
    @GeneratedValue
    @Column(name = "camp_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
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

    @Column
    private double rate = 0.0;

    @OneToMany(mappedBy = "camp")
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "camp")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "camp")
    private List<Image> images = new ArrayList<>();

    @Builder
    public Camp(Member member, String name, String address, String phone, String campIntroduction, double longitude, double latitude, LocalTime campOperationStart, LocalTime campOperationEnd, List<Image> images) {
        this.member = member;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.campIntroduction = campIntroduction;
        this.longitude = longitude;
        this.latitude = latitude;
        this.campOperationStart = campOperationStart;
        this.campOperationEnd = campOperationEnd;
        this.images = images;
    }

    public Camp update(CampUpdateRequest request) {
        this.name = request.getName();
        this.phone = request.getPhone();
        this.campIntroduction = request.getCampIntroduction();
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

    public Camp updateRate(double rate) {
        this.rate = rate;
        return this;
    }

}
