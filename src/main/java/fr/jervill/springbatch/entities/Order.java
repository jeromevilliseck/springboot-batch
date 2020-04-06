package fr.jervill.springbatch.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table("FACT_ORDER")
public class Order {
    @Id
    @Column("ID")
    private Long id;

    @Column("PRODUCT_ID")
    private Long productId;

    @Column("EMP_PURCHASER_ID")
    private Long purchaserId;

    @Column("DATE_ID")
    private Long dateId;

    @Column("SUPPLIER_ID")
    private Long supplierId;

    @Column("QUANTITY")
    private Integer quantity;

    @Column("TOTAL_AMOUNT")
    private Double amount;

}
