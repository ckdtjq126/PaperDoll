import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Vector;
import javax.swing.JComponent;

public abstract class Sprite extends JComponent{
    
    /**
     * Tracks our current interaction mode after a mouse-down
     */
    protected enum InteractionMode {
        IDLE,
        DRAGGING,
        SCALING,
        ROTATING
    }

    private Vector<Sprite> children = new Vector<Sprite>();     // Holds all of our children
    private Sprite parent = null;                     // Pointer to our parent
    
    private AffineTransform transform = new AffineTransform();	// Our transformation matrix
    private AffineTransform originalLocation = null;

    protected typeOfBody bodyType;
	// Last mouse point
    protected Point2D lastPoint = null;
    // Current interaction mode
    protected InteractionMode interactionMode = InteractionMode.IDLE;
    
    private double angle = 0;    
    
    public Sprite() {
        ; // no-op
    }
    
    public Sprite(Sprite parent) {
        if (parent != null) {
            parent.addChild(this);
        }
    }

    public void addChild(Sprite s) {
        children.add(s);
        s.setParent(this);
    }
    public Sprite getParent() {
        return parent;
    }
    private void setParent(Sprite s) {
        this.parent = s;
    }
    
    
    public void setOriginalLocation(){
    	originalLocation = new AffineTransform(transform);
    	for(Sprite child: children){
    		child.setOriginalLocation();
    	}
    }
    
	public typeOfBody getBodyType() {
		return this.bodyType;
	}
    
    /**
     * Test whether a point, in world coordinates, is within our sprite.
     */
    public abstract boolean pointInside(Point2D p);

    /**
     * Handles a mouse down event, assuming that the event has already
     * been tested to ensure the mouse point is within our sprite.
     */
    
    protected void handleMouseDownEvent(MouseEvent e) {
        lastPoint = e.getPoint();
        
        boolean isInside = this.pointInside(e.getPoint());
        
        if (e.getButton() == MouseEvent.BUTTON1) {
        	if(isInside && this.getBodyType() != typeOfBody.BODY){
        		interactionMode = InteractionMode.ROTATING;
        	}
        	else if(this.getBodyType() == typeOfBody.BODY){
        		interactionMode = InteractionMode.DRAGGING;
        	}
        }
    }

    /**
     * Handle mouse drag event, with the assumption that we have already
     * been "selected" as the sprite to interact with.
     * This is a very simple method that only works because we
     * assume that the coordinate system has not been modified
     * by scales or rotations. You will need to modify this method
     * appropriately so it can handle arbitrary transformations.
     */
    protected void handleMouseDragEvent(MouseEvent e) {
        
        Point2D oldPoint = lastPoint;
        Point2D newPoint = e.getPoint();
        
        double x_diff;
        double y_diff;
        
        switch (interactionMode) {
            case IDLE:
            	// no-op (shouldn't get here)
                break;
            case DRAGGING:
                x_diff = newPoint.getX() - oldPoint.getX();
                y_diff = newPoint.getY() - oldPoint.getY();
                transform.translate(x_diff, y_diff);
                break;
            // rotate the model.
            // scale only if the leg is selected.
            case ROTATING:
            	x_diff = newPoint.getX() - oldPoint.getX();
            	y_diff = newPoint.getY() - oldPoint.getY();
            	rotatePart(x_diff, y_diff);
            	if(this.bodyType == typeOfBody.LEG){
            		scalePart(x_diff,y_diff);
                }
            	break;
            case SCALING:
                break;
                
        }
        // Save our last point, if it's needed next time around
        lastPoint = e.getPoint();
    }
    // case ROTATING:
    protected void rotatePart(double x_diff, double y_diff){
    	AffineTransform trans = new AffineTransform();
    	double angle = 0;
    	boolean exceedRange;

    	switch (this.getBodyType()){
    		case HEAD:
    			angle = Math.PI/360 * x_diff;
    			break;
    		case BODY:
    		case UPPERARM:
    		case LOWERARM:
    		case HAND:
    		case LEG:
    			angle = -Math.PI/360 * x_diff;
    			break;
    		case FOOT:
    			angle = -Math.PI/360 * y_diff;
    			break;
    	}
    	// checks if the change would exceed angle limit
    	exceedRange = checkExceed(angle);
    	
    	// if not, then rotate along with its center
    	if(!exceedRange){
    		trans.rotate(angle, 
    				((EllipseSprite)this).getAnchorX(),
    				((EllipseSprite)this).getAnchorY()
    				);
    		transform.preConcatenate(trans);
    	}    	
}
    
    protected boolean checkExceed(double inputAngle){
    	int movementRange = 0;
    	double currentAngle = Math.toDegrees(this.angle + inputAngle);    	
    	
    	if(this.getBodyType() == typeOfBody.HEAD){
    		movementRange = 50;
    	}
    	else if(this.getBodyType() == typeOfBody.UPPERARM){
    		movementRange = 360;
    	}
    	else if(this.getBodyType() == typeOfBody.LOWERARM){
    		movementRange = 135;
    	}
    	else if(this.getBodyType() == typeOfBody.HAND){
    		movementRange = 35;
    	}
    	else if(this.getBodyType() == typeOfBody.LEG){
    		movementRange = 90;
    	}
    	else if(this.getBodyType() == typeOfBody.FOOT){
    		movementRange = 35;
    	}
    	
    	// angle exceeds the range
    	if (currentAngle >= movementRange || currentAngle <= -movementRange){
			return true;
		}
    	// angle is within the range
    	else{
    		this.angle += inputAngle;
    		return false;
    	}
    }
    
    // case SCALING
    protected void scalePart(double x_diff, double y_diff){
    	AffineTransform trans = new AffineTransform();
    	AffineTransform transFoot = new AffineTransform();
    	
    	int currentWidth = this.getWidth();
    	int currentHeight = this.getHeight();
    	
    	double newWidth = currentWidth + x_diff;
    	double newHeight = currentHeight + y_diff;
    	
    	trans.scale(1, newHeight/currentHeight);
    	
        transform.concatenate(trans);

        // resize the feet to original size
		if(this.children.size() == 1 && this.children.elementAt(0).children.size() == 1)
			this.children.elementAt(0).children.elementAt(0).transform.scale(1, 1/(newHeight/currentHeight));
		else if(this.children.size() == 1)
			this.children.elementAt(0).transform.scale(1, 1/(newHeight/currentHeight));
		
		// maintain opposite foot size when scaling UpperLegs
        if(this.parent.children.size() == 5){
        	if(this.equals(this.parent.children.elementAt(3))){
        		if(this.children.size() == 1 && this.children.elementAt(0).children.size() == 1)
        			this.parent.children.elementAt(4).children.elementAt(0).children.elementAt(0).transform.scale(1, 1/(newHeight/currentHeight));
        		else if(this.children.size() == 1)
        			this.parent.parent.children.elementAt(4).children.elementAt(0).children.elementAt(0).transform.scale(1, 1/(newHeight/currentHeight));
        	}
        	else{
        		if(this.children.size() == 1 && this.children.elementAt(0).children.size() == 1)
        			this.parent.children.elementAt(3).children.elementAt(0).children.elementAt(0).transform.scale(1, 1/(newHeight/currentHeight));
        		else if(this.children.size() == 1)
        			this.parent.parent.children.elementAt(3).children.elementAt(0).children.elementAt(0).transform.scale(1, 1/(newHeight/currentHeight));
        	}
        }
        
        // maintain opposite foot size when scaling LowerLegs
        else if(this.parent.children.size() == 1){
        	if(this.parent.equals(this.parent.parent.children.elementAt(3))){
        		if(this.children.size() == 1 && this.children.elementAt(0).children.size() == 1)
        			this.parent.children.elementAt(4).children.elementAt(0).children.elementAt(0).transform.scale(1, 1/(newHeight/currentHeight));
        		else if(this.children.size() == 1)
        			this.parent.parent.children.elementAt(4).children.elementAt(0).children.elementAt(0).transform.scale(1, 1/(newHeight/currentHeight));
        	}
        	else if(this.parent.equals(this.parent.parent.children.elementAt(4))){
        		if(this.children.size() == 1 && this.children.elementAt(0).children.size() == 1)
        			this.parent.children.elementAt(3).children.elementAt(0).children.elementAt(0).transform.scale(1, 1/(newHeight/currentHeight));
        		else if(this.children.size() == 1)
        			this.parent.parent.children.elementAt(3).children.elementAt(0).children.elementAt(0).transform.scale(1, 1/(newHeight/currentHeight));
        	}
        }
        
        
        
      // opposite legs
        else if(this.parent.children.size() == 1){
        	if(this.parent.equals(this.parent.parent.children.elementAt(3))){
        		this.parent.parent.children.elementAt(4).children.elementAt(0).transform.concatenate(trans);
        	}
        	else if(this.parent.equals(this.parent.parent.children.elementAt(4))){
        		this.parent.parent.children.elementAt(3).children.elementAt(0).transform.concatenate(trans);
        	}
        }        
        // scale all four legs
        if(this.parent.children.size() == 5){
        	// if leftUpperLeg selected,
        	if(this.equals(this.parent.children.elementAt(3))){
        		// also scale rightUpperLeg
        		this.parent.children.elementAt(4).transform.concatenate(trans);
        	}
        	// if right UpperLeg selected,
        	else{
        		// also scale leftUpperLeg
        		this.parent.children.elementAt(3).transform.concatenate(trans);
        	}
        }
        
        // sclae two lower legs
        else if(this.parent.children.size() == 1){
        	if(this.parent.equals(this.parent.parent.children.elementAt(3))){
        		this.parent.parent.children.elementAt(4).children.elementAt(0).transform.concatenate(trans);
        	}
        	else if(this.parent.equals(this.parent.parent.children.elementAt(4))){
        		this.parent.parent.children.elementAt(3).children.elementAt(0).transform.concatenate(trans);
        	}
        }
        
    }
    
    protected void handleMouseUp(MouseEvent e) {
        interactionMode = InteractionMode.IDLE;
    }
    
    /**
     * Locates the sprite that was hit by the given event.
     * You *may* need to modify this method, depending on
     * how you modify other parts of the class.
     * 
     * @return The sprite that was hit, or null if no sprite was hit
     */
    public Sprite getSpriteHit(MouseEvent e) {
        for (Sprite sprite : children) {
            Sprite s = sprite.getSpriteHit(e);
            if (s != null) {
                return s;
            }
        }
        if (this.pointInside(e.getPoint())) {
            return this;
        }
        return null;
    }
    
    /*
     * Important note: How transforms are handled here are only an example. You will
     * likely need to modify this code for it to work for your assignment.
     */
    
    /**
     * Returns the full transform to this object from the root
     */
    public AffineTransform getFullTransform() {
        AffineTransform returnTransform = new AffineTransform();
        
        Sprite curSprite = this;
        
        while (curSprite != null) {
            returnTransform.preConcatenate(curSprite.getLocalTransform());
            curSprite = curSprite.getParent();
        }
        return returnTransform;
    }

    /**
     * Returns our local transform
     */
    public AffineTransform getLocalTransform() {
        return (AffineTransform)transform.clone();
    }
    
    /**
     * Performs an arbitrary transform on this sprite
     */
    public void transform(AffineTransform t) {
    	transform.concatenate(t);
    }

    /**
     * Draws the sprite. This method will call drawSprite after
     * the transform has been set up for this sprite.
     */
    public void draw(Graphics2D g) {
        AffineTransform oldTransform = g.getTransform();

        // Set to our transform
        g.setTransform(this.getFullTransform());
        
        // Draw the sprite (delegated to sub-classes)
        this.drawSprite(g);
        
        // Restore original transform
        g.setTransform(oldTransform);

        // Draw children
        for (Sprite sprite : children) {
            sprite.draw(g);
        }
    }
    
    /**
     * The method that actually does the sprite drawing. This method
     * is called after the transform has been set up in the draw() method.
     * Sub-classes should override this method to perform the drawing.
     */
    protected abstract void drawSprite(Graphics2D g);
}

