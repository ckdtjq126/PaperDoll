import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Main {
	public static void main(String[] args) {		

		SpriteCanvas canvas = new SpriteCanvas();

		JFrame f = new JFrame("Paper Doll");
		f.setJMenuBar(Main.makeMenuBar(canvas, f));
		f.getContentPane().add(canvas);
		f.getContentPane().setLayout(new GridLayout(1,1));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400, 600);
		f.setLocation(200, 100);
		f.setVisible(true);

		canvas.addSprite(Main.makeSprite(f));
	}
	
	/* Make a sample sprite for testing purposes. */
	private static Sprite makeSprite(JFrame f) {

		Sprite body = new RectangleSprite(60, 120, typeOfBody.BODY);
		Sprite head = new EllipseSprite(40, 60, body, typeOfBody.HEAD, body.getWidth()/2,-10);
		body.transform(AffineTransform.getTranslateInstance(f.getWidth()/2 - body.getWidth()/2, 100));
		head.transform(AffineTransform.getTranslateInstance(body.getWidth()/2 - head.getWidth()/2, -70));
		
		Sprite leftUpperArm = new EllipseSprite(20, 65, body, typeOfBody.UPPERARM, 0,10);
		Sprite leftLowerArm = new EllipseSprite(20, 60, leftUpperArm, typeOfBody.LOWERARM,leftUpperArm.getWidth()/2,leftUpperArm.getHeight());
		Sprite leftHand = new EllipseSprite(20, 20, leftLowerArm, typeOfBody.HAND ,leftUpperArm.getWidth()/2,leftLowerArm.getHeight());
		leftUpperArm.transform(AffineTransform.getTranslateInstance(-1*leftUpperArm.getWidth()/2, 5));
		leftUpperArm.transform(AffineTransform.getRotateInstance(Math.PI/360 * 80));
		leftLowerArm.transform(AffineTransform.getTranslateInstance(0, leftUpperArm.getHeight()));
		leftHand.transform(AffineTransform.getTranslateInstance(0, leftLowerArm.getHeight()));
		
		Sprite rightUpperArm = new EllipseSprite(20, 65, body, typeOfBody.UPPERARM, body.getWidth(),10);
		Sprite rightLowerArm = new EllipseSprite(20, 60, rightUpperArm, typeOfBody.LOWERARM, rightUpperArm.getWidth()/2, rightUpperArm.getHeight());
		Sprite rightHand = new EllipseSprite(20, 20, rightLowerArm, typeOfBody.HAND, rightUpperArm.getWidth()/2,rightLowerArm.getHeight());
		rightUpperArm.transform(AffineTransform.getTranslateInstance(body.getWidth()-rightUpperArm.getWidth()/2+5,16));
		rightUpperArm.transform(AffineTransform.getRotateInstance(Math.PI/360 * -80));
		rightLowerArm.transform(AffineTransform.getTranslateInstance(0,rightUpperArm.getHeight()));
		rightHand.transform(AffineTransform.getTranslateInstance(0, rightLowerArm.getHeight()));
		
		Sprite leftUpperLeg = new EllipseSprite(20,70, body, typeOfBody.LEG,10,body.getHeight());
		Sprite leftLowerLeg = new EllipseSprite(20,70, leftUpperLeg, typeOfBody.LEG, leftUpperLeg.getWidth()/2, leftUpperLeg.getHeight());
		Sprite leftFoot = new EllipseSprite(45,15, leftLowerLeg, typeOfBody.FOOT, leftLowerLeg.getWidth()/2,leftLowerLeg.getHeight());
		leftUpperLeg.transform(AffineTransform.getTranslateInstance(0, body.getHeight()));
		leftLowerLeg.transform(AffineTransform.getTranslateInstance(0, leftUpperLeg.getHeight()));
		leftFoot.transform(AffineTransform.getTranslateInstance(-1*leftFoot.getWidth()+leftLowerLeg.getWidth(), leftLowerLeg.getHeight()));

		Sprite rightUpperLeg = new EllipseSprite(20,70, body, typeOfBody.LEG,body.getWidth()-10,body.getHeight());
		Sprite rightLowerLeg = new EllipseSprite(20,70, rightUpperLeg, typeOfBody.LEG, rightUpperLeg.getWidth()/2, rightUpperLeg.getHeight());
		Sprite rightFoot = new EllipseSprite(45,15, rightLowerLeg, typeOfBody.FOOT, rightLowerLeg.getWidth()/2, rightLowerLeg.getHeight());
		rightUpperLeg.transform(AffineTransform.getTranslateInstance(body.getWidth()-rightUpperLeg.getWidth(), body.getHeight()));
		rightLowerLeg.transform(AffineTransform.getTranslateInstance(0, rightUpperLeg.getHeight()));
		rightFoot.transform(AffineTransform.getTranslateInstance(0, rightLowerLeg.getHeight()));

		return body;
	}

	/* Menu with recording and playback. */
	private static JMenuBar makeMenuBar(final SpriteCanvas canvas, JFrame f) {
		JMenuBar mbar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu script = new JMenu("Scripting");
		
		final JFrame frame = f;
		final JMenuItem reset = new JMenuItem("Reset (Ctrl - R)");
		final JMenuItem quit = new JMenuItem("Quit");
		final JMenuItem record = new JMenuItem("Start recording");
		final JMenuItem play = new JMenuItem("Start script");

		file.add(reset);
		file.addSeparator();
		file.add(quit);
		
		script.add(record);
		script.add(play);

		f.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){}
			public void keyPressed(KeyEvent e) {
				// ctrl + r
				if ((e.getKeyCode() == KeyEvent.VK_R) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) && reset.isEnabled()) {
					canvas.reset(Main.makeSprite(frame));
				}
				// if 'q' is pressed, it quits
				else if((e.getKeyCode() == KeyEvent.VK_Q))
					canvas.quit(frame);
			}
	       public void keyReleased(KeyEvent e) {}
		});
		
		record.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (record.getText().equals("Start recording")) {
					record.setText("Stop recording");
					canvas.reset(Main.makeSprite(frame));
					canvas.startRecording();
				} else if (record.getText().equals("Stop recording")) {
					record.setText("Start recording");
					canvas.stopRecording();
				} else {
					assert false;
				}
			}
		});
		reset.addActionListener(new ActionListener(	) {
			public void actionPerformed(ActionEvent e) {
				if(reset.isEnabled())
					canvas.reset(Main.makeSprite(frame));
			}
		});
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.quit(frame);
			}
		});
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (play.getText().equals("Start script")) {
					play.setText("Stop script");
					reset.setEnabled(false);
					record.setEnabled(false);
					canvas.startDemo(Main.makeSprite(frame));
				} else if (play.getText().equals("Stop script")) {
					play.setText("Start script");
					record.setEnabled(true);
					reset.setEnabled(true);
					canvas.stopDemo(Main.makeSprite(frame));
				} else {
					assert false;
				}
			}
		});
		

		mbar.add(file);
		mbar.add(script);
		return mbar;
	}

}

