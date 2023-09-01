package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import whyzpotato.gamjacamp.domain.member.Member;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reservation extends BaseTimeEntity {


    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Min(1)
    private int numGuest;

    @Column(nullable = false)
    private LocalDate stayStarts;

    @Column(nullable = false)
    private LocalDate stayEnds;

    @Min(1)
    private int price;

    public Reservation(Member member, Camp camp, Room room, int numGuest, LocalDate stayStarts, LocalDate stayEnds, List<Integer> prices) {
        this.member = member;
        this.camp = camp;
        this.room = room;
        this.numGuest = numGuest;
        this.stayStarts = stayStarts;
        this.stayEnds = stayEnds;
        this.price = prices.stream().mapToInt(p -> p).sum();
    }

    public static ReservationBuilder builder() {
        return new ReservationBuilder();
    }

    public void cancel(Member requester) {
        if (requester.equals(member)) {
            if (LocalDate.now().isAfter(this.stayStarts.minusDays(3)))
                throw new IllegalStateException("방문일이 3일 이내인 예약은 취소할 수 없습니다.");
        } else if (!requester.equals(camp.getMember())) {
            throw new IllegalStateException();
        }
        this.status = ReservationStatus.CANCELED;
    }

    public void confirm(Member requester){
        if(requester.equals(camp.getMember()))
            this.status = ReservationStatus.BOOKED;
        else
            throw new IllegalStateException();
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }

    public static class ReservationBuilder {
        private Member member;
        private Camp camp;
        private Room room;
        private int numGuest;
        private LocalDate stayStarts;
        private LocalDate stayEnds;
        private List<Integer> prices;

        ReservationBuilder() {
        }

        public ReservationBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public ReservationBuilder camp(Camp camp) {
            this.camp = camp;
            return this;
        }

        public ReservationBuilder room(Room room) {
            this.room = room;
            return this;
        }

        public ReservationBuilder numGuest(int numGuest) {
            this.numGuest = numGuest;
            return this;
        }

        public ReservationBuilder stayStarts(LocalDate stayStarts) {
            this.stayStarts = stayStarts;
            return this;
        }

        public ReservationBuilder stayEnds(LocalDate stayEnds) {
            this.stayEnds = stayEnds;
            return this;
        }

        public ReservationBuilder prices(List<Integer> prices) {
            this.prices = prices;
            return this;
        }

        public Reservation build() {

            if (!camp.getId().equals(room.getCamp().getId()) || prices.isEmpty() || room.getCapacity() < numGuest)
                throw new IllegalArgumentException("잘못된 접근입니다.");
            if (!room.getPrices(stayStarts, stayEnds).containsAll(prices))
                throw new IllegalStateException("변경된 정보가 있습니다.");

            return new Reservation(member, camp, room, numGuest, stayStarts, stayEnds, prices);

        }

        public String toString() {
            return "Reservation.ReservationBuilder(member=" + this.member + ", camp=" + this.camp + ", room=" + this.room + ", numGuest=" + this.numGuest + ", stayStarts=" + this.stayStarts + ", stayEnds=" + this.stayEnds + ", prices=" + this.prices + ")";
        }
    }

}
