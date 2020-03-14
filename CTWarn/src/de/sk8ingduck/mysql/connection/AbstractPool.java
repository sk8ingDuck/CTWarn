package de.sk8ingduck.mysql.connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractPool<PooledType> implements Pool<PooledType> {

    private final Map<PooledType, Boolean> currentPool;

    public AbstractPool() {
        this.currentPool = new ConcurrentHashMap<>();
    }

    public abstract PooledType create();

    @Override
    public PooledType checkOut() {
        PooledType instance;

        if (currentPool.size() == 0) {
            instance = this.create();
        } else {
            instance = this.currentPool.entrySet().stream()
                    .filter(entry -> !entry.getValue())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
        }

        if (instance == null) instance = this.create();

        this.currentPool.put(instance, true);

        return instance;
    }

    @Override
    public void checkIn(PooledType instance) {
        this.currentPool.put(instance, false);
    }

    @Override
    public int getPoolSize() {
        return this.currentPool.size();
    }
}