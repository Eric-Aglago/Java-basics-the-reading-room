package persistence;

import domain.LendingItem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryItemRepository implements ItemRepository {
    private final Map<Integer, LendingItem> items = new LinkedHashMap<>();

    @Override
    public void save(LendingItem item) {
        items.put(item.getId(), item);
    }

    @Override
    public List<LendingItem> findAll() {
        return List.copyOf(items.values());
    }
}
