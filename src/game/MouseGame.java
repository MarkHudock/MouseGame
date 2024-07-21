package game;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class MouseGame extends JPanel {

	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 500;

	private int score = 0;
	private int missedClicks = 0;
	private int totalClicks = 0;

	private long totalReactionTime = 0;
	private long reactionTime = 0;
	private long clickTime = System.currentTimeMillis();

	private Rectangle rect = new Rectangle(FRAME_WIDTH / 2, (FRAME_HEIGHT / 2) + 10, 20, 20);

	private Random random = new Random();

	public MouseGame() {
		MyMouseAdapter mouseAdapter = new MyMouseAdapter();
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setClip(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		g2.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		if (rect == null) {
			return;
		} else {
			g2.draw(rect);
			String scoreString = "Score: " + Integer.toString(score) + " - " + missedClicks;
			g2.drawString(scoreString, (FRAME_WIDTH - g2.getFontMetrics().stringWidth(scoreString)) / 2,
					10 + g2.getFontMetrics().getHeight());

			String reactionString = "Reaction time: " + getReactionTime() + " ms (" + getAverageReactionTime() + ")";
			g2.drawString(reactionString, (FRAME_WIDTH - g2.getFontMetrics().stringWidth(reactionString)) / 2,
					(FRAME_HEIGHT - g2.getFontMetrics().getHeight()) - 10);
			repaint();
		}
	}

	private class MyMouseAdapter extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			int mouseX = e.getPoint().x;
			int mouseY = e.getPoint().y;
			if (mouseX >= rect.getX() && mouseX <= rect.getX() + rect.getWidth()
					&& mouseY >= rect.getY() && mouseY <= rect.getY() + rect.getHeight()) { // Within the rectangles X &
																							// Y coordinates.
				double centerX = rect.getX() + (rect.getWidth() / 2);
				double centerY = rect.getY() + (rect.getHeight() / 2);
				double diffX = Math.abs(mouseX - centerX);
				double diffY = Math.abs(mouseY - centerY);

				score += rect.getWidth() - (diffX + diffY); // Size of the rectangle minus the distance clicked from the
															// center.
				reactionTime = System.currentTimeMillis() - clickTime;
				totalReactionTime += reactionTime;
				totalClicks++;
				clickTime = System.currentTimeMillis();

				// Move the rectangle to a random location
				int randomX = random.nextInt(FRAME_WIDTH - (int) rect.getWidth());
				int randomY = random.nextInt(FRAME_HEIGHT - (int) rect.getHeight());
				while (randomX + (int) rect.getWidth() > FRAME_WIDTH) {
					randomX = random.nextInt(FRAME_WIDTH - (int) rect.getWidth());
				}
				while (randomY + (int) rect.getHeight() > FRAME_HEIGHT) {
					randomY = random.nextInt(FRAME_HEIGHT - (int) rect.getHeight());
				}
				rect.setLocation(randomX, randomY);
			} else {
				missedClicks++;
				score -= rect.getWidth();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// drawing = true;
			// int x = Math.min(mousePress.x, e.getPoint().x);
			// int y = Math.min(mousePress.y, e.getPoint().y);
			// int width = Math.abs(mousePress.x - e.getPoint().x);
			// int height = Math.abs(mousePress.y - e.getPoint().y);
			//
			// rect = new Rectangle(x, y, width, height);
			// repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

	}

	private long getReactionTime() {
		return reactionTime;
	}

	private long getAverageReactionTime() {
		if (totalClicks == 0) {
			return 0;
		} else {
			return totalReactionTime / totalClicks;
		}
	}

	private static void createAndShowGui() {
		JFrame frame = new JFrame("MouseGame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MouseGame());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				createAndShowGui();
			}

		});
	}
}