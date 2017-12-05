import java.awt.*;
import java.util.*;

public class JabberSpriteVector extends TextSpriteVector {
    String dummy_str;
    int state;
    int num_sprites;
    int magnitude;
    java.util.Random random;
    One_letter_frequency oneLetFreq;
    Two_letter_frequency twoLetFreq;
    Three_letter_frequency threeLetFreq;
    Three_letter_frequency_rev threeLetFreqRev;
    Three_letter_frequency_beg threeLetFreqBeg;
    Three_letter_frequency_end threeLetFreqEnd;
    private static final int MAGNITUDE = 10;
    Font font;
    
    JabberSpriteVector() {
        super();
        random = new Random();
        twoLetFreq = new Two_letter_frequency();
        threeLetFreq = new Three_letter_frequency();
        threeLetFreqRev = new Three_letter_frequency_rev();
        threeLetFreqBeg = new Three_letter_frequency_beg();
        threeLetFreqEnd = new Three_letter_frequency_end();
    }
    
    JabberSpriteVector(int initCapacity, int capacityInc, Component comp, One_letter_frequency one, Two_letter_frequency two, Three_letter_frequency three, Three_letter_frequency_rev three_rev, Three_letter_frequency_beg three_beg, Three_letter_frequency_end three_end, Font initFont ) {
        super(initCapacity, capacityInc, comp);
        random = new Random();
        oneLetFreq = one;
        twoLetFreq = two;
        threeLetFreq = three;
        threeLetFreqRev = three_rev;
        threeLetFreqBeg = three_beg;
        threeLetFreqEnd = three_end;
        font = initFont;
    }
    
    JabberSpriteVector(Component comp, Image bg) {
        super(comp, bg);
        twoLetFreq = new Two_letter_frequency();
        random = new Random();
        threeLetFreq = new Three_letter_frequency();
        threeLetFreqRev = new Three_letter_frequency_rev();
        threeLetFreqBeg = new Three_letter_frequency_beg();
        threeLetFreqEnd = new Three_letter_frequency_end();
    }
    
    JabberSpriteVector(Component comp, int mag) {
        super(comp);
        magnitude = mag;
        random = new Random();
        twoLetFreq = new Two_letter_frequency();
        threeLetFreq = new Three_letter_frequency();
        threeLetFreqRev = new Three_letter_frequency_rev();
        threeLetFreqBeg = new Three_letter_frequency_beg();
        threeLetFreqEnd = new Three_letter_frequency_end();
    }

    public void initializer() {
        clear();
        Enumeration letters = oneLetFreq.keys();
        double dub_tmp;
        int neg_1 = 1;
        int neg_2 = 1;
        while (letters.hasMoreElements()) {
            String letter = (String) letters.nextElement();
            for (int i = 0; i < (int) oneLetFreq.get(letter); i += 1) {
                dub_tmp = Math.floor(random.nextDouble() * MAGNITUDE);
                if (random.nextDouble() < 0.5) neg_1 *= -1;
                if (random.nextDouble() < 0.5) neg_2 *= -1;
                add(new JabberSprite(component, letter,
                        new Point((int)(random.nextDouble()*component.getSize().width), (int)(random.nextDouble()*(component.getSize().height - 40)+5)),
                        Math.sqrt(dub_tmp)*neg_1, Math.sqrt(MAGNITUDE - dub_tmp)*neg_2, font) );
            } // for
        } // while
    }//initializer
    
    void update() {
        JabberSprite s, hit;
        Point old;
        int i = 0;
        while (i < size()) {
            s = (JabberSprite)elementAt(i);
            if (s.state == JabberSprite.EXPLODE) {
                removeElement(s);
                int j = 0;
                int x_dir = 1;
                int y_dir = 1;
                int y_offset = 0;
                double dub_tmp = 0;
                for (j = 0; j < s.text.length()-1; j += 1) {
                    //decide quadrant to explode letter to, based on order:
                    //   \/
                    //     /\
                    if (j % 4 == 0) {
                        x_dir = -1;
                        y_dir = 1;
                        y_offset = 5;
                    } else if (j % 4 == 1) {
                        x_dir = 1;
                        y_dir = 1;
                        y_offset = 0;
                    } else if (j % 4 == 2) {
                        x_dir = -1;
                        y_dir = -1;
                        y_offset = -5;
                    } else if (j % 4 == 3) {
                        x_dir = 1;
                        y_dir = -1;
                        y_offset = 0;
                    } //if
                    dub_tmp = Math.floor(random.nextDouble() * MAGNITUDE);
                    this.add(new JabberSprite(component, s.text.substring(j, j+1),
                             new Point(s.position.x + j * 12, s.position.y + y_offset),
                             Math.sqrt(dub_tmp)*x_dir, Math.sqrt(MAGNITUDE-dub_tmp)*y_dir, s.font));
                }//for
            } else {
                old = new Point(s.position.x - 4, s.position.y - 4) ;
                s.update();
                hit = testCollision(s, i);
                if (hit != null) {
                    s.position = old;
                    collision(s, hit);
                } //if
            } //if
            i += 1;
        } //for
    } //update
      
  JabberSprite testCollision(JabberSprite test, int testIndex) {
    // Check for collision with other sprites
    int     size = size();
    JabberSprite  s;
    for (int i = testIndex + 1; i < size; i += 1)
    {
      s = (JabberSprite)elementAt(i);
      if (test.testCollision(s))
        return s;
    }
    return null;
  }

protected void collision(JabberSprite s, JabberSprite hit) {
    boolean bond = false; //flag to set if Sprites should combine together, ie “bond”
    //tests for two non-complete words
    if (s.state == JabberSprite.FREE & hit.state == JabberSprite.FREE) {
        //tests if both strings are single letters
        if (s.text.length() + hit.text.length() == 2) {
            //gets probability of two letter combination from two letter frequency hash      
            float chance = twoLetFreq.get(s.text + hit.text);
            // if random number is less than the chance, then bond
            if (random.nextFloat() <= chance) {
                bond = true;
            }//if

        //tests if s is a single letter, then hit is 2 or more
        } else if (s.text.length() == 1) {
            //get probability of s, given it’s followed by first two letters of hit
            float chance = threeLetFreqRev.get(s.text.substring(0,1) + hit.text.substring(hit.text.length()-2, hit.text.length()));
            if (random.nextFloat() <= chance) {
                bond = true;
            }//if

        //test if hit is a single letter, then s is 2 or more
        } else if (hit.text.length() == 1) {
            //get probability of hit, given it’s preceded by last two letters of s
            float chance = threeLetFreqRev.get(s.text.substring(s.text.length()-2, s.text.length()) + hit.text);
            if (random.nextFloat() <= chance) {
                  bond = true;
            }//if
 
       //two or more + two or more
        } else {
            //get probability of s, given it’s followed by first two letters of hit
            float chance1 = threeLetFreqRev.get(s.text.substring(s.text.length()-2, s.text.length()) + hit.text.substring(0,1));
            //get probability of hit, given it’s preceded by last two letters of s
            float chance2 = threeLetFreq.get(s.text.substring(s.text.length()-1, s.text.length()) + hit.text.substring(0,2));
            if (random.nextFloat() <= chance1 & random.nextFloat() <= chance2) {
                bond = true;
            } //if
        } //if
    
// if two strings are complete and less than 6 letters, check if they can bond to form a compound word in
// the same fashion as two strings of two or more letters
} else if (s.state == JabberSprite.FINAL & hit.state == JabberSprite.FINAL
                & s.text.length() < 6 & hit.text.length() < 6) {
        float chance1 = threeLetFreq.get(s.text.substring(s.text.length()-2, s.text.length()) + hit.text.substring(0,1));
        float chance2 = threeLetFreqRev.get(s.text.substring(s.text.length()-1, s.text.length()) + hit.text.substring(0,2));
        if (random.nextFloat() <= chance1 & random.nextFloat() <= chance2) {
            bond = true;
        } //if
    }//if

    if (bond == true) {
        //concatenate the s and hit text and remove hit Sprite
        s.text = s.text + hit.text;
        removeElement(hit);

        //if it’s greater than 4 letters, and has a valid prefix and suffix, it’s complete, change color to blue
        if (s.text.length() >=4 & s.state == JabberSprite.FREE) {
            if (threeLetFreqBeg.get(s.text.substring(0,3)) != 0
                & threeLetFreqEnd.get(s.text.substring(s.text.length()-3,s.text.length())) != 0) {
                s.state = JabberSprite.FINAL;
                s.setColour(java.awt.Color.blue);
            }//if

        //if the state is FINAL, it was two complete words that are now compound, change color to green
        } else if (s.state == JabberSprite.FINAL) {
            s.setColour(java.awt.Color.green);
        } //if
  
      // if the word is too long without a valid prefix and suffix, it’s a garbage word that is set to explode,
      // change color to red
      if (s.text.length() >= 8 & s.state == JabberSprite.FREE) {
            s.state = JabberSprite.GARBAGE;
            s.setColour(Color.red);
      }//if

    } else {
        // Swap velocities (bounce in opposite directions from whence they came)
        double swap_x = s.velocity_x;
        double swap_y = s.velocity_y;
        s.velocity_x = hit.velocity_x;
        s.velocity_y = hit.velocity_y;
        hit.velocity_x = swap_x;
        hit.velocity_y = swap_y;
    }//if
}//collision


} //JabberSpriteVector