package jpabook.realjpa.repository;

import jpabook.realjpa.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    void save() throws Exception{
        //given
        Member member = new Member();
        member.setName("memberA");

        //when
        Long saveid = memberRepository.save(member);
        Member findMember = memberRepository.findOne(saveid);
        //then
        Assertions.assertEquals(member.getId(),findMember.getId());
        Assertions.assertEquals(member.getName(),findMember.getName());
    }

    @Test
    void find() {
    }
}