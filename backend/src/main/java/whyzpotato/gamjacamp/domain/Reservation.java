package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reservation extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private int numGuest;

    private LocalDate stayStarts;

    private LocalDate stayEnds;

    private int price;

    @Builder
    public Reservation(Member member, Camp camp, Room room, int numGuest, LocalDate stayStarts, LocalDate stayEnds) {
        this.member = member;
        this.camp = camp;
        this.room = room;
        this.numGuest = numGuest;
        this.stayStarts = stayStarts;
        this.stayEnds = stayEnds;
        this.status = ReservationStatus.PENDING;
        this.price = room.getPrices(stayStarts, stayEnds).stream().mapToInt(Integer::intValue).sum();
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELED;
    }

    public void confirm() {
        this.status = ReservationStatus.BOOKED;
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }

}
