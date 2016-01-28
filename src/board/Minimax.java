package board;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Minimax {

	private boolean prune;
	private boolean byTime;
	private long time;
	private int depth;
	private boolean toDotFile;
	private PrintWriter writer;
	private File file;
	private long count = 0;
	private long startTime;
	private int podaId = 0;

	public Minimax(boolean prune, boolean byTime, int data, boolean toDotFile) {
		this.prune = prune;
		this.byTime = byTime;
		this.time = data;
		this.depth = data;
		this.toDotFile = toDotFile;
		if (toDotFile)
			try {
				if (byTime) {
					file = new File("tree_aux.dot");
					writer = new PrintWriter(file);
					writer.println("digraph TREE {");
				} else {
					file = new File("tree.dot");
					writer = new PrintWriter(file);
					writer.println("digraph TREE {");
				}
			} catch (FileNotFoundException e) {
				System.out.println("Error al generar el buffer de escritura");
			}
	}

	public Move executeMinimax(Board b, boolean side) {
		Move m = null;
		if (byTime) {
			m = minimaxByTime(b, side);
		} else {
			m = minimaxDepth(b, depth, side, new Move(null, null, 0,
					Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1), true);
		}
		if (toDotFile)
			closeBuffer();
		return m;

	}

	public Move minimaxDepth(Board currentBoard, int currentDepth,
			boolean side, Move move, boolean max) {
		if (toDotFile)
			printNode(move, max);
		count++;
		if (byTime && count % 100 == 0) {
			if (System.currentTimeMillis() - startTime > time * 1000)
				return null;
		}
		if (currentBoard.getStatusManager().checkGameOver(!side) > 0
				|| currentDepth == 0) {
			int w = currentBoard.getWeight();
			if (side)
				w = (-1) * w;
			return new Move(null, null, w);
		}
		int maxValue = Integer.MIN_VALUE;
		Move best = null;
		for (Move m : currentBoard.getMoveManager().getPossibleMoves(side)) {
			m.setAlpha(move.getAlpha());
			m.setBeta(move.getBeta());
			Board aux = currentBoard.copyBoard();
			aux.getMoveManager().makeMove(m, side);
			Move current = minimaxDepth(aux, currentDepth - 1, !side, m, !max);
			if (byTime && current == null)
				return null;
			int actual = current.getWeight();
			if (toDotFile)
				printArcs(move, m, actual, max);
			if (maxValue < actual) {
				move.updateParameters(max, actual);
				maxValue = actual;
				best = m;
				best.setWeight(actual);
			}
			if (prune && move.hasToPrune(max, actual)) {
				if (toDotFile)
					printPrune(move);
				break;
			}
		}
		if (toDotFile) {
			printWeight(move, maxValue);
			printHightLight(best);
		}
		maxValue = maxValue * (-1);
		return new Move(best.getFrom(), best.getTo(), maxValue);
	}

	public Move minimaxByTime(Board b, boolean side) {
		Move m = null;
		int level = 1;
		boolean timeIsOver = false;
		startTime = System.currentTimeMillis();
		count = 0;
		while (!timeIsOver) {
			Move current;
			current = minimaxDepth(b, level, side, new Move(null, null, 0,
					Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1) , true);
			level++;
			if (current == null) {
				timeIsOver = true;
			} else {
				if (toDotFile) {
					
					closeBuffer();
					file.renameTo(new File("tree.dot"));
					file = new File("tree_aux.dot");
					try {
						writer = new PrintWriter(file);
						writer.println("digraph TREE {");
					} catch (FileNotFoundException e) {
						System.out
								.println("Error al cargar los buffers de escritura");
					}
				}
				m = current;
			}
		}
		if (toDotFile)
			file.delete();
		return m;

	}

	private void printPrune(Move move) {
		writer.println("{" + podaId
				+ "[label=\"PODA\" style=filled shape=circle fillcolor=green]}");
		writer.println(move.hashForPrint() + "->" + podaId);
		podaId++;
	}

	private void printHightLight(Move best) {
		writer.println("{" + best.hashForPrint() + "[ fillcolor=red ]}");
	}

	public void closeBuffer() {
		writer.println("}");
		writer.close();
	}

	private void printWeight(Move move, int maxValue) {
		writer.println("{");
		String n = "";
		n += move.hashForPrint() + "[";
		if (move.getFrom() != null)
			n += " label=\"" + move.toString() + " " + maxValue + "\"]";
		else {
			n += " label=\"START " + maxValue + "\"]";
		}

		writer.println(n);
		writer.println("}");

	}

	private void printArcs(Move move, Move m, int actual, boolean max) {
		if (max)
			printWeight(m, actual);
		else
			printWeight(m, (-1) * actual);
		writer.println(move.hashForPrint() + "->" + m.hashForPrint() + ";");
	}

	private void printNode(Move move, boolean max) {
		writer.println("{");
		String n = "";
		n += move.hashForPrint() + "[ style=filled ";
		if (max)
			n += " shape=box";
		else
			n += " shape=oval";
		if (move.getFrom() != null)
			n += " label=\"" + move.toString() + "\"]";
		else
			n += " label=\"START\"]";
		writer.println(n);
		writer.println("}");

	}

}
