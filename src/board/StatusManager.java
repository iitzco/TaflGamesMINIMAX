package board;

public class StatusManager {
	private Board b;
	private boolean outOfPiecesDefender;
	private boolean outOfPiecesAgressor;
	private boolean kingArrivesToCastle;
	private boolean kingGetsSurrounded;
	private boolean outOfMovesAgressor;
	private boolean outOfMovesDefender;

	public StatusManager(Board b) {
		this.b = b;
		outOfPiecesDefender = false;
		outOfPiecesAgressor = false;
		kingArrivesToCastle = false;
		kingGetsSurrounded = false;
	}

	public boolean outOfPiecesEnemy() {
		for (int i = 0; i < b.getSize(); i++) {
			for (int j = 0; j < b.getSize(); j++) {
				if (!b.getBoard()[i][j].isEmpty())
					if (b.getBoard()[i][j].getCellContent().isEnemy())
						return false;
			}
		}
		return true;
	}

	public boolean outOfPiecesDefender() {
		for (int i = 0; i < b.getSize(); i++) {
			for (int j = 0; j < b.getSize(); j++) {
				if (!b.getBoard()[i][j].isEmpty())
					if (b.getBoard()[i][j].getCellContent().isGuard()
							|| b.getBoard()[i][j].getCellContent().isKing())
						return false;
			}
		}
		return true;
	}

	public boolean outOfPieces(boolean side) {
		for (int i = 0; i < b.getSize(); i++) {
			for (int j = 0; j < b.getSize(); j++) {
				if (!b.getBoard()[i][j].isEmpty())
					if (b.getBoard()[i][j].getCellContent().isAlly(side))
						return false;
			}
		}
		return true;
	}

	public Coordinates localizeKing() {
		for (int i = 0; i < b.getSize(); i++) {
			for (int j = 0; j < b.getSize(); j++) {
				if (!b.getBoard()[i][j].isEmpty()) {
					if (b.getBoard()[i][j].getCellContent().isKing())
						return new Coordinates(i, j);
				}
			}
		}
		// nunca llega
		return null;
	}

	private int isKingThreatened(int x, int y) {
		if (!b.getBoard()[x][y].isEmpty())
			if (b.getBoard()[x][y].getCellContent().isEnemy())
				return 1;
		if (b.isThrone(b.getBoard()[x][y]))
			return 2;
		return 0;
	}

	private boolean outOfBounds(int n) {
		return n < 0 || n >= b.getSize();
	}

	protected boolean castleNext(Coordinates c) {
		if (!outOfBounds(c.getX() + 1) && castleNear(c.getX() + 1, c.getY()))
			return true;
		if (!outOfBounds(c.getX() - 1) && castleNear(c.getX() - 1, c.getY()))
			return true;
		if (!outOfBounds(c.getY() + 1) && castleNear(c.getX(), c.getY() + 1))
			return true;
		if (!outOfBounds(c.getY() - 1) && castleNear(c.getX(), c.getY() - 1))
			return true;

		return false;
	}

	protected boolean castleNear(int x, int y) {
		return b.isCastle(b.getBoard()[x][y]);
	}

	private boolean blockedByAlly(Coordinates c) {
		if (blockedByAllybyCoordinates(c.getX(), c.getY() + 1))
			return true;
		if (blockedByAllybyCoordinates(c.getX() + 1, c.getY()))
			return true;
		if (blockedByAllybyCoordinates(c.getX(), c.getY() - 1))
			return true;
		if (blockedByAllybyCoordinates(c.getX() - 1, c.getY()))
			return true;
		return false;

	}

	private boolean blockedByAllybyCoordinates(int x, int y) {
		if (!outOfBounds(x) && !outOfBounds(y)) {
			if (!b.getBoard()[x][y].isEmpty()) {
				if (b.getBoard()[x][y].getCellContent().isGuard())
					return true;
			}
		}
		return false;
	}

	public boolean isKingSurrounded(Coordinates to) {
		if (!b.getBoard()[to.getX()][to.getY()].getCellContent().isEnemy()
				|| !kingNear(to))
			return false;

		Coordinates c = localizeKing();
		int border = 0;
		int throne = 0;
		int castle = 0;
		int enemies = 0;
		int inc_x = c.getX() + 1;
		int inc_y = c.getY() + 1;
		int dec_x = c.getX() - 1;
		int dec_y = c.getY() - 1;
		int code;
		if (outOfBounds(inc_y) || outOfBounds(inc_x) || outOfBounds(dec_y)
				|| outOfBounds(dec_x))
			border++;
		if (castleNext(c))
			castle++;
		if ((!outOfBounds(inc_x) && (code = isKingThreatened(inc_x, c.getY())) > 0)) {
			if (code == 1)
				enemies++;
			else
				throne++;
		}
		if ((!outOfBounds(dec_x) && (code = isKingThreatened(dec_x, c.getY())) > 0)) {
			if (code == 1)
				enemies++;
			else
				throne++;
		}
		if ((!outOfBounds(inc_y) && (code = isKingThreatened(c.getX(), inc_y)) > 0)) {
			if (code == 1)
				enemies++;
			else
				throne++;
		}
		if ((!outOfBounds(dec_y) && (code = isKingThreatened(c.getX(), dec_y)) > 0)) {
			if (code == 1)
				enemies++;
			else
				throne++;
		}
		return enemies == 4 || (throne == 1 && enemies == 3)
				|| (enemies == 3 && blockedByAlly(c))
				|| (enemies == 3 && border == 1)
				|| (enemies == 2 && castle == 1 && border == 1);

	}

	protected boolean kingNear(Coordinates c) {

		if (!outOfBounds(c.getX() + 1)
				&& !b.getBoard()[c.getX() + 1][c.getY()].isEmpty()
				&& b.getBoard()[c.getX() + 1][c.getY()].getCellContent()
						.isKing())
			return true;
		if (!outOfBounds(c.getX() - 1)
				&& !b.getBoard()[c.getX() - 1][c.getY()].isEmpty()
				&& b.getBoard()[c.getX() - 1][c.getY()].getCellContent()
						.isKing())
			return true;
		if (!outOfBounds(c.getY() + 1)
				&& !b.getBoard()[c.getX()][c.getY() + 1].isEmpty()
				&& b.getBoard()[c.getX()][c.getY() + 1].getCellContent()
						.isKing())
			return true;
		if (!outOfBounds(c.getY() - 1)
				&& !b.getBoard()[c.getX()][c.getY() - 1].isEmpty()
				&& b.getBoard()[c.getX()][c.getY() - 1].getCellContent()
						.isKing())
			return true;

		return false;
	}

	public boolean isKingOnCastle() {
		if ((!b.getBoard()[0][0].isEmpty() && b.getBoard()[0][0]
				.getCellContent().isKing())
				|| (!b.getBoard()[0][b.getSize() - 1].isEmpty() && b.getBoard()[0][b
						.getSize() - 1].getCellContent().isKing())
				|| (!b.getBoard()[b.getSize() - 1][0].isEmpty() && b.getBoard()[b
						.getSize() - 1][0].getCellContent().isKing())
				|| (!b.getBoard()[b.getSize() - 1][b.getSize() - 1].isEmpty() && b
						.getBoard()[b.getSize() - 1][b.getSize() - 1]
						.getCellContent().isKing()))
			return true;
		return false;
	}

	public boolean checkIfWin(boolean kingSide) {
		if (kingSide)
			return isKingOnCastle2() || outOfMovesAgressor2()
					|| outOfPiecesEnemy2();
		else
			return isKingSurrounded2() || outOfMovesDefender2()
					|| outOfPiecesDefender2();
	}

	public int checkGameOver(boolean turn) {

		if (checkIfWin(turn)) {
			if (turn)
				return 1;
			return 2;
		}
		return 0;
	}

	public boolean isKingOnCastle2() {
		return kingArrivesToCastle;
	}

	public boolean isKingSurrounded2() {
		return kingGetsSurrounded;
	}

	public boolean outOfPiecesDefender2() {
		return outOfPiecesDefender;
	}

	public boolean outOfPiecesEnemy2() {
		return outOfPiecesAgressor;
	}

	public boolean outOfMovesDefender2() {
		return outOfMovesDefender;
	}

	public boolean outOfMovesAgressor2() {
		return outOfMovesAgressor;
	}

	public int freeWay() {
		Coordinates k = localizeKing();
		return freeWayHorizontal(k, 1) + freeWayHorizontal(k, -1)
				+ freeWayVertical(k, 1) + freeWayVertical(k, -1);

	}

	private int freeWayVertical(Coordinates k, int increment) {
		int i, j, count = 0;
		for (i = k.getY(); i < b.getSize() && i >= 0
				&& b.getBoard()[k.getX()][i].isEmpty(); i += increment)
			;
		if (i != b.getSize() || i != -1)
			return 0;

		for (j = k.getX(); j < b.getSize()
				&& b.getBoard()[b.getSize() - 1][j].isEmpty(); j++)
			;
		if (j == b.getSize()) {
			count++;
		}
		for (j = k.getX(); j >= 0 && b.getBoard()[b.getSize() - 1][j].isEmpty(); j--)
			;
		if (j == -1)
			count++;

		return count;
	}

	private int freeWayHorizontal(Coordinates k, int increment) {
		int i, j, count = 0;
		for (i = k.getX(); i < b.getSize() && i >= 0
				&& b.getBoard()[i][k.getX()].isEmpty(); i += increment)
			;
		if (i != b.getSize() || i != -1)
			return 0;

		for (j = k.getY(); j < b.getSize()
				&& b.getBoard()[j][b.getSize() - 1].isEmpty(); j++)
			;
		if (j == b.getSize()) {
			count++;
			;
		}
		for (j = k.getY(); j >= 0 && b.getBoard()[j][b.getSize() - 1].isEmpty(); j--)
			;
		if (j == -1)
			count++;

		return count;
	}

	public void updateStatus(Coordinates to) {
		if (isKingOnCastle())
			kingArrivesToCastle = true;
		else
			kingArrivesToCastle = false;
		if (isKingSurrounded(to)) {
			kingGetsSurrounded = true;
		}

		else
			kingGetsSurrounded = false;
		if (outOfPiecesDefender())
			outOfPiecesDefender = true;
		else
			outOfPiecesDefender = false;
		if (outOfPiecesEnemy())
			outOfPiecesAgressor = true;
		else
			outOfPiecesAgressor = false;
		if (b.getMoveManager().outOfMovesAgressor())
			outOfMovesAgressor = true;
		else
			outOfMovesAgressor = false;
		if (b.getMoveManager().outOfMovesDefender())
			outOfMovesDefender = true;
		else
			outOfMovesDefender = false;
		return;
	}

	public double distanceToKing(Coordinates k, int i, int j) {
		return Math.sqrt(Math.pow(i - k.getX(), 2) + Math.pow(j - k.getY(), 2));
	}

}
