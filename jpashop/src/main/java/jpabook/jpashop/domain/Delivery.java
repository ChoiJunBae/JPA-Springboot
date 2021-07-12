package jpabook.jpashop.domain;

import jdk.jfr.StackTrace;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name="delivery_id")
    private  Long id;

    //order와의 1대1 관계에서 order 테이블 쪽이 FK를 가져갔기 때문에
    //mappedBy로 join 해놓은것을 잡아야함
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    // 값을 어떻게 넘겨줄제 정하는 Enumerated이다.
    // 기본값은 ORDINAL(숫자)이지만 배송준비랑 배송 사이에 어떠한 값이 들어온다면
    // 배송의 숫자가 밀리면서 골치 아파진다. 따라서 ORDINAL이 아닌 STRING을 사용한다.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY(배송준비), COMP(배송)
}
