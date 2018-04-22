package ships;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


public class EndGameInfo{

	private JFrame frame;
	private JButton OK;
		
	public EndGameInfo(boolean wygrana){
				
		frame = new JFrame("Koniec gry!");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        if(wygrana)OK = new JButton("WYGRANA");
        else OK = new JButton("PRZEGRANA");
        
        
        OK.addActionListener(new ActionListener()
		{
		public void actionPerformed(ActionEvent ae)
		{
			frame.dispose();
		}
		});

        
        frame.add(OK);
        frame.setSize(300, 300);
        
        frame.setLocation(525, 225);
        
        frame.setVisible(true);
	}
		
}
