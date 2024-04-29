package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.ProfileMapper;
import com.nvm.shoestoreapi.dto.request.ProfileRequest;
import com.nvm.shoestoreapi.dto.request.RegisterRequest;
import com.nvm.shoestoreapi.dto.response.ProfileResponse;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Cart;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.repository.AccountRepository;
import com.nvm.shoestoreapi.repository.CartRepository;
import com.nvm.shoestoreapi.repository.CustomerRepository;
import com.nvm.shoestoreapi.repository.RoleRepository;
import com.nvm.shoestoreapi.service.CustomerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public Customer register(RegisterRequest registerRequest) {
        // Kiểm tra xem email đã tồn tại trong hệ thống chưa
        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException(DUPLICATE_EMAIL);
        }

        // Tạo tài khoản mới
        Account account = new Account();
        account.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        account.setRoles(Collections.singletonList(roleRepository.findByName(ROLE_USER)));
        account.setEmail(registerRequest.getEmail());

        // Tạo mã xác nhận ngẫu nhiên và thiết lập thời gian hết hạn
        String randomCode = RandomStringUtils.randomNumeric(6);
        account.setVerificationCode(randomCode);
        account.setEnabled(false);
        account.setAccountNonLocked(true);
        account.setVerificationCodeExpirationDate(new Date(System.currentTimeMillis() + 5 * 60 * 1000));

        // Lưu tài khoản vào cơ sở dữ liệu
        accountRepository.save(account);

        // Tạo khách hàng mới
        Customer customer = new Customer();
        customer.setId(new Date().getTime());
        customer.setName(registerRequest.getName());
        customer.setAccount(account);
        customer.setOrders(Collections.emptyList());

        // Lưu thông tin khách hàng vào cơ sở dữ liệu
        Customer savedCustomer = customerRepository.save(customer);

        // Tạo giỏ hàng mới cho khách hàng
        Cart cart = new Cart();
        cart.setCustomer(savedCustomer);
        cartRepository.save(cart);

        // Trả về thông tin khách hàng đã đăng ký thành công
        return customerRepository.save(savedCustomer);
    }


    @Override
    public ProfileResponse findByEmail(String email) {
        Customer customer = customerRepository.findByAccount_Email(email);
        if (customer == null) {
            throw new RuntimeException(CUSTOMER_NOT_FOUND);
        }
        return profileMapper.convertToResponse(customer);
    }

    @Override
    public Customer updateProfile(ProfileRequest profileRequest) {
        Customer customer = customerRepository.findById(profileRequest.getId())
                .orElseThrow(() -> new RuntimeException(CUSTOMER_NOT_FOUND));

        if (customerRepository.existsByPhone(profileRequest.getPhone())) {
            if (customer.getPhone() != null) {
                if (!customer.getPhone().equals(profileRequest.getPhone())) {
                    throw new RuntimeException(DUPLICATE_PHONE);
                }
            } else {
                throw new RuntimeException(DUPLICATE_PHONE);
            }
        }

        customer.setName(profileRequest.getName());
        customer.setPhone(profileRequest.getPhone());
        customer.setCity(profileRequest.getCity());
        customer.setDistrict(profileRequest.getDistrict());
        customer.setWard(profileRequest.getWard());
        customer.setAddressDetail(profileRequest.getAddressDetail());
        return customerRepository.save(customer);
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> search(String search, Pageable pageable) {
        return customerRepository.findByNameContainingOrPhoneContainingOrAccount_EmailContaining(search, search, search, pageable);
    }

    @Override
    public long count() {
        return customerRepository.count();
    }

    @Override
    public long countByStatus(boolean status) {
        return customerRepository.countByAccount_isAccountNonLocked(status);
    }

    @Override
    public Page<Customer> findByStatus(boolean status, Pageable pageable) {
        return customerRepository.findByAccount_isAccountNonLocked(status, pageable);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        if (customerRepository.existsById(id)) {
            return customerRepository.findById(id);
        }
        throw new RuntimeException(CUSTOMER_NOT_FOUND);
    }
}
