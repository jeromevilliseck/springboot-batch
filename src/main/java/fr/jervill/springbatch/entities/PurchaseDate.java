package fr.jervill.springbatch.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Builder
@AllArgsConstructor(staticName="of")
@Getter
@ToString
@Table("DIM_DATE")
public class PurchaseDate {
    @Id
    @Column("ID")
    private Long id;

    @Column("DATE_TIME")
    private Date date;
}
