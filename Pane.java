import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Pane extends JFrame {

	Pane() {
		this.setSize(800, 600);
		this.setBackground(Color.BLACK);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		final DrawGraphe dp = new DrawGraphe();
		add(dp);

		MenuBar menuBar = new MenuBar();
		Menu jmb = new Menu("Edition");
		MenuItem mi1 = new MenuItem("Remise à zéro");
		MenuItem mi2 = new MenuItem("Quit");
		jmb.add(mi1);
		jmb.addSeparator();
		jmb.add(mi2);
		menuBar.add(jmb);
		this.setMenuBar(menuBar);

		mi1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dp.rightclick=false;
				
				dp.removenb();
				
				// TODO Auto-generated method stub
				while (dp.squareCount != 0) {
					dp.remove(dp.squareCount);
					dp.squareCount--;
					
					
				}
				dp.u=1;
				dp.repaint();
			};

		});
		mi2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);

			}
		});

	}

	public static void main(String[] args) {
		new Pane().setVisible(true);
	}

}
