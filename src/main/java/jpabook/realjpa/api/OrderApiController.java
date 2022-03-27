package jpabook.realjpa.api;

import jpabook.realjpa.controller.OrderSearch;
import jpabook.realjpa.domain.Address;
import jpabook.realjpa.domain.Order;
import jpabook.realjpa.domain.OrderItem;
import jpabook.realjpa.repository.OrderRepository;
import jpabook.realjpa.repository.order.query.OrderFlatDto;
import jpabook.realjpa.repository.order.query.OrderItemQueryDto;
import jpabook.realjpa.repository.order.query.OrderQueryDto;
import jpabook.realjpa.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> orderV1(){
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for (Order o :all ) {
            o.getMember().getName();
            o.getDelivery().getAddress();
            List<OrderItem> orderItems = o.getOrderItems();
            orderItems.stream().forEach(al -> al.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2(){
        List<Order> all = orderRepository.findAll(new OrderSearch());
        List<OrderDto> orderDtoList = all.stream()
                .map(dto -> new OrderDto(dto))
                .collect(Collectors.toList());
        return orderDtoList;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3(){
        List<Order> all = orderRepository.findAllWithItem(new OrderSearch());
        List<OrderDto> orderDtoList = all.stream()
                .map(dto -> new OrderDto(dto))
                .collect(Collectors.toList());
        return orderDtoList;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "limit", defaultValue = "100") int limit){
        List<Order> all = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> orderDtoList = all.stream()
                .map(dto -> new OrderDto(dto))
                .collect(Collectors.toList());
        return orderDtoList;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> orderV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> orderV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> orderV6(){
        List<OrderFlatDto>flats = orderQueryRepository.findAllByDto_flat();
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());


    }

    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order o){
            this.orderId = o.getId();
            this.name = o.getMember().getName();
            this.orderDate = o.getOrderDate();
            this.address = o.getDelivery().getAddress();

            System.out.println("*************************************************");
            this.orderItems = o.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
            System.out.println("*************************************************");
        }

    }

    @Data
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;


        public OrderItemDto(OrderItem orderItem) {
            System.out.println("=================================================================================================");
            this.itemName = orderItem.getItem().getName();
            System.out.println("=================================================================================================");
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}
