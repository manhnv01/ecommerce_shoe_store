package com.nvm.shoestoreapi.dto.mapper;

import com.nvm.shoestoreapi.dto.response.ProfileResponse;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.entity.Order;
import com.nvm.shoestoreapi.repository.AccountRepository;
import com.nvm.shoestoreapi.repository.CustomerRepository;
import com.nvm.shoestoreapi.repository.OrderRepository;
import com.nvm.shoestoreapi.repository.ProductDetailsRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import static com.nvm.shoestoreapi.util.Constant.formatTime;

@Component
@AllArgsConstructor
public class ProfileMapper {
    final ModelMapper modelMapper;
    final AccountRepository accountRepository;
    final ProductDetailsRepository productDetailsRepository;
    final OrderRepository orderRepository;
    final CustomerRepository customerRepository;

    // OrderMapper
    public ProfileResponse convertToResponse(Customer customer) {
        ProfileResponse response = modelMapper.map(customer, ProfileResponse.class);
        response.setEmail(customer.getAccount().getEmail());
        // Tính tổng tiền đã chi của khách hàng bằng các đơn hàng đã hoàn thành
        Long customerTotalMoney = orderRepository.findByCustomer_Account_EmailAndOrderStatus(customer.getAccount().getEmail(), 3, null).stream()
                .mapToLong(order1 -> order1.getOrderDetails().stream().mapToLong(orderDetails -> {
                    if (orderDetails.getSalePrice() != null) {
                        return orderDetails.getSalePrice() * orderDetails.getQuantity();
                    }
                    return orderDetails.getPrice() * orderDetails.getQuantity();
                }).sum()).sum();

        response.setTotalSpent(customerTotalMoney);

        response.setTotalOrder(orderRepository.countByCustomer_Account_EmailAndOrderStatus(customer.getAccount().getEmail(), 3));
        Order order = orderRepository.findTopByCustomer_Account_EmailOrderByCompletedDateDesc(customer.getAccount().getEmail());

        if (order != null && order.getCompletedDate() != null) {
            response.setLastOrderDate(order.getCompletedDate());
            long lastOrderDate = response.getLastOrderDate().getTime();
            response.setLastOrderText(formatTime(lastOrderDate));
        }
        else {
            response.setLastOrderDate(null);
        }

        return response;
    }
}
