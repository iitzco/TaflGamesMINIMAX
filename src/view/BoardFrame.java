package view;
import game.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;





public class BoardFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private Game game;
	private BoardPanel boardPanel;

	private JPanel stats;
	private JLabel turn;
	private JPanel alert;
	private JLabel message;
	

	public BoardFrame(Game g) {
		super("La fuga del rey");
		this.game=g;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		this.setContentPane(boardPanel = new BoardPanel(g));
		this.setSize(boardPanel.getWidth(), boardPanel.getHeight() + 80);
		this.setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);

		
		

		stats = new JPanel();
		stats.setLayout(new FlowLayout());
		stats.setBounds(0, boardPanel.getHeight(), boardPanel.getWidth(), 30);
		JLabel lscore = new JLabel("Turno: ");
		turn = new JLabel();
		stats.add(lscore);
		stats.add(turn);
		add(stats);
		alert = new JPanel();
		alert.setLayout(new FlowLayout());
		alert.setBounds(0,boardPanel.getHeight()+30, boardPanel.getWidth(), 40);
		message = new JLabel("");
		alert.add(message);
		add(alert);
		setTurn(game.getTurn());
		sendMessage("Escoge el origen...",Color.BLACK);

	}
	
	//true para defensor, false para atacante
	public void setTurn(boolean turn){
		if (turn)
			this.turn.setText("Defensor");
		else
			this.turn.setText("Atacante");
	}
	
	public void sendMessage(String m,Color c){
		message.setForeground(c);
		message.setText(m);
	}

	
}

