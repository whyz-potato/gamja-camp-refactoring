package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ordinary_price")
public class OrdinaryPrice {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Integer weekPrice;

    @Column
    private Integer weekendPrice;

}
