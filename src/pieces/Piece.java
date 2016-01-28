package pieces;


public interface Piece {
	public boolean isKing();

	public boolean isGuard();

	public boolean isEnemy();

	public boolean isAlly(boolean kingTeam);

	public boolean isEnemy(boolean kingTeam);

	public boolean isCastle();

}