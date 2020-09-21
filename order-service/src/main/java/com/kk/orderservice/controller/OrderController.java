package com.kk.orderservice.controller;

import com.kk.orderservice.dto.OrderDto;
import com.kk.orderservice.exception.OrderExistsException;
import com.kk.orderservice.exception.OrderNotFoundException;
import com.kk.orderservice.model.Order;
import com.kk.orderservice.service.MessageProducerService;
import com.kk.orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private MessageProducerService producer;

    @Autowired
    public OrderController(OrderService orderService, ModelMapper modelMapper, MessageProducerService producer) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.producer = producer;
    }

    /**
     * REST Endpoint for creating new Order
     * URI: /api/v1/orders  METHOD: POST
     * Response status: success: 201(created) , Order already exists : 409(conflict)
     */
    @PostMapping("v1/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto addNewOrder(@RequestBody OrderDto orderDto) throws OrderExistsException {
        Order newOrder = orderService.addNewOrder(convertToOrderEntity(orderDto));
        OrderDto newOrderDto = convertToOrderDto(newOrder);
        //**TODO**
        // Use the MessageProducer bean to send message to Kafka after successful creation of Order
        producer.sendOrderMessage(newOrderDto);
        return newOrderDto;
    }

    /**
     * REST Endpoint for fetching orders based on order id
     * URI: /api/v1/orders/{orderId}  METHOD: GET
     * Response status: success: 200(success) , Order Not Found : 404(NotFound)
     */
    @GetMapping("v1/orders/{orderId}")
    public OrderDto getOrderById(@PathVariable String orderId) throws OrderNotFoundException {
        return convertToOrderDto(orderService.getOrderByOrderId(orderId));
    }

    /**
     * Method for converting OrderDto entity to Order entity object
     */
    private Order convertToOrderEntity(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }

    /**
     * Method for converting Order entity to OrderDto object
     */
    private OrderDto convertToOrderDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

}
