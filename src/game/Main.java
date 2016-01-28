package game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;

import board.Board;
import board.Move;

public class Main {
	public static void main(String[] args) {
		try {
			Validator v = new Validator();

			v.validateParameters(args);
			if (v.visual) {
				Game g = v.generateGame(v.f, v.prune, v.byTime, v.data,
						v.toDotFile);
				g.setVisibleGUI(true);
			} else {
				Board b = v.generateBoard(v.f, v.prune, v.byTime, v.data,
						v.toDotFile);
				Move m = b.getMinimaxAlgorithm().executeMinimax(b, v.getTurn());
				if (m == null || m.getTo() == null || m.getFrom() == null)
					System.out.println("No se pudo realizar ninguna jugada!");
				else
					System.out.println(m.toString());
			}
		} catch (InvalidParameterException e) {
			System.out.println("Error en los parametros");
		} catch (FileNotFoundException e) {
			System.out.println("No se encontro el archivo");
		} catch (IOException e) {
			System.out.println("Error desconocido");
		}

	}
}
