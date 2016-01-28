package pieces;


public class Guard implements Piece {

	private static Guard instance=null;
	
	public static Guard getInstance(){
		if(instance==null)
			return new Guard();
		return instance;			
	}
	
	private Guard() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public String toString() {
		return "G";
	}

	@Override
	public boolean isKing() {
		return false;
	}

	@Override
	public boolean isAlly(boolean kingTeam) {
		return kingTeam? true:false;
	}

	@Override
	public boolean isEnemy(boolean kingTeam) {
		return kingTeam? false:true;
	}

	@Override
	public boolean isCastle() {
		return false;
	}

	@Override
	public boolean isGuard() {
		return true;
	}

	@Override
	public boolean isEnemy() {
		return false;
	}

}

