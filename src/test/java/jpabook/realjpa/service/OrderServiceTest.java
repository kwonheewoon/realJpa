package jpabook.realjpa.service;

import jpabook.realjpa.domain.*;
import jpabook.realjpa.domain.item.Book;
import jpabook.realjpa.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void order()throws Exception{
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("책이름 우우우웅");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        Order getOrder = orderRepository.findOne(orderId);

        //상품 주문시 상태는 order
        assertEquals( OrderStatus.ORDER, getOrder.getStatus());
        //주문한 상품 종류 개수가 정확해야 한다.
        assertEquals( 1, getOrder.getOrderItems().size());
        //주문 가격은 가격 * 수량이다
        assertEquals(10000*orderCount, getOrder.getTotalPrice());
        //주문 수량만큼 재고가 줄어야 한다.
        assertEquals(8,book.getStockQuantity());
    }

    @Test
    public void cancel()throws Exception{
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("책이름 우우우웅");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(),book.getId(),orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals(10, book.getStockQuantity());
    }

    @Test
    public void order_itemOver()throws Exception{
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("책이름 우우우웅");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 11;

        try{
            orderService.order(member.getId(), book.getId(), orderCount);
        }catch (IllegalArgumentException e){
            e.getMessage();
        }


        fail("재고수량 좆됨 시발");
    }
}