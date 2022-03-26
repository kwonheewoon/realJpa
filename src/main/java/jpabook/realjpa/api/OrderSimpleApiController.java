package jpabook.realjpa.api;

import jpabook.realjpa.controller.OrderSearch;
import jpabook.realjpa.domain.Address;
import jpabook.realjpa.domain.Order;
import jpabook.realjpa.domain.OrderStatus;
import jpabook.realjpa.repository.OrderRepository;
import jpabook.realjpa.repository.OrderSimpleQueryDto;
import jpabook.realjpa.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1(){
        List<Order> orderList = orderRepository.findAll(new OrderSearch());
        for(Order order : orderList){
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return orderList;
    }

    @GetMapping("/api/v2/simple-orders")
    public Result<SimpleOrderDto> orderV2(){
        List<SimpleOrderDto> orderList = orderRepository.findAll(new OrderSearch())
                .stream()
                .map(o -> new SimpleOrderDto(o.getMember().getId(), o.getMember().getName(), o.getOrderDate(), o.getStatus(), o.getDelivery().getAddress()))
                .collect(Collectors.toList());

        return new Result<SimpleOrderDto>(orderList.size(),orderList);
    }

    @GetMapping("/api/v3/simple-orders")
    public Result<SimpleOrderDto> orderV3(){
        List<SimpleOrderDto> orderList = orderRepository.findAllWithMemberDelivery(new OrderSearch())
                .stream()
                .map(o -> new SimpleOrderDto(o.getMember().getId(), o.getMember().getName(), o.getOrderDate(), o.getStatus(), o.getDelivery().getAddress()))
                .collect(Collectors.toList());

        return new Result<SimpleOrderDto>(orderList.size(),orderList);
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4(){
        return orderSimpleQueryRepository.findOrderDtos(new OrderSearch());
    }

    @Data
    @AllArgsConstructor
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        int count;
        List<T> data;
    }
}
