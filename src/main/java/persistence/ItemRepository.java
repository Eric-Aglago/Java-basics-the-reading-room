package persistence;

import domain.LendingItem;

import java.util.List;

/** Storage contract for lending items, independent of the chosen persistence mechanism. */
public interface ItemRepository {
    void save(LendingItem item);

    List<LendingItem> findAll();
}
