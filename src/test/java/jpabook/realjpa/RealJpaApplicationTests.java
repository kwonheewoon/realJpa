package jpabook.realjpa;

import jpabook.realjpa.repository.OrderRepository;
import jpabook.realjpa.repository.order.query.OrderFlatDto;
import jpabook.realjpa.repository.order.query.OrderItemQueryDto;
import jpabook.realjpa.repository.order.query.OrderQueryDto;
import jpabook.realjpa.repository.order.query.OrderQueryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

@SpringBootTest
class RealJpaApplicationTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderQueryRepository orderQueryRepository;

    @Test
    void contextLoads() {

        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
        flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList()).forEach(System.out::println);
    }

}
