package game;

import java.awt.Color;

public class Messages {
	public static void informErrorFrom(int code, Game g) {
		switch (code) {
		case 1:
			g.sendMessage("Fuera de rango",Color.RED);
			break;
		case 2:
			g.sendMessage("El casillero esta vacio!",Color.RED);
			break;
		case 3:
			g.sendMessage("Esa pieza no es tuya!",Color.RED);
			break;
		}
	}

	public static void informErrorTo(int code, Game g) {
		switch (code) {
		case 1:
			g.sendMessage("No se puede mover en diagonal",Color.RED);
			break;
		case 2:
			g.sendMessage("Debe moverse",Color.RED);
			break;
		case 3:
			g.sendMessage("Fuera de rango",Color.RED);
			break;
		case 4:
			g.sendMessage("Ese destino esta ocupado",Color.RED);
			break;
		case 5:
			g.sendMessage("No puede ir al castillo",Color.RED);
			break;
		case 6:
			g.sendMessage("No puede ir al trono",Color.RED);
			break;
		case 7:
			g.sendMessage("Camino bloqueado",Color.RED);
			break;
		case 8:
			g.sendMessage("No puede pasar por el trono",Color.RED);
			break;
		}
	}

}
