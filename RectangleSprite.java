

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;


/**
 * A simple demo of how to create a rectangular sprite.
 * 
 * Michael Terry
 */


// just for body part
public class RectangleSprite extends Sprite {
	
    
    private RoundRectangle2D rect = null;     

    /**
     * Creates a RoundRectangle based at the origin with the specified
     * width and height
     */
    public RectangleSprite(int width, int height, typeOfBody type) {
        super();
        this.initialize(width, height, type);
    }
    /**
     * Creates a rectangle based at the origin with the specified
     * width, height, and parent
     */
    public RectangleSprite(int width, int height, Sprite parentSprite, typeOfBody type) {
        super(parentSprite);
        this.initialize(width, height, type);
    }
    
    private void initialize(int width, int height, typeOfBody type) {
        rect = new RoundRectangle2D.Double(0, 0, width, height, 30,	20);
        this.bodyType = type;
    }
    
	public int getWidth() {
		return (int)rect.getWidth();
	}

	public int getHeight() {
		return (int)rect.getHeight();
	}
	
	public typeOfBody getBodyType() {
		return this.bodyType;
	}         
    
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
        return rect.contains(newPoint);
    }
    
    protected void drawSprite(Graphics2D g) {
    	Graphics2D g2 = (Graphics2D)g;
    	g2.setColor(Color.CYAN);
    	g2.fill(this.rect);
        g.setColor(Color.BLACK);
        g.draw(rect);
    }
    
    public String toString() {
        return "RectangleSprite: " + rect;
    }
}

