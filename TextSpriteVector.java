import java.awt.*;
import java.util.*;

public class TextSpriteVector extends Vector {
    Component component;
    Image background;
    
    TextSpriteVector() {
       super(50, 10);
    } //SpriteVector::SpriteVector

    TextSpriteVector(int initCapacity, int capacityInc, Component comp) {
        super(initCapacity, capacityInc);
        component = comp;
    } //TextSpriteVector::TextSpriteVector
    
    TextSpriteVector(Component comp, Image bg) {
        super(50,10);
        component = comp;
        background = bg;
    } //SpriteVector::SpriteVector
    
    TextSpriteVector(Component comp) {
        super(50, 10);
        component = comp;
    } //TextSpriteVector::TextSpriteVector
    
    Image getBackground() {
        return background;
    } //getBackground
    
    void setBackground(Image back) {
        background = back;
    } //setBackground
    
    void update() {
        TextSprite s;
        int size = size();
        for (int i = 0; i < size; i += 1) {
            s = (TextSprite)elementAt(i);
            s.update();
        } //for
    } //update

    void draw(Graphics g) {
        if (background != null) {
            //Draw background image
            g.drawImage(background, 0, 0, component);
        } else {
            Dimension dim = component.getSize();
            g.setColor(component.getBackground());
            g.fillRect(0, 0, dim.width, dim.height);
            g.setColor(Color.black);
        } //else
        for (int i = 0; i < size(); i += 1) {
            ((TextSprite)elementAt(i)).draw(g);
        } //for
    } //draw
    
    int add(TextSprite s) {
        insertElementAt(s, size());
        return size();
    } //add

} //TextSpriteVector