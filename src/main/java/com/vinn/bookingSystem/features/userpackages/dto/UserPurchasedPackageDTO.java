package com.vinn.bookingSystem.features.userpackages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserPurchasedPackageDTO {
    private Long userPackageId;
    private String packageName;
    private BigDecimal price;
    private Integer totalCredits;
    private Integer remainingCredits;
    private String status;
    private String purchaseDate;
    private String expiryDate;
}
