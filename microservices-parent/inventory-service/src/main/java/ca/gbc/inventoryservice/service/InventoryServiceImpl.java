package ca.gbc.inventoryservice.service;

import ca.gbc.inventoryservice.dto.InventoryRequest;
import ca.gbc.inventoryservice.dto.InventoryResponse;
import ca.gbc.inventoryservice.repository.InventoryRepository;
import ca.gbc.inventoryservice.model.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<InventoryRequest> requests) {

        // Testing purposes only
//        log.info("Wait started");
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//
//            Thread.currentThread().interrupt();
//            log.error("Thread was interrupted", e);
//        }
//        log.info("Wait ended");
        List<Inventory> availableInventory = inventoryRepository.findAllByInventoryRequests(requests);

        return requests.stream().map(request -> {
            boolean isInStock = availableInventory.stream()
                    .anyMatch(inventory -> inventory.getSkuCode().equals(request.getSkuCode())
                    && inventory.getQuantity() >= request.getQuantity());

            if(isInStock){
                return InventoryResponse.builder()
                        .skuCode(request.getSkuCode())
                        .sufficientStock(true)
                        .build();
            }else{
                return InventoryResponse.builder()
                        .skuCode(request.getSkuCode())
                        .sufficientStock(false)
                        .build();
            }
        }).toList();

    }
}
