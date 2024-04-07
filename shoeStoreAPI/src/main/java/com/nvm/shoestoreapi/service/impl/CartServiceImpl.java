package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.CartMapper;
import com.nvm.shoestoreapi.dto.request.CartDetailsRequest;
import com.nvm.shoestoreapi.dto.response.CartDetailsResponse;
import com.nvm.shoestoreapi.dto.response.CartResponse;
import com.nvm.shoestoreapi.entity.Cart;
import com.nvm.shoestoreapi.entity.CartDetails;
import com.nvm.shoestoreapi.repository.CartDetailsRepository;
import com.nvm.shoestoreapi.repository.CartRepository;
import com.nvm.shoestoreapi.repository.CustomerRepository;
import com.nvm.shoestoreapi.repository.ProductDetailsRepository;
import com.nvm.shoestoreapi.service.CartService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartDetailsRepository cartDetailsRepository;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CartMapper cartMapper;
    private Logger log;

    @Override
    public CartResponse getCartByUserEmail(String email) {
        return cartMapper.convertToResponse(cartRepository.findByCustomerAccountEmail(email));
    }

    @Override
    public CartDetailsResponse addProductToCart(CartDetailsRequest cartDetailsRequest) {
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<CartDetails> cartDetails = cartDetailsRepository
                .findByCartIdAndProductDetailsId(
                        Objects.requireNonNull(customerRepository.findByAccount_Email(
                                        SecurityContextHolder.getContext().getAuthentication().getName()))
                                .getCart().getId(),
                        cartDetailsRequest.getProductDetailsId());
        if (cartDetails.isPresent()) {
            // Nếu quá số lượng sản phẩm trong kho thì chỉ thêm số lượng sản phẩm còn lại
            if (cartDetails.get().getQuantity() + cartDetailsRequest.getQuantity() > cartDetails.get().getProductDetails().getQuantity()) {
                cartDetails.get().setQuantity(cartDetails.get().getProductDetails().getQuantity());
            } else
                // Nếu sản phẩm đã có trong giỏ hàng thì cộng thêm số lượng sản phẩm
                cartDetails.get().setQuantity(cartDetails.get().getQuantity() + cartDetailsRequest.getQuantity());
            return cartMapper.convertToResponse(cartDetailsRepository.save(cartDetails.get()));
        }

        Cart cart = cartRepository.findByCustomerAccountEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        // Giới hạn số lượng sản phẩm trong giỏ là 99 sản phẩm khác nhau
        if (cartDetailsRepository.countByCart_Id(cart.getId()) >= 99) {
            throw new RuntimeException(CART_IS_FULL);
        }

        return cartMapper.convertToResponse(cartDetailsRepository.save(cartMapper.convertToEntity(cartDetailsRequest)));
    }

    @Override
    public CartDetailsResponse updateProductQuantity(CartDetailsRequest cartDetailsRequest) {
        Optional<CartDetails> cartDetails = cartDetailsRepository.findById(cartDetailsRequest.getId());
        if (cartDetails.isPresent()) {
            if (cartDetailsRequest.getQuantity() > cartDetails.get().getProductDetails().getQuantity()) {
                throw new RuntimeException(PRODUCT_QUANTITY_NOT_ENOUGH);
            } else {
                return cartMapper.convertToResponse(cartDetailsRepository.save(cartMapper.convertToEntity(cartDetailsRequest)));
            }
        } else {
            throw new RuntimeException(CART_DETAILS_NOT_FOUND);
        }
    }

    @Override
    public void deleteCartDetails(Long[] cartDetailsId) {
        for (Long id : cartDetailsId) {
            try {
                cartDetailsRepository.deleteById(id);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void deleteCartDetailsById(Long id) {
        if (cartDetailsRepository.existsById(id)) {
            cartDetailsRepository.deleteById(id);
        } else {
            throw new RuntimeException(CART_DETAILS_NOT_FOUND);
        }
    }
}
