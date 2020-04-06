package fr.jervill.springbatch.entitiesrepository;

import fr.jervill.springbatch.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
