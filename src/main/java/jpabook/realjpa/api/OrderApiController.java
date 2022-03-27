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
/*
    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> orderV6(){
        List<OrderFlatDto>flats = orderQueryRepository.findAllByDto_flat();
        return flats.stream()
                .collect(Collectors.groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getStatus(), o.getAddress()),
                        Collectors.mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount(), Collectors.toList())
                        )).e


    }*/

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

            this.orderItems = o.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }

    }

    @Data
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;


        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}
