package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

import java.util.List;

import static com.nvm.shoestoreapi.util.Constant.NAME_MAX_LENGTH_30;
import static com.nvm.shoestoreapi.util.Constant.NAME_NOT_BLANK;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductRequest {
    private Long id;
    private String status; // ( RETURN_PENDING: chờ xử lý;  RETURN_APPROVED: đã xử lý;  RETURN_REJECTED:đã từ chối)
    private String reason;
    private Long orderId;
    private List<ReturnProductDetailsRequest> returnProductDetails;
}
