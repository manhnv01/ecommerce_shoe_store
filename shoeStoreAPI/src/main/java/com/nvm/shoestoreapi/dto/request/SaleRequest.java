package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

import static com.nvm.shoestoreapi.util.Constant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleRequest {
    private Long id;
    @NotBlank(message = NAME_NOT_BLANK)
    @Length(max = 50, message = NAME_MAX_LENGTH_50)
    private String name;
    @Min(value = 1, message = DISCOUNT_MUST_BE_GREATER_THAN_0)
    @Max(value = 100, message = DISCOUNT_MUST_BE_LESS_THAN_100)
    private int discount;
    private Date startDate;
    private Date endDate;
    private List<Long> productIds;
}
