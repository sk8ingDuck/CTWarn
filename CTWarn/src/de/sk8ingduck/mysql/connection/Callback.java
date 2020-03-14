package de.sk8ingduck.mysql.connection;

public interface Callback<V> {
    void call(V result);
}
