package Engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	/**
	 * Default Serial ID. You should probably change it.
	 */
	private static final long serialVersionUID = 1L;

	// DIMENESIONS of the game window
	public static int WIDTH = 800;
	public static int HEIGHT = WIDTH / 16 * 9;
	public static final Dimension DIMENSIONS = new Dimension(WIDTH, HEIGHT);

	// Name of the game (will appear at the top "menu bar"
	public static String NAME = "GAME";

	public Thread thread;
	public JFrame frame;
	public InputHandler input;
	public static Game game;

	public boolean running = false;
	public int tickCount = 0;

	// Initialize the variables
	public void init() {
		game = this;
		running = true;
		input = new InputHandler(game);
	}

	public synchronized void start() {
		running = true;

		thread = new Thread(this, NAME + "_main");
		thread.start();
	}

	public synchronized void stop() {
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Using runnable, controls the thread to run at 60fps
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				update();
				delta -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(ticks + " ticks, " + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}
	}

	// Method used to update your game
	public void update() {

	}

	// Method used to render your game
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g; // Graphics2D implementation given if
											// needed
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Render your game here

		// Dispose
		g.dispose();
		bs.show();
	}

}
