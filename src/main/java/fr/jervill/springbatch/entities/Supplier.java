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
@Table("DIM_SUPPLIER")
public class Supplier {
    @Id
    @Column("ID")
    private Long id;

    @Column("NAME")
    private String name;

    @Column("ADDRESS")
    private String address;
}
