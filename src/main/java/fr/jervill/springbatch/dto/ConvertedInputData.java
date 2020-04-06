package fr.jervill.springbatch.dto;

import fr.jervill.springbatch.entities.*;
import lombok.Data;

@Data
public class ConvertedInputData {
    private Supplier supplier;

    private Purchaser purchaser;

    private Product product;

    private PurchaseDate purchaseDate;

    private Order order;
}
