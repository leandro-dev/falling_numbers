package com.leandroideias.falling.numbers.util;

public class Number {
	private int numero;
	private float posX, posY;
	
	public Number(int numero, float posX){
		this.numero = numero;
		this.posX = posX;
		this.posY = 0;
	}

	public int getNumero() {
		return numero;
	}

	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}
}
