package board;

import pieces.Enemy;
import pieces.Guard;
import pieces.King;
import pieces.Piece;

public class Board {

	private MoveManager moveManager;
	private StatusManager statusManager;
	private Cell[][] board;
	private int size;
	private boolean type; // false si es menor a 11, true si es mayor.
	private Minimax minimax;

	// Recibe el tamanio y una secuencia de char que representan el tablero.
	public Board(int size, char[] state, boolean prune, boolean byTime,
			int data, boolean toDotFile) {
		this.size = size;
		type = this.size <= 11 ? false : true;
		board = new Cell[size][size];
		initializeBoard(state);
		statusManager = new StatusManager(this);
		moveManager = new MoveManager(this, statusManager);
		minimax = new Minimax(prune, byTime, data, toDotFile);

	}

	public Minimax getMinimaxAlgorithm() {
		return minimax;
	}

	public Board(Cell[][] b, int s, boolean t) {
		board = b;
		size = s;
		type = t;
		statusManager = new StatusManager(this);
		moveManager = new MoveManager(this, statusManager);
	}

	public void initializeBoard(char[] state) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				switch (state[i * size + j]) {
				case '0':
					board[i][j] = new Cell();
					break;
				case 'N':
					board[i][j] = new Cell(Enemy.getInstance());
					break;
				case 'G':
					board[i][j] = new Cell(Guard.getInstance());
					break;
				case 'R':
					board[i][j] = new Cell(King.getInstance());
					break;

				}
			}
		}
	}

	public void print() {
		System.out.print("xx");
		for (int i = 0; i < size; i++) {
			System.out.print(i);
		}
		System.out.println();
		for (int i = 0; i < size; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < size; j++) {

				System.out.print(board[i][j].toString());
			}
			System.out.println();
		}
	}

	public boolean typeOfBoard() {
		return type;
	}

	public Cell throne() {
		return board[size / 2][size / 2];
	}

	public boolean isThrone(Cell cell) {
		return throne() == cell;
	}

	public boolean isCastle(Cell cell) {
		return board[0][0] == cell || board[0][size - 1] == cell
				|| board[size - 1][0] == cell
				|| board[size - 1][size - 1] == cell;
	}

	public int getHeight() {
		return size;
	}

	public int getWidth() {
		return size;
	}

	public Cell get(int i, int j) {
		return board[i][j];

	}

	public StatusManager getStatusManager() {
		return statusManager;
	}

	public MoveManager getMoveManager() {
		return moveManager;
	}

	public int getSize() {
		return size;
	}

	public Cell[][] getBoard() {
		return board;
	}

	public Board copyBoard() {
		Cell[][] ret = new Cell[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				ret[i][j] = new Cell(board[i][j]);
			}
		}
		return new Board(ret, size, type);
	}

//	public int getWeight() {
//		if (statusManager.isKingOnCastle2()
//				|| statusManager.outOfPiecesEnemy2()
//				|| statusManager.outOfMovesAgressor2())
//			return Integer.MAX_VALUE - 1;
//		if (statusManager.outOfPiecesDefender2()
//				|| statusManager.isKingSurrounded2()
//				|| statusManager.outOfMovesDefender2()) {
//			return Integer.MIN_VALUE + 1;
//		}
//
//		int allies = 0;
//		int enemies = 0;
//		for (int i = 0; i < size; i++) {
//			for (int j = 0; j < size; j++) {
//				if (!board[i][j].isEmpty()) {
//					if (board[i][j].getCellContent().isGuard()
//							|| board[i][j].getCellContent().isKing())
//						allies++;
//					else if (board[i][j].getCellContent().isEnemy())
//						enemies++;
//				}
//			}
//		}
//		return allies - enemies;
//
//	}

	public int getWeight() {
		if (statusManager.isKingOnCastle2()
				|| statusManager.outOfPiecesEnemy2()
				|| statusManager.outOfMovesAgressor2())
			return Integer.MAX_VALUE - 1;
		if (statusManager.outOfPiecesDefender2()
				|| statusManager.isKingSurrounded2()
				|| statusManager.outOfMovesDefender2()) {
			return Integer.MIN_VALUE + 1;
		}

		int allies = 0;
		int enemies = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (!board[i][j].isEmpty()) {
					Piece p = board[i][j].getCellContent();
					Coordinates c = new Coordinates(i, j);
					if (p.isGuard()) {
						if (statusManager.kingNear(c))
							allies += 150;
						else
							allies += 100;
					} else if (p.isKing()) {
						if (statusManager.castleNext(c))
							allies += 200;
						else
							allies += 100;
					} else if (p.isEnemy()) {
						if (statusManager.kingNear(c))
							enemies += 150;
						else
							enemies += 100;
					}
				}
			}
		}
		return allies - enemies;
	}

//	public int getWeight() {
//		if (statusManager.isKingOnCastle2()
//				|| statusManager.outOfPiecesEnemy2()
//				|| statusManager.outOfMovesAgressor2())
//			return Integer.MAX_VALUE - 1;
//		if (statusManager.outOfPiecesDefender2()
//				|| statusManager.isKingSurrounded2()
//				|| statusManager.outOfMovesDefender2()) {
//			return Integer.MIN_VALUE + 1;
//		}
//
//		double distance_allies = 0;
//		double distance_enemies = 0;
//		Coordinates k = statusManager.localizeKing();
//		int allies = 0;
//		int enemies = 0;
//		for (int i = 0; i < size; i++) {
//			for (int j = 0; j < size; j++) {
//				if (!board[i][j].isEmpty()) {
//					Piece p = board[i][j].getCellContent();
//					if (p.isGuard()) {
//						allies += 10;
//						distance_allies += statusManager
//								.distanceToKing(k, i, j);
//					} else if (p.isKing()) {
//						allies += 3 * statusManager.freeWay();
//					} else if (p.isEnemy()) {
//						enemies += 10;
//						distance_enemies += statusManager.distanceToKing(k, i,
//								j);
//					}
//				}
//			}
//		}
//		return allies - enemies + (int) (distance_enemies - distance_allies);
//	}


}
