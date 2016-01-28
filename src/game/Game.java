package game;

import java.awt.Color;
import view.BoardFrame;
import view.BoardPanel;
import board.Board;
import board.Coordinates;
import board.Move;

public class Game {

	private Board b;
	private boolean turn; // false para la agresor, true para el defensor
	private BoardPanel bPanel;
	private BoardFrame bFrame;
	private boolean proccessing = false;
	private boolean needFrom = true;
	private boolean gameOver = false;
	private Coordinates from;
	private Coordinates to;


	public Game(boolean turn, int size, String state,boolean prune,boolean byTime, int data, boolean toDotFile) {
		this.turn = turn;
		this.b = new Board(size, state.toCharArray(),prune,byTime, data,toDotFile);
		bFrame = new BoardFrame(this);
		bPanel = new BoardPanel(this);
	}

	
	public boolean handleMovePlayer(Coordinates c) {
		int code;
		if (needFrom) {
			code=b.getMoveManager().checkFrom(c, turn);
			if (code != 0) {
				Messages.informErrorFrom(code, this);
				return false;
			}
			b.getBoard()[c.getX()][c.getY()].select();
			sendMessage("Escoge el destino...", Color.BLACK);
			from = new Coordinates(c.getX(), c.getY());
			needFrom = !needFrom;
			return false;
		} else {
			code=b.getMoveManager().verifyMove(from, c, turn);
			if (code != 0) {
				if (code == 2) {
					b.getBoard()[c.getX()][c.getY()].select();
					sendMessage("Escoge el origen...", Color.BLACK);
					needFrom = !needFrom;
				}
				Messages.informErrorTo(code, this);
				return false;
			}
			b.getBoard()[from.getX()][from.getY()].select();
			to = new Coordinates(c.getX(), c.getY());
			b.getMoveManager().makeMove(new Move(from, to), turn);
			bPanel.refresh();
			if (checkGameOver(turn))
				return false;
			turn = !turn;
			needFrom = !needFrom;
			sendMessage("Procesando", Color.BLUE);
			return true;

		}
	}

	public void handleMoveMinimax() {
		proccessing = true;
		boolean turnForMinimax = turn;
		Move minimaxMove=b.getMinimaxAlgorithm().executeMinimax(b, turnForMinimax);
		b.getMoveManager().makeMove(minimaxMove, turn);
		if (checkGameOver(turn))
			return;
		turn = !turn;
		proccessing = false;
	}

	
	private boolean checkGameOver(boolean turn){
		int code=b.getStatusManager().checkGameOver(turn);
		if (code==1){
			sendMessage("HA GANADO EL DEFENSOR !!", Color.magenta);
			gameOver = true;
			return true;
		}
		if (code==2){
			sendMessage("HA GANADO EL ATACANTE !!", Color.magenta);
			gameOver = true;
			return true;
		}
		if (code==3){
			sendMessage("EMPATE", Color.magenta);
			gameOver = true;
			return true;
		}
		sendMessage("Escoge el origen...", Color.BLACK);
		return false;
	}

	public void setVisibleGUI(boolean t) {
		bFrame.setVisible(t);
		bPanel.setVisible(t);
	}

	public void sendMessage(String m, Color c) {
		bFrame.sendMessage(m, c);
	}

	public void clearMessage() {
		bFrame.sendMessage("", Color.BLACK);
	}

	public Board getBoard() {
		return b;
	}


	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isProccessing() {
		return proccessing;
	}
	
	public boolean getTurn() {
		return turn;
	}

}
