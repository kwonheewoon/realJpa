package jpabook.realjpa;

import jpabook.realjpa.domain.*;
import jpabook.realjpa.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1(){
            Member member = CreateMember("userA","인천","길거리","123123");
            em.persist(member);

            Book book1 = CreateBook("JPA1 Book", 10000,100);
            em.persist(book1);
            Book book2 = CreateBook("JPA1 Book2", 20000,100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000,1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000,2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery,orderItem1,orderItem2);
            em.persist(order);
        }

        private Member CreateMember(String name,String city, String street, String zipcode){
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book CreateBook(String name, int price, int stockQuantity){
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        public void dbInit2(){
            Member member = CreateMember("userB","서울","길거리","123123");
            em.persist(member);

            Book book1 = CreateBook("Spring Book1", 10000,150);
            em.persist(book1);
            Book book2 = CreateBook("Spring Book2", 20000,200);
            em.persist(book2);


            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000,1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000,2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery,orderItem1,orderItem2);
            em.persist(order);
        }

    }
}
