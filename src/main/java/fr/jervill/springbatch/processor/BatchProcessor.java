package fr.jervill.springbatch.processor;

import fr.jervill.springbatch.dto.InputData;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchProcessor implements ItemProcessor<InputData, ConvertedInputData> {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaserRepository purchaserRepository;
}
