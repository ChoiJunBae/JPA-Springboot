package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

//@Repository는 가장 상단 부모인 Application이 실행시에 context를 먼저 확인하여 bean으로 등록하는데
//그때 bean으로 등록해주기 위해서는 @Repository을 선언해주어야함
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //스프링이 생성해준 EntityManager를 @PersistenceContext가 있다면 알아서 생성된 em을 주입해준다.
//    @PersistenceContext
    // 스프링데이터JPA를 사용하면 persistence말고 Autowired를 사용해도 무관하다.
//    @Autowired
    // 하지만 MemberService에서 설명했듯이 그럴 필요 없이 Final로 선언후에 @RequiredArgsConstructor을
    // 쓰는 것이 최근 트랜드이다.
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
