package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//스프링은 기본적으로 트랜젝션 안에서 진행되어야한다.
//회원 전체조회와 단일조회는 값이 변경될 이유가 없는 Select 형식의 메소드이므로 기본 값으로 readOnly=true를
//할당한다. 하지만 중복회원검사시에는 변경을 해야하므로 따로 다시 선언을 해주도록 하자. default값은 false이다.
@Transactional(readOnly = true)
//@RequiredArgsConstructor는 final이 있는 객체만을 생성자로 만들어주는 Lombok의 기능이다. 아래의 주석처리된
//Autowired 이후의 코드를 @RequiredArgsConstructor가 대신 해줄 수 있다.
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //회원가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); //중복 회원 검사
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원전체조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    //회원단일조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
