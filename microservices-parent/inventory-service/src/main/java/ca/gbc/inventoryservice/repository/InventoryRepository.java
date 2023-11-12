package ca.gbc.inventoryservice.repository;

import ca.gbc.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findBySkuCode(String skuCode);

    List<Inventory> findAllBySkuCodeIn(List<String> skuCodes);

    //if you are trying to execute a unique query... you'll have to assist JPA by authoring it
    @Query("SELECT i FROM Inventory i WHERE i.skuCode = :skuCode AND i.quantity >= :quantity")
    List<Inventory> findBySkuCodeAndQuantity(String skuCode, Integer quantity);

    default List<Inventory> findAllByInventoryRequests(List<Inventory> inventoryRequests){
        return inventoryRequests.stream()
                .flatMap(request -> findBySkuCodeAndQuantity(request.getSkuCode(), request.getQuantity()).stream())
                .collect(Collectors.toList());
    }

}
