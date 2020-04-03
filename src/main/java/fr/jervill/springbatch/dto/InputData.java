package fr.jervill.springbatch.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class InputData {
    private String productName;

    private String productType;

    private String productEanCode;

    private Double productAmount;

    private Integer productQuantity;

    private String purchaserFirstName;

    private String purchaserLastName;

    private String purchaserEmail;

    private String supplierName;

    private String supplierAddress;

    private Date transactionDate;
}
