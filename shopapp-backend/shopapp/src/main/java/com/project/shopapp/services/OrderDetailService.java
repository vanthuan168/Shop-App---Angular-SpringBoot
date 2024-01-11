package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        // Kiem tra orderId co ton tai
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()->
                        new DataNotFoundException("Cannot find order with id = "
                        + orderDetailDTO.getOrderId()));
        // tim product theo id
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()->
                        new DataNotFoundException("Cannot find product with id = "
                                + orderDetailDTO.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(
                ()-> new DataNotFoundException("Cannot find order detail with id = "+ id)
        );
    }

    @Override
    public OrderDetail updateOderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        // Kiểm tra order detail có tồn tại hay không
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find order detail with id = " + id));
        // kiếm tra order
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find order with id = " + orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find product with id = " + orderDetailDTO.getProductId()));

        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        orderDetail.setColor(orderDetail.getColor());
        orderDetail.setOrder(existingOrder);
        orderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
