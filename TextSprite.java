//TextSprite Class

import java.awt.*;

public class TextSprite {
    Component   component;
    Point       position;
    Rectangle   collision;
    double      velocity_x,
                velocity_y;
    Font        font;
    FontMetrics fontMetrics;
    boolean     floor_ceiling;
    public     String      text;
    
    TextSprite(Component comp, String txt, Point pos, double xvel, double yvel, Font fnt) {
        component = comp;
        text = txt;
        velocity_x = xvel;
        velocity_y = yvel;
        position = pos;
        setFont(fnt);
        floor_ceiling = false;
    } //TextSprite::TextSprite
    
    public void setFont(Font fnt) {
        font = fnt;
        component.setFont(font);
        fontMetrics = component.getFontMetrics(font);
        calcCollisionRect();
    } //setFont

    public void setPosition(Point pos) {
        position = pos;
    } //setPosition
    
    void update() {
        int text_width = fontMetrics.stringWidth(text),
            text_height = fontMetrics.getHeight(),
            w = component.getSize().width,
            h = component.getSize().height;
        if (floor_ceiling) {
            position.x += Math.round(velocity_x);
            position.y += Math.round(velocity_y);
            floor_ceiling = false;
        } else {
            position.x += Math.ceil(velocity_x);
            position.y += Math.ceil(velocity_y);
            floor_ceiling = true;
        }
        if (position.x < 0) {
            position.x = 0;
            velocity_x *= -1;
        } else if (position.x + text_width > w) {
            position.x = w - text_width;
            velocity_x *= -1;
        }//if
        if (position.y - fontMetrics.getAscent() < 0) {
            position.y = fontMetrics.getAscent();
            velocity_y *= -1;
        } else if (position.y + text_height > h) {
            position.y = h - text_height;
            velocity_y *= -1;
        }
        calcCollisionRect();
    } //update
    
    protected void calcCollisionRect() {
        collision = new Rectangle(position.x, position.y - fontMetrics.getHeight(), fontMetrics.stringWidth(text), fontMetrics.getDescent());
    } //calcCollisionRect

    public Rectangle getCollisionRect() {
        return collision;
    } //getCollisionRect

    public void draw(Graphics g) {
        //draw the text
        g.drawString(text, position.x, position.y);
    } //draw

    protected boolean testCollision(TextSprite test) {
      // Check for collision with another sprite
      if (this != test)
        if (collision.intersects(test.getCollisionRect()))
          return true;
      return false;
    }


} //TextSprite