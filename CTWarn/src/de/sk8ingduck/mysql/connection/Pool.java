package de.sk8ingduck.mysql.connection;

public interface Pool<PooledType> {

    PooledType checkOut();

    void checkIn(PooledType instance);

    int getPoolSize();
}