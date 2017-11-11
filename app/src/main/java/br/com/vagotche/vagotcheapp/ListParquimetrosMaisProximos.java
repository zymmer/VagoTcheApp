package br.com.vagotche.vagotcheapp;

/**
 * Created by guilherme on 16/10/17.
 */

public class ListParquimetrosMaisProximos {
    String name, tipoVaga, ocupacao, latitude, longitude;
    Float latLong, tipoIcon;
    int colorText, quatidadeVagasOcupadas;


    public void setData(String name, Float latLong, String tipoVaga, int quatidadeVagasOcupadas, String ocupacao, String latitude, String longitude, Float tipoIcon, int colorText) {
        this.name = name;
        this.latLong = latLong;
        this.tipoVaga = tipoVaga;
        this.quatidadeVagasOcupadas = quatidadeVagasOcupadas;
        this.ocupacao = ocupacao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tipoIcon = tipoIcon;
        this.colorText = colorText;
    }

    public String getNomeParquimetro() {
        return name;
    }

    public Float getLatLong() {
        return latLong;
    }

    public String getTipoVaga() {
        return tipoVaga;
    }

    public String getQuatidadeVagasOcupadas() {
        return String.valueOf(quatidadeVagasOcupadas);
    }

    public String getOcupacao() {
        return ocupacao;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Float getTipoIcon() {
        return tipoIcon;
    }

    public int getColorText() {
        return colorText;
    }

}