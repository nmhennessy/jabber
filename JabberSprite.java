//JabberSprite

import java.awt.*;

public class JabberSprite extends TextSprite {

    public int state;
    public int prefix;
    public int suffix;
    public static final int FREE = 0;
    public static final int FINAL = 1;
    public static final int BEG = 2;
    public static final int END = 3;
    public static final int GARBAGE = 4;
    public static final int EXPLODE = 5;
    
    public static final int PREF_VOWEL = 6;
    public static final int PREF_CONSONANT = 7;
    public static final int SUFF_VOWEL = 8;
    public static final int SUFF_CONSONANT = 9;
    public static final int PREF_NULL = 10;
    public static final int SUFF_NULL = 11;
    
    java.awt.Color colour;
    private int countdown;

    JabberSprite(Component comp, String txt, Point pos, double xvel, double yvel, Font fnt) {
        super(comp, txt, pos, xvel, yvel, fnt);
        state = FREE;
        colour = java.awt.Color.black;
        countdown = 12;
        prefix = PREF_NULL;
        suffix = SUFF_NULL;
    } //JabberSprite::JabberSprite
    
    public void draw(Graphics g) {
        Color tempColour = g.getColor();
        g.setColor(colour);
        super.draw(g);
        g.setColor(tempColour);
    }//draw
    
    public void setColour(Color newColour) {
        colour = newColour;
    }//setColour
    
    void update() {
        if (state == GARBAGE) {
            if (countdown > 0) {
                countdown -= 1;
            } else {
                state = EXPLODE;
            }//if
        }//if
        super.update();
    }//update
    
    public void setPrefSuf() {
        //Set vowel consonant pattern
        int i = 0;
        String pattern = new String("");
        char temp;
        int text_length = text.length();
        for (i = 0; i < text_length; i += 1) {
            temp = text.charAt(i);
            if (temp == 'a' | temp == 'e' | temp == 'i' | temp == 'o' | temp == 'u' | (temp == 'y' & i != 0)) {
                pattern += "v";
            } else {
                pattern += "c";
            }
        }//for

        //PREFIX stuff
        if (pattern.substring(pattern.length()-4,pattern.length()).compareTo("vccv") == 0
            | pattern.substring(pattern.length()-4,pattern.length()).compareTo("vcvv") == 0
            | pattern.substring(pattern.length()-4,pattern.length()).compareTo("ccvv") == 0
            | pattern.substring(pattern.length()-4,pattern.length()).compareTo("cvcv") == 0
            | pattern.substring(pattern.length()-4,pattern.length()).compareTo("cccv") == 0
            ) {
            prefix = PREF_VOWEL;
        } else if (pattern.charAt(pattern.length()-1) == 'c') {
            prefix = PREF_CONSONANT;
        }//if

        //SUFFIX stuff
        if (pattern.compareTo("vccc") == 0
            | pattern.compareTo("vcvc") == 0
            | (pattern.compareTo("vccv") == 0 & text.charAt(text.length()-1) == 'e')
            | pattern.compareTo("vvcc") == 0
            | pattern.compareTo("vccvc") == 0
            | (pattern.compareTo("vcvcv") == 0 & text.charAt(text.length()-1) == 'e')
            | (pattern.compareTo("vcccv") == 0 & text.charAt(text.length()-1) == 'e')
            | pattern.compareTo("vcvcc") == 0
            | (pattern.charAt(0) == 'v' & text.charAt(text.length()-1) == 'e')
            ){
            suffix = SUFF_VOWEL;
        } else if ((pattern.charAt(0) == 'c' & pattern.charAt(pattern.length()-1) == 'c')
            | (pattern.charAt(0) == 'c' & text.charAt(text.length()-1) == 'y')
            | (pattern.compareTo("cvcv") == 0 & text.charAt(text.length()-1) == 'e')
            | (pattern.compareTo("ccvcv") == 0 & text.charAt(text.length()-1) == 'e')
            ){
            suffix = SUFF_CONSONANT;
        }//if
    } //setPrefSuf
    
}//JabberSprite