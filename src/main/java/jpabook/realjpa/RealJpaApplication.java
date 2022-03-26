package jpabook.realjpa;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RealJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealJpaApplication.class, args);
    }

    @Bean
    Hibernate5Module hibernate5Module(){

        //jsonignore 로 되어있는 포록시 객체를 NULL 값으로 만들어 주기 오류 발생 제거
        //포스 레이지 로딩 옵션을 true로 주면 lazy 로딩으로 걸려있는 프록시 객체도 같이 조회
        Hibernate5Module hibernate5Module = new Hibernate5Module();
/*
        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING,true);
*/
        return hibernate5Module;
    }
}
