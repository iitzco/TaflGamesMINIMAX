package board;

import pieces.*;

public class Cell {

	private Piece p;
	private boolean isSelected;

	public Cell() {
		p = null;
	}

	public Cell(Piece p) {
		this.p = p;
	}

	public Cell(Cell cell) {
		p=cell.p;
		isSelected=cell.isSelected;
	}

	public Piece getCellContent(){
		return p;
	}

	public boolean isEmpty() {
		return p == null;
	}


	@Override
	public String toString() {
		return isEmpty() ? "-" : p.toString();
	}

	public void setPiece(Piece p){
		this.p=p;
	}

	public void select() {

		this.isSelected=!this.isSelected;
	}

	
	public boolean isSelected() {
		return isSelected;
	} 


}
