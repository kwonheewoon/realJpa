package jpabook.realjpa.repository.order.simplequery;

import jpabook.realjpa.controller.OrderSearch;
import jpabook.realjpa.repository.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos(OrderSearch orderSearch){
        return em.createQuery("select new jpabook.realjpa.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o"
                +" join o.member m"+
                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
