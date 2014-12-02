

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

/**
 * A simple demo of how to create a rectangular sprite.
 * 
 * Michael Terry
 */
public class EllipseSprite extends Sprite {
	
    private Ellipse2D ellip = null;
	private int anchorX;
	private int anchorY;

    /**
     * Creates a rectangle based at the origin with the specified
     * width and height
     */
    public EllipseSprite(int width, int height, typeOfBody type, int anchorX, int anchorY) {
        super();
        this.initialize(width, height, type, anchorX, anchorY);
    }
    /**
     * Creates a rectangle based at the origin with the specified
     * width, height, and parent
     */
    public EllipseSprite(int width, int height, Sprite parentSprite, typeOfBody type, int anchorX, int anchorY) {
        super(parentSprite);
        this.initialize(width, height, type, anchorX, anchorY);
    }
    
    private void initialize(int width, int height, typeOfBody type, int anchorX, int anchorY) {
        ellip = new Ellipse2D.Double(0, 0, width, height);
        this.bodyType = type;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

	public int getWidth() {
		return (int)ellip.getWidth();
	}
	public int getHeight() {
		return (int)ellip.getHeight();
	}
	public typeOfBody getBodyType() {
		return this.bodyType;
	}
	public int getAnchorX(){
		return this.anchorX;
	}
	public int getAnchorY(){
		return this.anchorY;
	}
         
    /**
     * Test if our ellipse contains the point specified.
     */
    public boolean pointInside(Point2D p) {
        AffineTransform fullTransform = this.getFullTransform();
        AffineTransform inverseTransform = null;
        try {
            inverseTransform = fullTransform.createInverse();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        Point2D newPoint = (Point2D)p.clone();
        inverseTransform.transform(newPoint, newPoint);
        return ellip.contains(newPoint);
    }
    
protected void drawSprite(Graphics2D g) {
    	Graphics2D g2 = (Graphics2D)g;

        g.setColor(Color.BLACK);
        
        g.draw(ellip);
        if(this.bodyType == typeOfBody.HEAD){
            g2.setColor(Color.BLACK);
            g2.fill(this.ellip);        	
        }
        else if(this.bodyType == typeOfBody.UPPERARM){
            g2.setColor(Color.RED);
            g2.fill(this.ellip);        	
        }
        else if(this.bodyType == typeOfBody.LOWERARM){
            g2.setColor(Color.GREEN);
            g2.fill(this.ellip);        	
        }
        else if(this.bodyType == typeOfBody.HAND){
            g2.setColor(Color.BLUE);
            g2.fill(this.ellip);        	
        }
        else if(this.bodyType == typeOfBody.LEG){
            g2.setColor(Color.ORANGE);
            g2.fill(this.ellip);        	
        }
        else if(this.bodyType == typeOfBody.FOOT){
            g2.setColor(Color.YELLOW);
            g2.fill(this.ellip);        	
        }
    }
    
    public String toString() {
        return "RectangleSprite: " + ellip;
    }
}

