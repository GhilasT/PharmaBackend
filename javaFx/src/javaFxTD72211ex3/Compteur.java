package javaFxTD72211ex3;
public class Compteur {
	private int valeur ;
	public Compteur() { valeur = 0 ; }
	public void incrementer(int i) { valeur += i ; }
	public void incrementer() { valeur++ ; }
	public int getValeur() { return valeur ; }
}
