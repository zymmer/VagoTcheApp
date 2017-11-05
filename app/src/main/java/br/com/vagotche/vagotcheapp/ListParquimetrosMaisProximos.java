package br.com.vagotche.vagotcheapp;

/**
 * Created by guilherme on 16/10/17.
 */

public class ListParquimetrosMaisProximos {
    String name;
    Float latLong;

    public void setData(String name, Float latLong) {
        this.name = name;
        this.latLong = latLong;
    }

    public Float getLatLong() {
        return latLong;
    }
}