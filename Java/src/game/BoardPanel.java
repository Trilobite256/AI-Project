/**
 * 
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

    private ArrayList<Space> spaceList;
    private ArrayList<DrawMarble> drawn;  

    private Marble m1;
    private Marble m2;
    private Marble m3;
    private int direction;
    private boolean marbleClicked;

    
    public BoardPanel(Game g){
        this.spaceList = new ArrayList<Space>();
        initSpaces();
        this.drawn = new ArrayList<DrawMarble>();
        Board b = g.getBoard();
        drawMarbles(b);
        marbleClicked = false;
        m1 = null;
        m2 = null;
        m3 = null;
        direction = 0;

        // listener for marbles
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);

                marbleClicked = false;

                for (DrawMarble d : drawn) {

                    if (d.contains(me.getPoint())) {//check if mouse is clicked within shape

                        if(m1 == null){
                            m1 = d.getMarble();
                            direction = 0;
                            marbleClicked = true;
                        } else if(m2 == null) {
                            m2 = d.getMarble();
                            direction = 0;
                            marbleClicked = true;
                        } else if(m3 == null){
                            m3 = d.getMarble();
                            marbleClicked = true;
                        } else {
                            marbleClicked = false;
                        }

/*
                        if(m1 != null){
                            System.out.println(m1.toString());
                        }
                        if(m2 != null){
                            System.out.println(m2.toString());
                        }*/
                    }
                }
            }
        });

        // listener for spaces
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);

                for(Space s : spaceList){
                    if(s.contains(me.getPoint())){
                        //System.out.println(s.toString());

                        if((s != null && m1 != null && !marbleClicked) || (s != null && m3 != null)){
                            int alphaDiff = m1.getAlpha() - s.getAlpha();
                            int numDiff = m1.getNumeric() - s.getNum();
                            direction = 0;

                            if(alphaDiff == 0){
                                if(numDiff < 0){
                                    direction = 3;
                                } else {
                                    direction = 6;
                                }
                            } else if(numDiff == 0){
                                if(alphaDiff > 0){
                                    direction = 4;
                                } else {
                                    direction = 1;
                                }
                            } else if(alphaDiff == numDiff){
                                if(alphaDiff > 0){
                                    direction = 5;
                                } else {
                                    direction = 2;
                                }
                            }

                            if(direction != 0){
                                if(m2 == null){
                                    m2 = TestDriver.game.checkAdjacent(m1, direction);
                                }

                                // single marble move
                                if((m2 == null && Gui.moveMarbles(TestDriver.game, m1.isBlack(), m1, direction))
                                        // double/triple marble move
                                        || (m2 != null) && Gui.moveMarbles(TestDriver.game, m1.isBlack(), m1, m2, direction)){

                                    drawMarbles(b);
                                    repaint();

                                    
                                }
                            }
                         // reset variables
                            m1 = null;
                            m2 = null;
                            m3 = null;
                            direction = 0;
                        }
                    }
                }
            }
        });    
    }


    public void initSpaces(){
        int y = 0;
        int x = 0;
        int blank = 80;
        int c = 0;
        for(int j = 5; j <= 9; j++) {
            x = blank;
            y += 60;
            for(int i = 1; i <= j; i ++) {
                c = (x + blank - 160)/60;
                x += 60;
                spaceList.add(new Space(((60 - y)/60 + 9), c + 5, x, y));
            }
            blank -= 30;
        }
        c = 0;
        blank = -10;
        for(int j = 8; j >= 5; j--) {
            x = blank;
            y += 60;
            for(int i = 0; i < j; i ++) {
                c = (x - blank - 1000)/60;
                x += 60;
                spaceList.add(new Space(((60 - y)/60 + 9), c + 17, x, y));

            }
            blank += 30;
        }
    }


    public void drawMarbles(Board b){
        this.drawn = new ArrayList<DrawMarble>();
        for(Marble m : b){
            for(Space s : spaceList){
                if(m.getAlpha() == s.getAlpha() && m.getNumeric() == s.getNum()){
                    DrawMarble d = new DrawMarble(m, s);
                    drawn.add(d);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        setBackground(new Color(218, 222, 229));
        Graphics2D g2d = (Graphics2D) grphcs;
        g2d.setPaint(Color.GRAY);
        for (Space s : spaceList) {
            g2d.fill(s);
        }
        for (DrawMarble d : drawn){

            if(d.getMarble().isBlack()){
                g2d.setPaint(Color.BLACK);
            } else {
                g2d.setPaint(Color.WHITE);

            }
            g2d.draw(d);
            g2d.fill(d);
        }
    }

    public ArrayList<Space> getSpaceList(){
        return this.spaceList;
    }

    public Space getSpace(Marble m){
        for(Space s : this.spaceList){
            if(m.getAlpha() == s.getAlpha() && m.getNumeric() == s.getNum()){
                return s;
            }
        }
        return null;
    }
}
