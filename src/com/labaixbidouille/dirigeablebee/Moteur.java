package com.labaixbidouille.dirigeablebee;
public enum Moteur {
	HAUT_AVANT(1), HAUT_ARRIERE(0), DROITE(2), GAUCHE(3);

	int numero;

	Moteur(int numero) {
		this.numero = numero;
	}

	public int getNumero() {
		return numero;
	}
	
	public String getSortie(){
		return "D" + numero;
	}

}
