package jpabook.realjpa.service;

import jpabook.realjpa.domain.Member;
import jpabook.realjpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(false)
    void join() {
        Member member = new Member();
        member.setName("kokokooo");

        Long saveId = memberRepository.save(member);

        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    void dupMember() {
        Member member1 = new Member();
        member1.setName("kikikim1");

        Member member2 = new Member();
        member2.setName("kikikim2");

        memberService.join(member1);
        try{
            memberService.join(member2); //예외 발생
        }catch (IllegalArgumentException e){
            e.getMessage();
        }


        fail("예외가 발생됨");
    }
}