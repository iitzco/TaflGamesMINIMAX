package pieces;


public class King implements Piece{
	
	private static King instance=null;
	
	public static King getInstance(){
		if(instance==null)
			return new King();
		return instance;			
	}
	
	private King() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean isKing() {
		return true;
	}

	@Override
	public boolean isAlly(boolean kingTeam) {
		return kingTeam ? true : false;
	}

	@Override
	public boolean isEnemy(boolean kingTeam) {
		return kingTeam ? false : true;
	}

	@Override
	public boolean isCastle() {
		return false;
	}

	@Override
	public boolean isGuard() {
		return false;
	}

	@Override
	public boolean isEnemy() {
		return false;
	}

}
