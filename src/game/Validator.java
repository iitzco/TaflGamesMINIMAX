package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;

import board.Board;

public class Validator {

	boolean prune;
	boolean byTime;
	int data = 3;// data puede ser la profundidad o el tiempo en segundos.
	boolean visual;
	boolean toDotFile;
	File f;
	boolean turn;

	public void validateParameters(String[] params)
			throws InvalidParameterException {
		prune = false;
		toDotFile = false;
		if (params.length < 5) {
			throw new InvalidParameterException();
		} else {
			if (params[0].equals("-file")) {
				f = new File(params[1]);
			} else {
				throw new InvalidParameterException();
			}
			if (params[2].equals("-maxtime")) {
				byTime = true;
			} else if (params[2].equals("-depth")) {
				byTime = false;
			} else {
				throw new InvalidParameterException();
			}
			data = Integer.parseInt(params[3]);
			if (params[4].equals("-visual")) {
				visual = true;
			} else if (params[4].equals("-console")) {
				visual = false;
			} else {
				throw new InvalidParameterException();
			}
		}
		if (params.length == 6) {
			if (params[5].equals("-prune")) {
				prune = true;
			} else if (params[5].equals("-tree")
					&& params[4].equals("-console")) {
				prune = false;
				toDotFile = true;
			} else {
				throw new InvalidParameterException();
			}
		}
		if (params.length == 7)
			if (params[5].equals("-prune") && params[6].equals("-tree")
					&& params[4].equals("-console")) {
				prune = true;
				toDotFile = true;
			} else {
				throw new InvalidParameterException();
			}
		if (params.length > 7)
			throw new InvalidParameterException();

	}

	@SuppressWarnings("resource")
	public Game generateGame(File f, boolean prune, boolean byTime, int data,
			boolean toDotFile) throws FileNotFoundException, IOException,
			InvalidParameterException {
		String state = "";
		int i = 1;
		int size;
		FileInputStream fis;
		fis = new FileInputStream(f);

		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;

		// Leo la primera linea para el turno.
		line = br.readLine();
		if (line.length() != 1
				|| !(line.charAt(0) == '1' || line.charAt(0) == '2'))
			throw new InvalidParameterException();
		if (line.charAt(0) == '1')
			turn = false;
		else
			turn = true;

		// Leo la segunda linea para tener parametro del tamanio
		line = br.readLine();
		size = line.length();
		state += line;
		while ((line = br.readLine()) != null) {
			if (line.length() != size) {
				throw new InvalidParameterException();
			}
			state += line;
			i++;
		}
		br.close();
		if (i != size || i < 7 || i > 19 || i % 2 == 0)
			throw new InvalidParameterException();
		return new Game(turn, size, state, prune, byTime, data, toDotFile);

	}

	@SuppressWarnings("resource")
	public Board generateBoard(File f, boolean prune, boolean byTime, int data,
			boolean toDotFile) throws FileNotFoundException, IOException,
			InvalidParameterException {
		String state = "";
		int i = 1;
		int size;
		FileInputStream fis;
		fis = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;

		// Leo la primera linea para el turno.
		line = br.readLine();
		if (line.length() != 1
				|| !(line.charAt(0) == '1' || line.charAt(0) == '2'))
			throw new InvalidParameterException();
		if (line.charAt(0) == '1')
			turn = false;
		else
			turn = true;

		// Leo la segunda linea para tener parametro del tamanio
		line = br.readLine();
		size = line.length();
		state += line;
		while ((line = br.readLine()) != null) {
			if (line.length() != size) {
				throw new InvalidParameterException();
			}
			state += line;
			i++;
		}
		br.close();
		if (i != size || i < 7 || i > 19 || i % 2 == 0)
			throw new InvalidParameterException();
		return new Board(size, state.toCharArray(), prune, byTime, data,
				toDotFile);
	}

	public boolean getTurn() {
		return turn;
	}
}
