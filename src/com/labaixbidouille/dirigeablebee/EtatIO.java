package com.labaixbidouille.dirigeablebee;
public enum EtatIO {
	DISABLE(0), NA(1), ADC(2), INPUT(3), OUTPUT_LOW(4), OUTPUT_HIGH(5);

	int valeur;

	private EtatIO(int valeur) {
		this.valeur = valeur;
	}

	public int getValeur() {
		return valeur;
	}

}
