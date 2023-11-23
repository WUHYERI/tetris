package kr.ac.jbnu.se.tetris;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tetris extends JFrame {

	JLabel statusbar;
	Board board;

	public Tetris() {
		this.setLayout(null);
		statusbar = new JLabel(" 0");
		add(statusbar);
		Board board = new Board(this);
		board.setBounds(250,50,300,600);
		add(board);
		board.start();

		setSize(800, 800);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);


	}

	public JLabel getStatusBar() {
		return statusbar;
	}
	public Board getGameBoard() { return board; }
	public void startGame() { board.start(); }



	public static void main(String[] args) {
		Tetris game = new Tetris();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
		game.startGame();
	}
}