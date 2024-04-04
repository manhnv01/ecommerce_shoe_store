package com.nvm.shoestoreapi.dto.mapper;

import com.nvm.shoestoreapi.dto.request.OrderDetailsRequest;
import com.nvm.shoestoreapi.dto.request.OrderRequest;
import com.nvm.shoestoreapi.dto.response.OrderDetailsResponse;
import com.nvm.shoestoreapi.dto.response.OrderResponse;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.entity.Order;
import com.nvm.shoestoreapi.entity.OrderDetails;
import com.nvm.shoestoreapi.repository.CustomerRepository;
import com.nvm.shoestoreapi.repository.EmployeeRepository;
import com.nvm.shoestoreapi.repository.OrderRepository;
import com.nvm.shoestoreapi.repository.ProductDetailsRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.nvm.shoestoreapi.util.Constant.ROLE_ADMIN;
import static com.nvm.shoestoreapi.util.Constant.ROLE_EMPLOYEE;

@Component
@AllArgsConstructor
public class OrderMapper {
    final ModelMapper modelMapper;
    final ProductDetailsRepository productDetailsRepository;
    final OrderRepository orderRepository;
    final EmployeeRepository employeeRepository;
    final CustomerRepository customerRepository;

    // OrderMapper
    public OrderResponse convertToResponse(Order order) {
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        if (Objects.nonNull(order.getCustomer().getAccount()))
            orderResponse.setEmail(order.getCustomer().getAccount().getEmail());
        orderResponse.getOrderDetails().clear();
        order.getOrderDetails().forEach(orderDetails -> orderResponse.getOrderDetails().add(convertToResponse(orderDetails)));
        return orderResponse;
    }

    public Order convertToEntity(OrderRequest orderRequest) {
        Order order = modelMapper.map(orderRequest, Order.class);
        order.getOrderDetails().clear();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(authority -> {
            if (authority.getAuthority().equals(ROLE_ADMIN) || authority.getAuthority().equals(ROLE_EMPLOYEE)) {
                Customer customer = customerRepository.findByPhone(orderRequest.getPhone());
                if (customer == null) {
                    customer = new Customer();
                    customer.setPhone(orderRequest.getPhone());
//                    customer.setFullname(orderRequest.getFullname());
//                    customer.setAddress(orderRequest.getAddress());
                    customerRepository.save(customer);
                }
                order.setCustomer(customer);
                order.setEmployee(employeeRepository.findByAccount_Email(authentication.getName()));
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                order.setCustomer(customerRepository.findByAccount_Email(authentication.getName()));
            }
        });
        return order;
    }

    // OrderDetailsMapper
    public OrderDetailsResponse convertToResponse(OrderDetails orderDetails) {
        OrderDetailsResponse orderDetailsResponse = modelMapper.map(orderDetails, OrderDetailsResponse.class);
//        orderDetailsResponse.setProductName(orderDetails.getProductDetails().getProduct().getName());
//        orderDetailsResponse.setProductColor(orderDetails.getProductDetails().getColor());
//        orderDetailsResponse.setProductPrice(orderDetails.getProductDetails().getProduct().getPrice());
//        orderDetailsResponse.setProductThumbnail(orderDetails.getProductDetails().getProduct().getThumbnail());
        return orderDetailsResponse;
    }

    public OrderDetails convertToEntity(OrderDetailsRequest orderDetailsRequest) {
        OrderDetails orderDetails = modelMapper.map(orderDetailsRequest, OrderDetails.class);
        orderDetails.setProductDetails(productDetailsRepository.findById(orderDetailsRequest.getProductDetailsId()).orElse(null));
        return orderDetails;
    }
}
