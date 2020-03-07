import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;

import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Font;

public class BubblePanel extends JPanel {
	Random rand = new Random();
	ArrayList<Bubble> bubbleList;
	int size = 25;
	Timer timer;
	int delay = 33;
	JSlider slider;
	
	public BubblePanel() {
		timer = new Timer(delay, new BubbleListener());
		bubbleList = new ArrayList<Bubble>();
		setBackground(Color.black);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 128, 128));
		add(panel);
		
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton)e.getSource();
				if (btn.getText().equals("Pause")) {
					timer.stop();
					btn.setText("Start");
				}
				else {
					timer.start();
					btn.setText("Pause");
				}
			}
		});
		
		JLabel lblNewLabel = new JLabel("FPS");
		lblNewLabel.setForeground(new Color(211, 211, 211));
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 36));
		panel.add(lblNewLabel);
		
		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int speed = slider.getValue() + 1;
				int delay = 1000 / speed;
				timer.setDelay(delay);
			}
		});
		slider.setValue(30);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(30);
		slider.setMaximum(120);
		panel.add(slider);
		panel.add(btnPause);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bubbleList = new ArrayList<Bubble>();
				repaint();
			}
		});
		panel.add(btnClear);
//		testBubbles();
		addMouseListener(new BubbleListener());
		addMouseMotionListener(new BubbleListener());
		addMouseWheelListener(new BubbleListener());
		timer.start();
	}
	public void paintComponent(Graphics canvas) {
		super.paintComponent(canvas);
		for (Bubble b : bubbleList) {
			b.draw(canvas);
		}
	}
	
	public void testBubbles() {
		for (int n = 0; n < 100; n++) {
			int x = rand.nextInt(600);
			int y = rand.nextInt(400);
			int size = rand.nextInt(50);
			bubbleList.add(new Bubble(x, y, size));
		}
		repaint();
	}
	
	private class BubbleListener extends MouseAdapter implements ActionListener {
		public void mousePressed(MouseEvent e) {
			bubbleList.add(new Bubble(e.getX(), e.getY(), size));
			repaint();
		}
		
		public void mouseDragged(MouseEvent e) {
			bubbleList.add(new Bubble(e.getX(), e.getY(), size));
			repaint();
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			size += e.getUnitsToScroll();
		}
		
		public void actionPerformed(ActionEvent e) {
			for (Bubble b : bubbleList) {
				b.update();
			}
			repaint();
		}
	}
	
	private class Bubble {
		private int x;
		private int y;
		private int size;
		private Color color;
		private int xspeed, yspeed;
		private final int maxSpeed = 5;
		
		public Bubble (int newX, int newY, int newSize) {
			this.x = newX;
			this.y = newY;
			this.size = newSize;
			this.color = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256), rand.nextInt(256));
			this.xspeed = rand.nextInt(maxSpeed * 2 + 1) - maxSpeed;
			this.yspeed = rand.nextInt(maxSpeed * 2 + 1) - maxSpeed;
		}
		
		public void draw (Graphics canvas) {
			canvas.setColor(color);
			canvas.fillOval(this.x - this.size / 2, this.y - this.size / 2, size, size);
		}
		
		public void update () {
			if (xspeed == 0) {
				this.xspeed = rand.nextInt(maxSpeed * 2 + 1) - maxSpeed;
			}
			if (yspeed == 0) {
				this.yspeed = rand.nextInt(maxSpeed * 2 + 1) - maxSpeed;
			}
			if (this.x - this.size / 2<= 0 || this.x + this.size/2>= getWidth()) {
				this.xspeed = -this.xspeed;
			}
			if (this.y - this.size / 2 <= 0 || this.y + this.size/2 >= getHeight()) {
				this.yspeed = -this.yspeed;
			}
			this.x += xspeed;
			this.y -= yspeed;
		}
	}
}

