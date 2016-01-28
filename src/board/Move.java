package board;

public class Move {

	private Coordinates from;
	private Coordinates to;
	private int weight;

	private int alpha;
	private int beta;
	
	private static long idGlobal=0;
	private Long id;

	public Move(int x1, int y1, int x2, int y2, int w) {
		from = new Coordinates(x1, y1);
		to = new Coordinates(x2, y2);
		this.weight = w;
		id=idGlobal++;
	}
	
	public Move(int x1, int y1, int x2, int y2, int w, int alpha, int beta) {
		from = new Coordinates(x1, y1);
		to = new Coordinates(x2, y2);
		this.weight = w;
		this.alpha=alpha;
		this.beta=beta;
		id=idGlobal++;
	}

	public Move(Coordinates f, Coordinates t, int w) {
		from = f;
		to = t;
		this.weight = w;
		id=idGlobal++;
	}
	
	public Move(Coordinates f, Coordinates t, int w,int alhpa, int beta) {
		from = f;
		to = t;
		this.weight = w;
		this.alpha=alhpa;
		this.beta=beta;
		id=idGlobal++;
	}

	public Move(int x1, int y1, int x2, int y2) {
		from = new Coordinates(x1, y1);
		to = new Coordinates(x2, y2);
		this.weight = 0;
		id=idGlobal++;
	}

	public Move(Coordinates f, Coordinates t) {
		from = f;
		to = t;
		this.weight = 0;
		id=idGlobal++;
	}

	public Coordinates getFrom() {
		return from;
	}

	public Coordinates getTo() {
		return to;
	}

	public void setFrom(Coordinates from) {
		this.from = from;
	}

	public void setTo(Coordinates to) {
		this.to = to;
	}

	public void setFrom(int x, int y) {
		from.setX(x);
		from.setY(y);
	}

	public void setTo(int x, int y) {
		to.setX(x);
		to.setY(y);
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	public int getAlpha() {
		return alpha;
	}

	public int getBeta() {
		return beta;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public void setBeta(int beta) {
		this.beta = beta;
	}
	
	public boolean hasToPrune(boolean max,int currentValue){
		if (max && currentValue>=beta){
			return true;
		}
		if (!max && currentValue*(-1)<=alpha){
			return true;
		}			
		return false;
		
	}
	
	public void updateParameters(boolean max, int actual){
		if (max)
			alpha=actual;
		else
			beta=actual*(-1);
	}
	
	@Override
	public String toString() {
		return from.toString()+" -> "+to.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
	
	
	public long hashForPrint() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());		
		return result;
	}
	
	

}
