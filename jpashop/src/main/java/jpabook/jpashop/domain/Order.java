package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//orders에 있는 Member의 FK 연관관계의 주인은
//FK로 가지고 있는 테이블을 주인으로 설정하면된다.
//Member에서도 order를 List로 가지고 있기 때문에
//JPA는 어떤 것을 주로 설정하고 써야할지 모른다.
//따라서 Order테이블이 주인으로 설정하고 시작하면됨

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    //아래의 Member가 연관관계의 주인이 되며 Member.java의 List<order>가 참조한다.
    private Member member;

    // 원래는 Order가 persist 되어서 값이 저장될 때 OrderItem의 값도 개개인 별로 차례차례 persist 된다.
    // 하지만 CasCadeType.ALL을 해주면 Order와 같이 한꺼번에 값을 Persist 한다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //delivery와 Order는 1대1 관계이므로 어떤 테이블에서 FK을 가지고 있던지 상관이 없다.
    //Order에서 FK를 가지기로 선택했다.
    //이 곳 또한 CascadeType이 선언되었기 때문에 Order 호출 시에 OrderItem과 같이 호출된다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    //전에는 date를 사용했지만 LocalDataTime을 사용하면 hivernate가 자동으로 생성해준다.
    private LocalDateTime orderData;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태 (ORDER, CANCEL)

    //==연관관계 메서드==//
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderData(LocalDateTime.now());
        return order;
    }

    //==비지니스 로직==//
    //주문취소
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    //전체 주문 가격 조회
    public int getTotalPrice(){
        int totalPrice = 0 ;
        for(OrderItem orderItem: orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
