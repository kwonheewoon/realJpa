package jpabook.realjpa.repository;

import jpabook.realjpa.controller.OrderSearch;
import jpabook.realjpa.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch){
        String where = "";
        String where1 = "";

        if(orderSearch.getOrderStatus() != null)
            where = " and o.status =  '"+orderSearch.getOrderStatus()+"'";

        if(!"".equals(orderSearch.getMemberName()) && null != orderSearch.getMemberName())
            where1 = " where m.name like '%"+orderSearch.getMemberName()+"%'";

        return em.createQuery("select o from Order o join o.member m" +
                where1+
                where,Order.class)
                .setFirstResult(0)
                .setMaxResults(1000)
                .getResultList();

    }

    public List<Order> findAllWithMemberDelivery(OrderSearch search){
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit){
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        )
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    public List<Order> findAllWithItem(OrderSearch orderSearch) {
        return em.createQuery("select distinct o from Order o"+
                " join fetch o.member m"+
                " join fetch o.delivery d"+
                " join fetch o.orderItems oi"+
                " join fetch oi.item i", Order.class)
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();
    }
}
