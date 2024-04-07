package com.nvm.shoestoreapi.dto.mapper;

import com.nvm.shoestoreapi.dto.request.OrderDetailsRequest;
import com.nvm.shoestoreapi.dto.request.OrderRequest;
import com.nvm.shoestoreapi.dto.response.OrderDetailsResponse;
import com.nvm.shoestoreapi.dto.response.OrderResponse;
import com.nvm.shoestoreapi.entity.*;
import com.nvm.shoestoreapi.repository.CustomerRepository;
import com.nvm.shoestoreapi.repository.EmployeeRepository;
import com.nvm.shoestoreapi.repository.OrderRepository;
import com.nvm.shoestoreapi.repository.ProductDetailsRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.nvm.shoestoreapi.util.Constant.*;

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

        Product product = orderDetails.getProductDetails().getProductColor().getProduct();

        orderDetailsResponse.setProductDetailsId(orderDetails.getProductDetails().getId());
        orderDetailsResponse.setProductThumbnail(product.getThumbnail());
        orderDetailsResponse.setProductName(product.getName());
        orderDetailsResponse.setProductColor(orderDetails.getProductDetails().getProductColor().getColor());
        orderDetailsResponse.setProductSize(orderDetails.getProductDetails().getSize());
        orderDetailsResponse.setProductPrice(product.getPrice());
        orderDetailsResponse.setTotalPrice(product.getPrice() * orderDetails.getQuantity());
        orderDetailsResponse.setQuantity(orderDetails.getQuantity());
        orderDetailsResponse.setProductSlug(product.getSlug());

        return orderDetailsResponse;
    }

    public OrderDetails convertToEntity(OrderDetailsRequest orderDetailsRequest) {
        OrderDetails orderDetails = modelMapper.map(orderDetailsRequest, OrderDetails.class);

        ProductDetails productDetails = productDetailsRepository.findById(orderDetailsRequest.getProductDetailsId())
                .orElseThrow(() -> new RuntimeException(PRODUCT_DETAILS_NOT_FOUND));

        Product product = productDetails.getProductColor().getProduct();

        if (product.getSales() != null) {
            List<Sale> activeSales = product.getSales().stream()
                    .filter(sale -> sale.getStartDate().before(new Date()) && sale.getEndDate().after(new Date()))
                    .collect(Collectors.toList());

            if (!activeSales.isEmpty()) {
                Sale activeSale = activeSales.get(0);
                Long salePrice = product.getPrice() - (product.getPrice() * activeSale.getDiscount() / 100);
                orderDetails.setSalePrice(salePrice);
            }
        }

        orderDetails.setProductDetails(productDetails);
        return orderDetails;
    }
}
