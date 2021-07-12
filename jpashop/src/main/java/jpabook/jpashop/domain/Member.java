package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    //Order.java의 member가 연관관계의 주인임을 선언(mappedBy)
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
