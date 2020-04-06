package fr.jervill.springbatch.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor(staticName="of")
@Getter
@ToString
@Table("DIM_PRODUCT")

public class Product {
    @Id
    @Column("ID")
    private Long id;

    @Column("NAME")
    private String name;

    @Column("PRDT_TYPE")
    private String type;

    @Column("EAN_CODE")
    private String eanCode;
}
