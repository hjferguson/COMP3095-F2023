package ca.gbc.inventoryservice.bootstrap;

import ca.gbc.inventoryservice.model.Inventory;
import ca.gbc.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor

public class DataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {

        if(inventoryRepository.findBySkuCode("sku_12345").isEmpty()){

            Inventory widgets = Inventory.builder()
                    .skuCode("sku_12345")
                    .quantity(2)
                    .build();

            inventoryRepository.save(widgets);
        }

        if(inventoryRepository.findBySkuCode("sku_67890").isEmpty()){

            Inventory widgets = Inventory.builder()
                    .skuCode("sku_67890")
                    .quantity(0)
                    .build();

            inventoryRepository.save(widgets);
        }

        if(inventoryRepository.findBySkuCode("sku_55555").isEmpty()){

            Inventory widgets = Inventory.builder()
                    .skuCode("sku_55555")
                    .quantity(5)
                    .build();

            inventoryRepository.save(widgets);
        }
    }
}