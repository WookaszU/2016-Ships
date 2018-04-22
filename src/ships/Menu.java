
package ships;


import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;


public class Menu
{
    private JFrame frame;
    
    private JPanel firstPanel;		//start high  exit
    private GamePanel secondPanel;	// game panel    
    private JPanel highPanel;		//highscores panel
        
    private static JTextField gameinfo;
    
    private JButton bStart;
    private JButton bHighscores;
    private JButton bExit;
    private JButton backButton;
    private JButton backHighButton;
    private JButton newGame;
    
    private JLabel shiplogo;
    private static JSlider opponentReactionDelay;
           
    
    public Menu(GamePanel mapadogry){	
    	this.secondPanel = mapadogry;
    }
    
    public void displayGUI()
    {
        frame = new JFrame("Statki");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 550);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        
        
        
        firstPanel = new JPanel();
        firstPanel.setOpaque(true);
        firstPanel.setBackground(Color.GRAY);
        firstPanel.setLayout(null);
        
        
        shiplogo = new JLabel( new ImageIcon(ShootBoard.class.getResource("pictures/statek.png")) );
        shiplogo.setBounds(530,5,150,200);
        firstPanel.add(shiplogo);
        
        
        
        JLabel reactinfo = new JLabel("Opoznienie przeciwnika:");
        reactinfo.setForeground(Color.WHITE);
        reactinfo.setBounds((frame.getWidth() - 138 )/2, 415, 138, 30);
        firstPanel.add(reactinfo);
        
        opponentReactionDelay = new JSlider();
        opponentReactionDelay.setBounds(550, 450, 100, 20);
        opponentReactionDelay.setMinimum(0);
        opponentReactionDelay.setMaximum(5);
        opponentReactionDelay.setValue(2);
        firstPanel.add(opponentReactionDelay);
        
        JLabel autor = new JLabel("Lukasz Uchman 2016 PO IEiT");
        autor.setForeground(Color.WHITE);
        autor.setBounds((frame.getWidth() - 165 )/2, 490, 165, 20);
        firstPanel.add(autor);

        	                                              
        secondPanel.setOpaque(true);
        secondPanel.setLayout(null);
        secondPanel.setBackground(Color.GRAY);
        

        bStart = new JButton("START");
        bStart.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	
            	           	
                frame.remove(firstPanel);
                              
                secondPanel = new GamePanel();
        		secondPanel.setOpaque(true);
        		secondPanel.setBackground(Color.GRAY);
        		secondPanel.add(backButton);
        		secondPanel.add(newGame);
        		secondPanel.add(gameinfo);
        		frame.add( secondPanel);
        		
                frame.revalidate();                
                frame.repaint();
                
                new Thread(new Runnable(){               	
	                public void run(){
	                	NewGame.gra(secondPanel);
	                }
                }).start();
                
                
            }
        });

        
        backButton = new JButton("BACK");
        backButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                frame.remove(secondPanel);
                
                frame.add(firstPanel);
                frame.revalidate();                
                frame.repaint();
            }
        });
        
        backHighButton = new JButton("BACK");
        
        backHighButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                frame.remove(highPanel);
                frame.add(firstPanel);
                frame.revalidate();               
                frame.repaint();
            }
        });
        
        
        
        
        bExit = new JButton("EXIT");
        bExit.addActionListener(new ActionListener()
        		{
        		public void actionPerformed(ActionEvent ae)
        		{
        			System.exit(0);
        		}
        		});
        
        bHighscores = new JButton("Highscores");
        bHighscores.addActionListener(new ActionListener()
			{
        	public void actionPerformed(ActionEvent ae)
        	{
        		
        		highPanel = new Highscore();
                highPanel.setOpaque(true);
                highPanel.setBackground(Color.GRAY);
                highPanel.add(backHighButton);
        		
        		
        		frame.remove(firstPanel);
                frame.add(highPanel);
                frame.revalidate();                
                frame.repaint();
			
        	}
			});
        
        //1200  550  frame
        bStart.setBounds( (frame.getWidth() - 200 )/2, 200, 200, 50);
        bHighscores.setBounds((frame.getWidth() - 200 )/2, 280, 200, 50);
        bExit.setBounds((frame.getWidth() - 200 )/2, 360, 200, 50);
        
        firstPanel.add(bStart);       
        firstPanel.add(bHighscores);
        firstPanel.add(bExit);
        
        backButton.setBounds(1045, 5, 100, 30);
        secondPanel.add(backButton);
      
        
        
       
        
        gameinfo = new JTextField("Ustaw swoje statki",10);
        gameinfo.setEditable(false);
        secondPanel.add(gameinfo);
        gameinfo.setBounds(300,460,500,40);
        
       
        
        
        
        newGame = new JButton("New Game");
        
        newGame.addActionListener(new ActionListener()
			{
        	public void actionPerformed(ActionEvent ae)
        	{	
        		
        		frame.remove(secondPanel);
        		 	
        		
        		secondPanel = new GamePanel();
        		secondPanel.setOpaque(true);
        		secondPanel.setBackground(Color.GRAY);
        		secondPanel.add(backButton);
        		secondPanel.add(newGame);
        		secondPanel.add(gameinfo);
        		
        		frame.add(secondPanel);       		    		
                frame.revalidate();               
                frame.repaint();
                
                             
                new Thread(new Runnable(){               	
	                public void run(){
	                	NewGame.gra(secondPanel);
	                }
                }).start();
                
        	}
			});
        
        
        
        newGame.setBounds(1045, 50, 100, 30);
        secondPanel.add(newGame);
        
       
        frame.add(firstPanel);           
        frame.setVisible(true);
    }
    
 
   //ustalanie komuniktatow o stanie gry   
 public static void setInfo(String text){
	 gameinfo.setText(text);
 }
 
 public GamePanel getGamePanel(){
	 return secondPanel;
 }
 
 public static int getNPCspeed(){
	 return 100 * opponentReactionDelay.getValue();
 }

}
 

