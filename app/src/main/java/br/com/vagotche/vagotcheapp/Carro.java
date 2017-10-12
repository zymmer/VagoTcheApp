package br.com.vagotche.vagotcheapp;

import java.util.List;

public class Carro {
	private String placa;
	private String modelo;
	private String anoModelo;
	private String ano;
	private String cor;
	private List<Placa> placas;

	public Carro() {
		super();
	}
	public Carro(String placa, String modelo, String anoModelo, String ano, String cor, List<Placa> placas) {
		super();
		this.placa = placa;
		this.modelo = modelo;
		this.ano = ano;
		this.anoModelo = anoModelo;
		this.cor = cor;
		this.placas = placas;

	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getAnoModelo() {
		return anoModelo;
	}

	public void setAnoModelo(String anoModelo) {
		this.anoModelo = anoModelo;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public List<Placa> getPlacas() {
		return placas;
	}

	public void setPlacas(List<Placa> placas) {this.placas = placas;}

}