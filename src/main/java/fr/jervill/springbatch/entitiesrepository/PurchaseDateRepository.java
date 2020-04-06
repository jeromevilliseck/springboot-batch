package fr.jervill.springbatch.entitiesrepository;

import fr.jervill.springbatch.entities.PurchaseDate;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface PurchaseDateRepository extends CrudRepository<PurchaseDate, Long> {
    @Query("select * from DIM_DATE where DATE_TIME = :date ")
    public PurchaseDate findByDate(@Param("date") Date date);
}
