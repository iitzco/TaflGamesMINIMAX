package board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pieces.Piece;

public class MoveManager {
	private Board b;
	private StatusManager statusManager;

	public MoveManager(Board b,StatusManager s) {
		this.b = b;
		statusManager=s;
	}

	public int checkFrom(Coordinates c, boolean kingSide) {
		int x = c.getX();
		int y = c.getY();
		if (x >= b.getSize() || y >= b.getSize() || x < 0 || y < 0)
			return 1;
		Cell cell = b.getBoard()[x][y];
		if (cell.isEmpty())
			return 2;
		if (!cell.getCellContent().isAlly(kingSide))
			return 3;
		return 0;
	}

	public void makeMove(Move v, boolean kingSide) {
		Cell c = b.getBoard()[v.getFrom().getX()][v.getFrom().getY()];
		Piece piece = c.getCellContent();
		c.setPiece(null);
		Cell to = b.getBoard()[v.getTo().getX()][v.getTo().getY()];
		to.setPiece(piece);
		eatPieces(to, v.getTo().getX(), v.getTo().getY(), kingSide);
		statusManager.updateStatus(v.getTo());
	}
	public int verifyMove(Coordinates from, Coordinates to, boolean kingSide) {
		int origin_x = from.getX();
		int origin_y = from.getY();
		int dest_x = to.getX();
		int dest_y = to.getY();

		if (origin_x != dest_x && origin_y != dest_y)
			return 1;
		if (origin_x == dest_x && origin_y == dest_y)
			return 2;
		// chequeo que no se vaya del tablero (no deberia pasar por la interfaz)
		if (dest_x >= b.getSize() || dest_y >= b.getSize() || dest_x < 0
				|| dest_y < 0)
			return 3;
		// chequeo que haya una pieza de el
		if (!b.getBoard()[dest_x][dest_y].isEmpty())
			return 4;
		Cell cell = b.getBoard()[origin_x][origin_y];
		return origin_x == dest_x ? projectMove(1, cell.getCellContent(),
				origin_y, dest_y, origin_x,kingSide) : projectMove(2,
				cell.getCellContent(), origin_x, dest_x, origin_y,kingSide);

	}
	
	private int projectMove(int xORy, Piece p, int origin, int dest,
			int constant,boolean kingSide) {
		Cell c;
		if (xORy == 1)
			c = b.getBoard()[constant][dest];
		else
			c = b.getBoard()[dest][constant];
		if (b.isCastle(c) && !p.isKing())
			return 5;
		if (b.isThrone(c) && !p.isKing())
			return 6;
		int current = origin > dest ? origin - 1 : origin + 1;
		while (current != dest) {
			Cell path;
			if (xORy == 1)
				path = b.getBoard()[constant][current];
			else
				path = b.getBoard()[current][constant];
			if (!path.isEmpty())
				return 7;
			if (b.isThrone(path) && b.typeOfBoard() && kingSide) {
				return 8;
			}
			current = current > dest ? current - 1 : current + 1;
		}
		return 0;
	}
	
	public List<Move> getPossibleMoves(boolean side) {
		List<Move> l = new ArrayList<Move>();
		for (int i = 0; i < b.getSize(); i++) {
			for (int j = 0; j < b.getSize(); j++) {
				if (!b.getBoard()[i][j].isEmpty())
					if (b.getBoard()[i][j].getCellContent().isAlly(side))
						getPossibleMovesFrom(side, i, j, l);
			}
		}
		if (!side) {
			Collections.sort(l, new Comparator<Move>() {

				@Override
				public int compare(Move o1, Move o2) {
					if (b.getBoard()[o2.getFrom().getX()][o2.getFrom().getY()]
							.getCellContent().isKing()
							&& !b.getBoard()[o1.getFrom().getX()][o1.getFrom().getY()]
									.getCellContent().isKing())
						return 1;
					if (!b.getBoard()[o2.getFrom().getX()][o2.getFrom().getY()]
							.getCellContent().isKing()
							&& b.getBoard()[o1.getFrom().getX()][o1.getFrom().getY()]
									.getCellContent().isKing())
						return -1;
					return 0;
				}
			});
		}
		return l;
	}

	public void getPossibleMovesFrom(boolean side, int x, int y, List<Move> m) {
		getPossibleMovesFromHorizontal(side, x, y, 1, m);
		getPossibleMovesFromHorizontal(side, x, y, -1, m);
		getPossibleMovesFromVertical(side, x, y, 1, m);
		getPossibleMovesFromVertical(side, x, y, -1, m);
	}

	public void getPossibleMovesFromVertical(boolean side, int x, int y,
			int dy, List<Move> m) {
		boolean blocked = false;
		int current = y + dy;
		while (!blocked) {
			if (current < 0 || current >= b.getSize())
				blocked = true;
			else if (b.isCastle(b.getBoard()[x][current])) {
				if (!b.getBoard()[x][y].getCellContent().isKing())
					blocked = true;
				else
					m.add(new Move(x, y, x, current));
			} else if (b.getBoard()[x][current].isEmpty()) {
				if (!b.isThrone(b.getBoard()[x][current]))
					m.add(new Move(x, y, x, current));
				else if ((b.typeOfBoard() && side && !b.getBoard()[x][y]
						.getCellContent().isKing()))
					blocked = true;
			} else
				blocked = true;
			current += dy;
		}

	}

	public void getPossibleMovesFromHorizontal(boolean side, int x, int y,
			int dx, List<Move> m) {
		boolean blocked = false;
		int current = x + dx;
		while (!blocked) {
			if (current < 0 || current >= b.getSize())
				blocked = true;
			else if (b.isCastle(b.getBoard()[current][y])) {
				if (!b.getBoard()[x][y].getCellContent().isKing())
					blocked = true;
				else
					m.add(new Move(x, y, current, y));
			} else if (b.getBoard()[current][y].isEmpty()) {
				if (!b.isThrone(b.getBoard()[current][y]))
					m.add(new Move(x, y, current, y));
				else if (b.typeOfBoard() && side
						&& !b.getBoard()[x][y].getCellContent().isKing())
					blocked = true;
			} else
				blocked = true;
			current += dx;
		}

	}
	
	public boolean outOfMoves(boolean kingSide) {
		List<Move> l = new ArrayList<Move>();
		for (int i = 0; i < b.getSize(); i++) {
			for (int j = 0; j < b.getSize(); j++) {
				if (!b.getBoard()[i][j].isEmpty()) {
					if (b.getBoard()[i][j].getCellContent().isAlly(kingSide)) {
						getPossibleMovesFrom(kingSide, i, j, l);
						if (l.size() != 0){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public void eatPieces(Cell cell, int x, int y, boolean kingSide) {
		analyze(cell, x, y, 1, 0, kingSide);
		analyze(cell, x, y, 0, 1, kingSide);
		analyze(cell, x, y, -1, 0, kingSide);
		analyze(cell, x, y, 0, -1, kingSide);
	}

	private boolean analyze(Cell cell, int x, int y, int dx, int dy,
			boolean kingSide) {
		int forward_x_1 = x + dx;
		int forward_y_1 = y + dy;
		int forward_x_2 = x + 2 * dx;
		int forward_y_2 = y + 2 * dy;

		if (forward_x_1 >= b.getSize() || forward_x_1 < 0 || forward_y_1 >= b.getSize()
				|| forward_y_1 < 0)
			return false;
		Cell next = b.getBoard()[forward_x_1][forward_y_1];
		if (next.isEmpty())
			return false;
		if (!next.getCellContent().isEnemy(kingSide)
				|| next.getCellContent().isKing()) {
			return false;
		}
		// Si llego hasta aca, significa que al lado hay un enemigo
		if (forward_x_2 >= b.getSize() || forward_x_2 < 0 || forward_y_2 >= b.getSize()
				|| forward_y_2 < 0)
			return false;
		Cell backup = b.getBoard()[forward_x_2][forward_y_2];
		if (b.isCastle(backup)) {
			next.setPiece(null);
			return true;
		}
		if (b.isThrone(backup) && !b.typeOfBoard() && kingSide) {
			next.setPiece(null);
			return true;
		}
		if (backup.isEmpty())
			return false;
		if (backup.getCellContent().isAlly(kingSide)) {

			next.setPiece(null);
			return true;
		}
		return false;
	}

	public boolean outOfMovesAgressor() {
		return outOfMoves(false);
	}
	
	public boolean outOfMovesDefender() {
		return outOfMoves(true);
	}

	

}
