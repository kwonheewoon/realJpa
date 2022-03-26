package jpabook.realjpa.controller;

import jpabook.realjpa.domain.Item;
import jpabook.realjpa.domain.Member;
import jpabook.realjpa.domain.Order;
import jpabook.realjpa.service.ItemService;
import jpabook.realjpa.service.MemberService;
import jpabook.realjpa.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){
        List<Member> members = memberService.findMember();
        List<Item> items = itemService.findItems();

        model.addAttribute("members",members);
        model.addAttribute("items",items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@ModelAttribute OrderForm form){
        orderService.order(form.getMemberId(), form.getItemId(), form.getCount());

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String list(Model model,@ModelAttribute("orderSearch") OrderSearch orderSearch){
        model.addAttribute("orderSearch",orderSearch);
        List<Order> list = orderService.orderList(orderSearch);
        model.addAttribute("orders",list);
        System.out.println("=================="+list.stream().map(a -> a.getOrderItems().size()).count());
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancel(@PathVariable Long orderId){

        orderService.cancelOrder(orderId);

        return "redirect:/orders";
    }

}
