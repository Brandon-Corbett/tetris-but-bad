package main;

//Java program to draw a line in Applet 

import java.awt.*; 
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


class Keychecker extends KeyAdapter {
	public static Boolean[][] savedMap = new Boolean[30][10];
	public static int savedX = 4;
	public static int savedY = 7;
	public static int savedCurrentPiece = 0;
    @Override
    public void keyPressed(KeyEvent event) {
        char ch = event.getKeyChar();
        
        if(ch == "a".charAt(0) || event.getKeyCode() == KeyEvent.VK_LEFT) {
        	if(Game.x > -1) {
        		if(!Game.isColliding(Game.x-1, Game.y)) {
        			Game.x--;
        		}
        	}
        }else if(ch == "d".charAt(0) || event.getKeyCode() == KeyEvent.VK_RIGHT) {
        	if(Game.x < 11) {
        		if(!Game.isColliding(Game.x+1, Game.y)) {
        			Game.x++;
        		}
        	}
        }else if(ch == "s".charAt(0) || event.getKeyCode() == KeyEvent.VK_DOWN) {
        	if(Game.y < 30) {
        		if(!Game.isColliding(Game.x, Game.y+1)) {
        			Game.y++;
        		}else{
        			Game.addTileToMap(Game.x, Game.y);
        		}
        	}
        	/*System.out.println("SAVING: ");
        	for(int i = 0; i < Game.map.length; i++) {
        		for(int ii = 0; ii < Game.map[i].length; ii++) {
            		savedMap[i][ii] = !!Game.map[i][ii];
            	}
        	}
        	System.out.println(Game.map[29][0].toString());
        	System.out.println(savedMap[29][0].toString());
        	savedX = Game.x;
        	savedY = Game.y;
        	savedCurrentPiece = Game.currentPiece;*/
        	
        }else if(ch == " ".charAt(0)) {
        	System.out.println("space pressed");
        	int tmp = Game.currentPiece;
        	System.out.println(tmp);
        	System.out.println(Game.rotationGuide[Game.currentPiece]);
        	
        	Game.currentPiece = Game.rotationGuide[Game.currentPiece];
        	Game.tile = Game.rotationMap[Game.currentPiece];
        	
        	if(Game.isColliding(Game.x, Game.y)) {
        		Game.tile = Game.rotationMap[tmp];
        		Game.currentPiece = tmp;
        	}
        	System.out.println(Game.currentPiece);
        }/*else if(ch == "q".charAt(0)) {
    		
    		System.out.println("LOADING: ");
        	System.out.println(savedMap[29][0].toString());
        	for(int i = 0; i < Game.map.length; i++) {
        		for(int ii = 0; ii < Game.map[i].length; ii++) {
        			Game.map[i][ii] = !!savedMap[i][ii];
            	}
        	}
        	System.out.println(Game.map[29][0].toString());
        	Game.x = savedX;
    		Game.y = savedY;
    		Game.currentPiece = savedCurrentPiece;
    		Game.tile = Game.rotationMap[savedCurrentPiece];
        }*/else if(ch == "r".charAt(0)) {
        	Game.x = 4;
    		Game.y = 7;
    		Game.clearMap();
        }
    }
}


@SuppressWarnings("serial")
class Game extends JPanel { 
    static int x = 4;
    static int y = 7;
    static Boolean[] lines = new Boolean[30];
    static int[] framesPerGridCell = {48, 43, 38, 33, 28, 23, 18, 13, 8, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1};// https://tetris.wiki/Tetris_(NES,_Nintendo)
    static int clearedLines = 0;
    static int numLines = 0;
    static int score = 0;
    static int level = 1;
    static int frames = 0;
    
    public static final int[] rotationGuide = {1, 0, 3, 4, 5, 2, 7, 8, 9, 6, 11, 12, 13, 10, 15, 14, 17, 16, 18};//given an index, when rotating, change index to... this
    public static final Boolean[][][] rotationMap = {
    		{
    			{false, false, false, false},//0 should go to 1 - line piece
    			{ true,  true,  true,  true},
    			{false, false, false, false},
    			{false, false, false, false}
    		},
    		{
    			{false, false,  true, false},//1 should go to 0 - line piece
    			{false, false,  true, false},
    			{false, false,  true, false},
    			{false, false,  true, false}
    		},
    		{
    			{false, false, false, false},//2 should go to 3 - T
    			{ true,  true,  true, false},
    			{false,  true, false, false},
    			{false, false, false, false}
    		},
    		{
    			{false,  true, false, false},//3 should go to 4 - T
    			{ true,  true, false, false},
    			{false,  true, false, false},
    			{false, false, false, false}
    		},
    		{
    			{false, false, false, false},//4 should go to 5 - T
    			{false,  true, false, false},
    			{ true,  true,  true, false},
    			{false, false, false, false}
    		},
    		{
    			{false,  true, false, false},//5 should go to 2 - T
    			{false,  true,  true, false},
    			{false,  true, false, false},
    			{false, false, false, false}
    		},
    		{
    			{false, false, false, false},//6 should go to 7 - L
    			{ true,  true,  true, false},
    			{ true, false, false, false},
    			{false, false, false, false}
    		},
    		{
    			{ true,  true, false, false},//7 should go to 8 - L
    			{false,  true, false, false},
    			{false,  true, false, false},
    			{false, false, false, false}
    		},
    		{
    			{false, false, false, false},//8 should go to 9 - L
    			{false, false,  true, false},
    			{ true,  true,  true, false},
    			{false, false, false, false}
    		},
    		{
    			{false,  true, false, false},//9 should go to 6 - L
    			{false,  true, false, false},
    			{false,  true,  true, false},
    			{false, false, false, false}
    		},
    		{
    			{false, false, false, false},//10 should go to 11 - reverse L
    			{ true,  true,  true, false},
    			{false, false,  true, false},
    			{false, false, false, false}
    		},
    		{
    			{false,  true, false, false},//11 should go to 12 - reverse L
    			{false,  true, false, false},
    			{ true,  true, false, false},
    			{false, false, false, false}
    		},
    		{
    			{false, false, false, false},//12 should go to 13 - reverse L
    			{ true, false, false, false},
    			{ true,  true,  true, false},
    			{false, false, false, false}
    		},
    		{
    			{false,  true,  true, false},//13 should go to 10 - reverse L
    			{false,  true, false, false},
    			{false,  true, false, false},
    			{false, false, false, false}
    		},
    		{
    			{false, false, false, false},//14 should go to 15 - S
    			{false,  true,  true, false},
    			{ true,  true, false, false},
    			{false, false, false, false}
    		},
    		{
    			{ true, false, false, false},//15 should go to 14 - S
    			{ true,  true, false, false},
    			{false,  true, false, false},
    			{false, false, false, false}
    		},
    		{
    			{false, false, false, false},//16 should go to 17 - Z
    			{ true,  true, false, false},
    			{false,  true,  true, false},
    			{false, false, false, false}
    		},
    		{
    			{false, false,  true, false},//17 should go to 16 - Z
    			{false,  true,  true, false},
    			{false,  true, false, false},
    			{false, false, false, false}
    		},
    		{
    			{false, false, false, false},//18 should go to 18.... as it is a square.
    			{false,  true,  true, false},
    			{false,  true,  true, false},
    			{false, false, false, false}
    		}
    };
    
    public static Boolean[][] clearMap() {
    	score = 0;
    	return map = new Boolean[][]{//I'm sorry.
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false},
	        {false, false, false, false, false, false, false, false, false, false}
	    };
    }
    
    //public Boolean[][] map = new Boolean[30][10];
    public static Boolean[][] map = clearMap();
    

    
    static int currentPiece = (int) Math.floor(Math.random()*rotationMap.length+1);
    public static Boolean[][] tile = rotationMap[currentPiece];

    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	drawBackground(g);
    	drawMap(g);
    	drawTile(g, x, y);
    	g.setFont(new Font("Arial", Font.PLAIN, 80));
    	//g.drawString("top\n"+"\nlevel\n"+Integer.toString(level), (super.getWidth()/2)+300, (super.getHeight()/2)-275);
    	g.drawString("top", (super.getWidth()/2)+300, (super.getHeight()/2)-275);
    	
    	g.drawString("score", (super.getWidth()/2)+300, (super.getHeight()/2)-125);
    	g.drawString(Integer.toString(score), (super.getWidth()/2)+300, (super.getHeight()/2)-50);
    }
    
    public void tick() {
    	frames++;
    	if(frames%framesPerGridCell[level] == 0) {
    		if(isColliding(x, y+1)) {
    			addTileToMap(x, y);
    		}else {
    			y++;
    		}
    	}

    	repaint();
    }
    
    public static Boolean checkForLine(int layer) {
    	System.out.println("checkForLine");
    	if(layer > 29) {
    		System.out.println("checkForLine: layer > 29");
    		return false;
    	}
    	Boolean isHole = false;
		for(int ii = 0; ii < map[layer].length && !isHole && ii >= 0; ii++){
			if(!map[layer][ii]) {
				isHole = true;
			}
		}
		if(!isHole) {
			System.out.println("checkForLine: no hole on layer " + Integer.toString(layer));
			numLines += 1;
			lines[layer] = true;
			return checkForLine(layer-1);
		}else {
			if(numLines > 0) {
				clearLines(layer+numLines);
				clearedLines += numLines;
				level = (int) Math.floor(clearedLines/10);
				frames = 1;
				if(numLines == 1) {//https://tetris.wiki/Scoring
					score += 40*(level + 1);
				}else if(numLines == 2) {
					score += 100*(level + 1);
				}else if(numLines == 3) {
					score += 300*(level + 1);
				}else {
					score += 1200*(level + 1);
				}
				numLines = 0;
				return true;
			}else {
				return false;
			}
		}
		
    }
    
    public static void clearLines(int layer) {
    	if(layer > 29) {
    		layer = 29;
    	}else if(layer < 0) {
    		return;
    	}
    	
    	System.out.println("clearLines");
    	System.out.println("clearLines: Num lines - " + Integer.toString(numLines));
    	Boolean hitLine = false;
    	

    	for(int i = layer; i - numLines > 0; i--) {
        	if(i > 29) {
        		i = 29;
        	}
    		System.out.println("clearLines: checking layer " + Integer.toString(i));
    		
    		if(lines[i] == null) {
    			lines[i] = false;
    		}
    		
    		if(lines[i] || hitLine) {
    			System.out.println("clearLines: line = true or already hit line; replacing line "+Integer.toString(i)+" with line "+Integer.toString(i-numLines));
    			hitLine = true;
    			
    			for(int ii = 0; ii < map[i].length; ii++) {
    				map[i][ii] = !!map[i-numLines][ii];
    			}
    			}else {
    			System.out.println("line != null, but line != true");
    		}
    	}
    }
    
    public static void addTileToMap(int x, int y) {
		for(int i = 0; i < tile.length; i++) {
			for(int ii = 0; ii < tile[i].length; ii++) {
				if(tile[i][ii]) {
					System.out.println("addTileToMap: adding block");
					map[y + i][x-1 + ii] = true;
				}
				if(y+i < 10) {
					System.out.println();
					clearMap();//FIX: GAME OVER
				} 
			}
		}
		int i = 0;
		System.out.println("addTileToMapLoop");
		while(i <= 4) {
			System.out.println(y+i);
			if(y+3-i > 29) {
				checkForLine(29);
			}else if(checkForLine(y+3-i)){
				break;
			}
			i++;
		}
		Game.y = 7;
		Game.x = 4;
		currentPiece = (int) Math.floor(Math.random()*rotationMap.length);
		tile = rotationMap[currentPiece];
    }
    
    public static Boolean isColliding(int x, int y) {
		Boolean colliding = false;
		try{
			for(int i = 0; i < tile.length && !colliding; i++) {
				for(int ii = 0; ii < tile[i].length && !colliding; ii++) {
					if(tile[i][ii] && map[y + i][x + ii-1]) {
						colliding = true;
					}
				}
			}
			return colliding;
    	}catch(Exception e){//if it errors out... it's probably out of bounds. tell whatever is calling it to fuck off
    		return true;
    	}
    }
    
    public void drawBackground(Graphics g) {
    	drawFrame(g, super.getWidth()/2, super.getHeight()/2, 400, 800, 5, new Color(40, 40, 40), new Color(240, 240, 240));
    	drawFrame(g, super.getWidth()/2 + 450, super.getHeight()/2 - 200, 400, 400, 5, new Color(40, 40, 40), new Color(240, 240, 240));
    }
    
    public void drawFrame(Graphics g, int x, int y, int width, int height, int border, Color color1, Color color2) {
    	//draws a frame based on a given center and size in each direction, with the outer just.. doing whatever it feels like
        g.setColor(color1);
        g.fillRect(x-border-width/2, y-border-height/2, width+border*2, height+border*2);
        g.setColor(color2);
        g.fillRect(x-width/2, y-height/2, width, height);
    }
    
    public void drawTile(Graphics g, int x, int y) {
    	g.setColor(new Color(100, 100, 255));
    	for(int i = 0; i < tile.length; i++) {
        	for(int ii = 0; ii < tile[i].length; ii++) {
        		if(tile[i][ii] == true) {
        			g.fillRect((super.getWidth()/2-240)+40*(x+ii), (super.getHeight()/2-800)+40*(y+i), 40, 40);
        		}
        	}
    	}
    }
    
    public void drawMap(Graphics g) {
    	g.setColor(new Color(255, 100, 100));
    	for(int i = 0; i < map.length; i++) {
        	for(int ii = 0; ii < map[i].length; ii++) {
        		if(map[i][ii] == true) {
        			g.fillRect((super.getWidth()/2-200)+40*ii, (super.getHeight()/2-800)+40*i, 40, 40);
        		}
        	}
    	}
    }
} 

public class className {

    
    static Graphics graphics;
    static Container myPanel;
    
    public static void main(String[] args) {
        // creating object of JFrame(Window popup)
        JFrame window = new JFrame(); 
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        window.setBounds(30, 30, 500, 500); 
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);  
        
        
        // setting canvas for draw 
        Game game = new Game();
        window.getContentPane().add(game);
        window.setUndecorated(true);
        window.addKeyListener(new Keychecker());
        
        // set visibility
        window.setVisible(true);
        
        //handle timing for gameloop
        long now = System.nanoTime();
        float lastTime = now;
        float delta = 0;
        
        while (true) {
        	now = System.nanoTime();
            delta += (now - lastTime)/60;
            lastTime = now;
       
            if(delta>1) {
            	game.tick();
                delta = 0;
            }
        }
    }
}

