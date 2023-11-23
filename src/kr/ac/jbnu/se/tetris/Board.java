package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private HeavyBlock heavyBlock;
	private int heavyBlockCount;
	private static final int HEAVY_BLOCK_DELAY = 30;
	private int heavyBlockCurrentRow;
	final int BoardWidth = 13;
	final int BoardHeight = 26;

	private Timer heavyBlockTimer;
	Timer timer;
	boolean isFallingFinished = false;
	boolean isStarted = false;
	boolean isPaused = false;
	int numLinesRemoved = 0;
	int curX = 0;
	int curY = 0;
	JLabel statusbar;
	Shape curPiece;
	Tetrominoes[] board;

	public Board(Tetris parent) {

		heavyBlock = new HeavyBlock();
		heavyBlockCount = 50;

		setFocusable(true);
		curPiece = new Shape();
		timer = new Timer(400, this);
		timer.start();

		statusbar = parent.getStatusBar();
		board = new Tetrominoes[BoardWidth * BoardHeight];
		addKeyListener(new TAdapter());
		clearBoard();

	}



	public void actionPerformed(ActionEvent e) {
		if (isFallingFinished) {

			isFallingFinished = false;


		}
			oneLineDown();
	}

	int squareWidth() {
		return (int) getSize().getWidth() / BoardWidth;
	}

	int squareHeight() {
		return (int) getSize().getHeight() / BoardHeight;
	}

	Tetrominoes shapeAt(int x, int y) {
		return board[(y * BoardWidth) + x];
	}

	public void start() {
		if (isPaused)
			return;

		isStarted = true;
		isFallingFinished = false;
		numLinesRemoved = 0;
		clearBoard();

		newPiece();

		timer.start();
	}

	private void pause() {
		if (!isStarted)
			return;

		isPaused = !isPaused;
		if (isPaused) {
			timer.stop();
			statusbar.setText("paused");
		} else {
			timer.start();
			heavyBlockTimer.start();
			statusbar.setText(String.valueOf(numLinesRemoved));
		}
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);

		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

		//보드에서 블럭 떨어지는 곳 바운더리 선
		g.setColor(Color.BLACK);
		g.drawRect(0, boardTop, BoardWidth * squareWidth(), BoardHeight * squareHeight());


		for (int i = 0; i < BoardHeight; ++i) {
			for (int j = 0; j < BoardWidth; ++j) {
				Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
				if (shape != Tetrominoes.NoShape)
					drawSquare(g, 0 + j * squareWidth(), boardTop + i * squareHeight(), shape);
			}
		}

		if (curPiece.getShape() != Tetrominoes.NoShape) {
			for (int i = 0; i < 4; ++i) {
				int x = curX + curPiece.x(i);
				int y = curY - curPiece.y(i);
				drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
						curPiece.getShape());
			}
		}
	}

	private void dropDown() {
		int newY = curY;
		while (newY > 0) {
			if (!tryMove(curPiece, curX, newY - 1))
				break;
			--newY;
		}
		pieceDropped();
	}

	private void oneLineDown() {
		if (!tryMove(curPiece, curX, curY - 1))
			pieceDropped();
	}

	private void clearBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i)
			board[i] = Tetrominoes.NoShape;
	}

	private void pieceDropped() {
		for (int i = 0; i < 4; ++i) {
			int x = curX + curPiece.x(i);
			int y = curY - curPiece.y(i);
			board[(y * BoardWidth) + x] = curPiece.getShape();
		}

		if(curPiece.getShape() != Tetrominoes.HeavyBlockShape){
			removeFullLines();
		} else if (curPiece.getShape() == Tetrominoes.HeavyBlockShape) {
			HeavyBlockDown();
			removeFullLines();
		}


		if (!isFallingFinished)
			newPiece();
			System.out.println("pieceDroped newPiece");

	}

	private void newPiece() {
		curPiece.setRandomShape();
		curX = BoardWidth / 2 + 1;
		curY = BoardHeight - 1 + curPiece.minY();

		if (!tryMove(curPiece, curX, curY)) {
			curPiece.setShape(Tetrominoes.NoShape);
			timer.stop();
			isStarted = false;
			statusbar.setText("game over");
		}
	}

	private boolean tryMove(Shape newPiece, int newX, int newY) {
		for (int i = 0; i < 4; ++i) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape)
				return false;

		}

		curPiece = newPiece;
		curX = newX;
		curY = newY;
		repaint();
		return true;

	}

	private void removeFullLines() {
		int numFullLines = 0;

		for (int i = BoardHeight - 1; i >= 0; --i) {
			boolean lineIsFull = true;

			for (int j = 0; j < BoardWidth; ++j) {
				if (shapeAt(j, i) == Tetrominoes.NoShape) {
					lineIsFull = false;
					break;
				}
			}

			if (lineIsFull) {
				++numFullLines;
				for (int k = i; k < BoardHeight - 1; ++k) {
					for (int j = 0; j < BoardWidth; ++j)
						board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
				}
			}
		}

		if (numFullLines > 0) {
			numLinesRemoved += numFullLines;
			statusbar.setText(String.valueOf(numLinesRemoved));
			isFallingFinished = true;
			curPiece.setShape(Tetrominoes.NoShape);
			repaint();
		}
	}

	private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
				new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204),
				new Color(218, 170, 0), new Color(0,0, 0) };

		Color color = colors[shape.ordinal()];

		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);

		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	}

	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			if (!isStarted || curPiece.getShape() == Tetrominoes.NoShape) {
				return;
			}

			int keycode = e.getKeyCode();

			if (keycode == 'p' || keycode == 'P') {
				pause();
				return;
			}

			if (isPaused)
				return;

			switch (keycode) {
			case KeyEvent.VK_LEFT:
				tryMove(curPiece, curX - 1, curY);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(curPiece, curX + 1, curY);
				break;
			case KeyEvent.VK_DOWN:
				tryMove(curPiece.rotateRight(), curX, curY);
				break;
			case KeyEvent.VK_UP:
				tryMove(curPiece.rotateLeft(), curX, curY);
				break;
			case KeyEvent.VK_SPACE:
				dropDown();
				break;
			case 'd':
				oneLineDown();
				break;
			case 'D':
				oneLineDown();
				break;
			case '1' :
				restoredHeavyBlock();
				break;
			}

		}
	}

	private void restoredHeavyBlock() {

		if (heavyBlockCount > 0 ) {

			curPiece.setShape(Tetrominoes.HeavyBlockShape);
			heavyBlockCount--;

			curX = BoardWidth / 2;
			curY = BoardHeight - 1 - curPiece.minY();
			System.out.println("call heavyBlock");

		}


	}


	// 헤비 블록 이동 시작
	private void HeavyBlockDown() {
		heavyBlockCurrentRow = BoardHeight - 1;
		heavyBlockTimer = new Timer(HEAVY_BLOCK_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (heavyBlockCurrentRow > 0) {
					heavyBlockDownStep(heavyBlockCurrentRow);
					heavyBlockCurrentRow--;
				} else {
					((Timer) e.getSource()).stop();
					isFallingFinished = true;
				}
			}
		});
		heavyBlockTimer.start();
		timer.start();

	}

	// 헤비 블록 한 단계 아래로 이동
	private void heavyBlockDownStep(int currentRow) {
		System.out.println("2");
		for (int j = 0; j < BoardWidth; ++j) {
			System.out.println("2. for문");
			if (shapeAt(j, currentRow) == Tetrominoes.HeavyBlockShape) {
				// 현재 위치의 헤비 블록을 제거합니다.
				board[(currentRow * BoardWidth) + j] = Tetrominoes.NoShape;
				repaint();

				// 헤비 블록을 아래로 이동합니다.
				int newRow = currentRow - 1;
				if (newRow >= 0) {
					board[(newRow * BoardWidth) + j] = Tetrominoes.HeavyBlockShape;
				}
			}
		}
		repaint();
	}



}