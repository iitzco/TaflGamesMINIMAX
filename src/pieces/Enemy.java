package pieces;

public class Enemy implements Piece{

	private static Enemy instance=null;
	
	public static Enemy getInstance(){
		if(instance==null)
			return new Enemy();
		return instance;			
	}
	
	private Enemy() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public String toString() {
		return "N";
	}

	@Override
	public boolean isKing() {
		return false;
	}

	@Override
	public boolean isAlly(boolean kingTeam) {
		return kingTeam? false:true;
	}

	@Override
	public boolean isEnemy(boolean kingTeam) {
		return kingTeam? true:false;
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
		return true;
	}

}

