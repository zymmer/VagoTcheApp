package br.com.vagotche.vagotcheapp;

import java.util.List;

/**
 * Created by guilherme on 07/10/17.
 */

public class Placa {
    private List<Placa> placas;

    public Placa() {
        super();
    }
    public Placa(List<Placa> placas) {
        super();
        this.placas = placas;
    }

    public List<Placa> getPlacas() {
        return placas;
    }
    public void setPlacas(List<Placa> placas) {
        this.placas = placas;
    }
}
