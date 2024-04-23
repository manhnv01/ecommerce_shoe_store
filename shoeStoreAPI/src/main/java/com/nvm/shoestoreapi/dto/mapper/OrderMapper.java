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
        orderResponse.setCustomerId(order.getCustomer().getId());
        orderResponse.setCustomerName(order.getCustomer().getName());
        orderResponse.setCustomerEmail(order.getCustomer().getAccount().getEmail());
        orderResponse.setCustomerCreatedDate(order.getCustomer().getCreatedAt().toString());
        orderResponse.setCustomerTotalOrder(orderRepository.countByCustomer_Account_EmailAndOrderStatus(order.getCustomer().getAccount().getEmail(), 3));

        // Tính tổng tiền đã chi của khách hàng bằng các đơn hàng đã hoàn thành
        Long customerTotalMoney = orderRepository.findByCustomer_Account_EmailAndOrderStatus(order.getCustomer().getAccount().getEmail(), 5, null).stream()
                .mapToLong(order1 -> order1.getOrderDetails().stream().mapToLong(orderDetails -> {
                    if (orderDetails.getSalePrice() != null) {
                        return orderDetails.getSalePrice() * orderDetails.getQuantity();
                    }
                    return orderDetails.getPrice() * orderDetails.getQuantity();
                }).sum()).sum();
        orderResponse.setCustomerTotalMoney(customerTotalMoney);

        // Tính tổng tiền và tổng số lượng sản phẩm trong đơn hàng

        // Kiểm tra salePrice có null không nếu có thì lấy price, nếu không thì lấy salePrice
        Long totalSaleMoney = order.getOrderDetails().stream().mapToLong(orderDetails -> {
            if (orderDetails.getSalePrice() != null) {
                return orderDetails.getSalePrice() * orderDetails.getQuantity();
            }
            return orderDetails.getPrice() * orderDetails.getQuantity();
        }).sum();
        orderResponse.setTotalSaleMoney(totalSaleMoney);

        Long totalMoney = order.getOrderDetails().stream().mapToLong(orderDetails -> orderDetails.getPrice() * orderDetails.getQuantity()).sum();
        orderResponse.setTotalMoney(totalMoney);

        Long totalDiscount = totalMoney - totalSaleMoney;
        orderResponse.setTotalDiscount(totalDiscount);

        Long totalQuantity = order.getOrderDetails().stream().mapToLong(OrderDetails::getQuantity).sum();
        orderResponse.setTotalQuantity(totalQuantity);

        orderResponse.setCarrier_name(order.getCarrier_name());
        orderResponse.setCarrier_logo(order.getCarrier_logo());
        orderResponse.setService(order.getService());
        orderResponse.setTotal_fee(order.getTotal_fee());

        // thông tin nhân viên xác nhận đơn
        if (Objects.nonNull(order.getEmployee())) {
            orderResponse.setEmployeeId(order.getEmployee().getId());
            orderResponse.setEmployeeName(order.getEmployee().getName());
        }

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
                    customerRepository.save(customer);
                }
                order.setCustomer(customer);
                order.setEmployee(employeeRepository.findByAccount_Email(authentication.getName()));
            } else if (authority.getAuthority().equals(ROLE_USER)) {
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
        orderDetailsResponse.setProductId(product.getId());
        orderDetailsResponse.setProductName(product.getName());
        orderDetailsResponse.setProductColor(orderDetails.getProductDetails().getProductColor().getColor());
        orderDetailsResponse.setProductSize(orderDetails.getProductDetails().getSize());
        orderDetailsResponse.setProductPrice(product.getPrice());
        orderDetailsResponse.setQuantity(orderDetails.getQuantity());
        orderDetailsResponse.setProductSlug(product.getSlug());

        orderDetailsResponse.setTotalPrice(product.getPrice() * orderDetails.getQuantity());

        if (orderDetails.getSalePrice() != null) {
            orderDetailsResponse.setSalePrice(orderDetails.getSalePrice());
            orderDetailsResponse.setTotalPrice(orderDetails.getSalePrice() * orderDetails.getQuantity());
        }

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
