package jpabook.realjpa.service;

import jpabook.realjpa.domain.Member;
import jpabook.realjpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.metamodel.model.domain.internal.MapMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;


    //회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); //중복회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    //readOnly true 설정시 읽기전용 트랜잭션이므로 속도 최적화 가능
    public List<Member> findMember(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
