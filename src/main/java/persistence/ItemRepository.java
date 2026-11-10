package persistence;

import domain.LendingItem;

import java.util.List;

public interface ItemRepository {
    void save(LendingItem item);

    List<LendingItem> findAll();
}
