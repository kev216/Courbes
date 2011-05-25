import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.CubicCurve2D;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawGraphe extends JPanel implements MouseMotionListener {

	/**
	 * WANG Lingxiao-IHM
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Point mouse;
	Cursor cross_cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
	int u = 1;
	int t = 0;
	int Max = 100000;
	Rectangle[] squares = new Rectangle[Max];// Point princip
	Rectangle[] ctrl1 = new Rectangle[Max];// point controle 1
	Rectangle[] ctrl2 = new Rectangle[Max];// point controle 2
	int[] nb = new int[Max];// Point princip
	int rightclickcount = 0;
	int squareCount = 0; // compteur de point
	boolean rightclick = false;
	int currentSquareIndex = -1;// indexe de point

	// ***********************************************
	DrawGraphe() {

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				int x = evt.getX();
				int y = evt.getY();
				currentSquareIndex = getSquare(x, y);

				if (SwingUtilities.isLeftMouseButton(evt)
						&& currentSquareIndex < 0) // cet point n'est pas dans le square
					add(x, y);

			}

			// doubleclick : supprimer le point courrant
			public void mouseClicked(MouseEvent evt) {
				int x = evt.getX();
				int y = evt.getY();

				if (SwingUtilities.isLeftMouseButton(evt)
						&& evt.getClickCount() >= 2) {
					System.out.println("Lingxiao");
					nb[u] = squareCount;
					rightclickcount++;
					u++;
					rightclick = true;

				}
				
				repaint();
			}
		});

		addMouseMotionListener(this);
	}

	// ***********************************************

	public void mouseMoved(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();

		if (getSquare(x, y) >= 0)
			setCursor(cross_cursor);// si le Cursor est dans le zone RECTANGLE,
		// il est "cross"
		else
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseDragged(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		Graphics g = getGraphics();

		if (currentSquareIndex >= 0) {
			// Point contrle 1
			if(SwingUtilities.isRightMouseButton(evt)){
				g.setXORMode(getBackground());
				((Graphics2D) g).draw(squares[currentSquareIndex]);
				squares[currentSquareIndex].x = x;
				squares[currentSquareIndex].y = y;
			}
			// "draw" le point principe
			((Graphics2D) g).draw(squares[currentSquareIndex]);
			
			g.setXORMode(getBackground());
			((Graphics2D) g).draw(ctrl1[currentSquareIndex]);
			ctrl1[currentSquareIndex].x = x;
			ctrl1[currentSquareIndex].y = y;
			// Point Control 2
			((Graphics2D) g).draw(ctrl2[currentSquareIndex]);
			ctrl2[currentSquareIndex].x = 2 * squares[currentSquareIndex].x - x;
			ctrl2[currentSquareIndex].y = 2 * squares[currentSquareIndex].y - y;

			g.dispose();
			repaint();

		}

	}

	// ***********************************************

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// definir le ligne qui connecte le point principe et les point controle
		Stroke st = ((Graphics2D) g).getStroke();
		Stroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 16, 4 }, 0);

		/*
		 * "draw" les Rectangles pour tous les points, et les point controle
		 * sont "blue", les points principe sont "red".
		 */

		for (int i = 0; i < squareCount; i++) {

			int x1 = squares[i].x;
			int y1 = squares[i].y;
			int x2 = ctrl1[i].x;
			int y2 = ctrl1[i].y;
			int x3 = ctrl2[i].x;
			int y3 = ctrl2[i].y;

			((Graphics2D) g).setColor(Color.blue.brighter());
			((Graphics2D) g).draw(ctrl1[i]);

			((Graphics2D) g).setColor(Color.blue.brighter());
			((Graphics2D) g).draw(ctrl2[i]);
			((Graphics2D) g).fillOval(ctrl2[i].x, ctrl2[i].y, 5, 5);

			((Graphics2D) g).setColor(Color.red.brighter());
			((Graphics2D) g).draw(squares[i]);
			((Graphics2D) g).fillOval(squares[i].x, squares[i].y, 5, 5);

			/*
			 * "drawLine" entre les points
			 */

			((Graphics2D) g).setStroke(bs);
			((Graphics2D) g).setColor(Color.GREEN.brighter().brighter());
			((Graphics2D) g).drawLine(x1, y1, x2, y2);
			((Graphics2D) g).drawLine(x1, y1, x3, y3);
			((Graphics2D) g).setStroke(st);

		}
		// utilise le methode qui "draw CubicCurve2D"

		drawCub(g);

	}

	// ***********************************************
	// utilise une point a chercher sa rectangle.
	public int getSquare(int x, int y) {
		for (int i = 0; i < squareCount; i++)
			if (squares[i].contains(x, y) || ctrl1[i].contains(x, y))
				return i;

		return -1;

	}

	// ***********************************************

	/*
	 * Apres un "Mouse Press" , on ajoute 3 points en meme temps (point principe
	 * est dans "squares", ctrl1 est dans "ctrl1", et ctrl2 est dans "ctrl2" )
	 */
	public void add(int x, int y) {
		if (squareCount < Max) {
			squares[squareCount] = new Rectangle(x, y, 5, 5);
			ctrl1[squareCount] = new Rectangle(x, y, 5, 5);
			ctrl2[squareCount] = new Rectangle(x, y, 5, 5);
			currentSquareIndex = squareCount;
			squareCount++;
			repaint();
		}
	}

	// **********************************************

	/*
	 * supprimer une point qui situe a le place n.
	 */
	public void remove(int n) {
		if (n < 0 || n >= squareCount)
			return;
		squareCount--;
		squares[n] = squares[squareCount];
		ctrl1[n] = ctrl1[squareCount];
		ctrl2[n] = ctrl2[squareCount];
		if (currentSquareIndex == n)
			currentSquareIndex = -1;
		repaint();

	}

	public void removenb() {
		for (int i = 1; i < rightclickcount+1; i++) {
			nb[i] = 0;
			
		}
		rightclickcount = 0;
	}

	// ***********************************************

	// draw CubicCurve2D
	public void drawCub(Graphics g) {
		CubicCurve2D shape;

		Graphics2D g2D = (Graphics2D) g;

		float X1, Y1, CX1, CY1, CX2, CY2, X2, Y2;

		if (squareCount > 1) {
			if (rightclick == false) {
				for (int i = 0; i < squareCount - 1; i++) {
					X1 = squares[i].x;
					Y1 = squares[i].y;
					CX1 = ctrl1[i].x;
					CY1 = ctrl1[i].y;
					CX2 = ctrl2[i + 1].x;
					CY2 = ctrl2[i + 1].y;
					X2 = squares[i + 1].x;
					Y2 = squares[i + 1].y;

					shape = new CubicCurve2D.Float(X1, Y1, CX1, CY1, CX2, CY2,
							X2, Y2);

					g2D.setColor(Color.white);
					Stroke bs1 = new BasicStroke(2);
					g2D.setStroke(bs1);
					g2D.draw(shape);
				}
			} else if (rightclick == true) {

				for (int i = 0; i < rightclickcount; i++) {

					for (int t = nb[i]; t < nb[i + 1] - 1; t++) {

						X1 = squares[t].x;
						Y1 = squares[t].y;
						CX1 = ctrl1[t].x;
						CY1 = ctrl1[t].y;
						CX2 = ctrl2[t + 1].x;
						CY2 = ctrl2[t + 1].y;
						X2 = squares[t + 1].x;
						Y2 = squares[t + 1].y;

						shape = new CubicCurve2D.Float(X1, Y1, CX1, CY1, CX2,
								CY2, X2, Y2);

						g2D.setColor(Color.white);
						Stroke bs1 = new BasicStroke(2);
						g2D.setStroke(bs1);
						g2D.draw(shape);

					}

				}
				if(squares[nb[rightclickcount]+1] != null){
					for (int a= nb[rightclickcount]; a < squareCount - 1; a++) {
						X1 = squares[a].x;
						Y1 = squares[a].y;
						CX1 = ctrl1[a].x;
						CY1 = ctrl1[a].y;
						CX2 = ctrl2[a + 1].x;
						CY2 = ctrl2[a + 1].y;
						X2 = squares[a + 1].x;
						Y2 = squares[a + 1].y;

						shape = new CubicCurve2D.Float(X1, Y1, CX1, CY1, CX2, CY2,
								X2, Y2);

						g2D.setColor(Color.white);
						Stroke bs1 = new BasicStroke(2);
						g2D.setStroke(bs1);
						g2D.draw(shape);
					}
					}
			}

		}
	}

}
