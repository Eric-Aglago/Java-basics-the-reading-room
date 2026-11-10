package persistence;

import domain.LendingItem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * This implementation keeps records only for the current process. Insertion
 * order makes item reports deterministic; a durable repository can replace it.
 */
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
