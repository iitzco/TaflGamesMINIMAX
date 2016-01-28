package view;

import game.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import board.Cell;
import board.Coordinates;

public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int CELL_SIZE = 35;

	private ImageManager imgManager;
	private Image[][] images;
	private int rows, columns;
	private int height;
	private int width;
	private Game game;
	private Coordinates coord;

	public BoardPanel(Game g) {
		this.game = g;
		this.rows = game.getBoard().getHeight();
		this.columns = game.getBoard().getWidth();
		this.imgManager = new ImageManager();
		this.images = new Image[rows][columns];
		this.height = CELL_SIZE * rows;
		this.width = CELL_SIZE * columns;
		setLayout(null);
		setSize(width, height);
		refresh();

		addMouseListener(new MouseListener() {

		
			public void mouseClicked(MouseEvent e) {
				if (game.isGameOver() || game.isProccessing())
					return;
				int row = e.getY() / CELL_SIZE;
				int column = e.getX() / CELL_SIZE;
				coord = new Coordinates(row, column);	
				if (game.handleMovePlayer(coord)){
					refresh();
					SwingUtilities.invokeLater(new Runnable() {						
						@Override
						public void run() {							
							game.handleMoveMinimax();
							refresh();
							return;
						}
					});					
				}
				refresh();
			}

			
			public void mousePressed(MouseEvent e) {

			}

			
			public void mouseReleased(MouseEvent e) {

			}

	
			public void mouseEntered(MouseEvent e) {

			}

			
			public void mouseExited(MouseEvent e) {

			}

		});
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, columns * CELL_SIZE, rows * CELL_SIZE);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (images[i][j] != null) {
					g.drawImage(images[i][j], j * CELL_SIZE, i * CELL_SIZE,
							null);
				}
			}
		}

	}

	public void put(Image image, int row, int column) {
		images[row][column] = image;
	}

	public void clear(int row, int column) {
		images[row][column] = null;
	}

	public void refresh() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				put(imgManager.get(getImageBase(i, j), getImagePiece(i, j),
						game.getBoard().getBoard()[i][j]), i, j);
			}
		}
		repaint();
	}

	public String getImageBase(int i, int j) {
		Cell cell = game.getBoard().getBoard()[i][j];
		if (game.getBoard().isCastle(cell))
			return "C";
		if (game.getBoard().isThrone(cell))
			return "T";
		return "CELL";
	}

	public String getImagePiece(int i, int j) {
		Cell cell = game.getBoard().getBoard()[i][j];
		if (cell.isEmpty()) {
			return "";
		} else {
			if (cell.getCellContent().isKing())
				return "K";
			if (cell.getCellContent().isGuard())
				return "G";
			// if (cell.getCellContent() instanceof Enemy)
			return "E";
		}
	}

	public Coordinates getCoord() {
		return coord;
	}

}
