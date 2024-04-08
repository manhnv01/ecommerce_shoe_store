package com.nvm.shoestoreapi.dto.mapper;

import com.nvm.shoestoreapi.dto.request.CartDetailsRequest;
import com.nvm.shoestoreapi.dto.request.CartRequest;
import com.nvm.shoestoreapi.dto.response.*;
import com.nvm.shoestoreapi.entity.*;
import com.nvm.shoestoreapi.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CartMapper {
    final ModelMapper modelMapper;
    final CustomerRepository customerRepository;
    final CartRepository cartRepository;
    final SaleRepository saleRepository;
    final ProductDetailsRepository productDetailsRepository;

    public CartResponse convertToResponse(Cart cart) {
        CartResponse cartResponse = modelMapper.map(cart, CartResponse.class);
        cartResponse.setTotalProduct(cart.getCartDetails().size());

        // sắp xếp theo updatedAt cảu cartDetails
        cartResponse.setCartDetails(cart.getCartDetails().stream()
                .map(this::convertToResponse)
                .sorted((o1, o2) -> (int) (o2.getId() - o1.getId()))
                .collect(Collectors.toList()));
        return cartResponse;
    }

    public Cart convertToEntity(CartRequest cartRequest) {
        Cart cart = modelMapper.map(cartRequest, Cart.class);
        cart.setCustomer(customerRepository.findById(cartRequest.getCustomerId()).orElse(null));
        return cart;
    }

    public CartDetailsResponse convertToResponse(CartDetails cartDetails) {
        CartDetailsResponse cartDetailsResponse = modelMapper.map(cartDetails, CartDetailsResponse.class);
        Product product = cartDetails.getProductDetails().getProductColor().getProduct();

        cartDetailsResponse.setProductDetailsId(cartDetails.getProductDetails().getId());
        cartDetailsResponse.setProductThumbnail(product.getThumbnail());
        cartDetailsResponse.setProductName(product.getName());
        cartDetailsResponse.setProductColor(cartDetails.getProductDetails().getProductColor().getColor());
        cartDetailsResponse.setProductSize(cartDetails.getProductDetails().getSize());
        cartDetailsResponse.setProductPrice(product.getPrice());
        cartDetailsResponse.setProductQuantity(cartDetails.getProductDetails().getQuantity());
        cartDetailsResponse.setTotalPrice(product.getPrice() * cartDetails.getQuantity());
        cartDetailsResponse.setQuantity(cartDetails.getQuantity());
        cartDetailsResponse.setProductSlug(product.getSlug());

        if (product.getSales() != null) {
            List<Sale> activeSales = product.getSales().stream()
                    .filter(sale -> sale.getStartDate().before(new Date()) && sale.getEndDate().after(new Date()))
                    .collect(Collectors.toList());

            if (!activeSales.isEmpty()) {
                Sale activeSale = activeSales.get(0);
                Long salePrice = product.getPrice() - (product.getPrice() * activeSale.getDiscount() / 100);
                cartDetailsResponse.setSalePrice(salePrice);
                cartDetailsResponse.setTotalSalePrice(salePrice * cartDetails.getQuantity());
            }
        }

        return cartDetailsResponse;
    }

    public CartDetails convertToEntity(CartDetailsRequest cartDetailsRequest) {
        CartDetails cartDetails = modelMapper.map(cartDetailsRequest, CartDetails.class);
        cartDetails.setQuantity(cartDetailsRequest.getQuantity());
        cartDetails.setProductDetails(productDetailsRepository.findById(cartDetailsRequest.getProductDetailsId()).orElse(null));
        cartDetails.setCart(Objects.requireNonNull(customerRepository.findByAccount_Email(SecurityContextHolder.getContext().getAuthentication().getName())).getCart());
        return cartDetails;
    }
}