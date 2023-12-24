package Setup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import GameGUI.GameMechanics;

public class GameSetup extends JPanel implements ActionListener, ItemListener, WindowListener{
	
	private JPanel upper = new JPanel(new GridLayout(2, 2)); //checklist 8.
	private JPanel mouseSec = new JPanel(new GridLayout(1, 3));
	private JTextField scoreTf = new JTextField(10);
	private JRadioButton p1Rb = new JRadioButton("Player 1", true);
	private JRadioButton p2Rb = new JRadioButton("Player 2", false);
	private String[] level = {"Easiest", "Easy", "Hard", "Crazy"};
	private JComboBox<String> levelCb = new JComboBox<String>(level);
	private JButton doneBt = new JButton("Done");
	private JFrame f = new JFrame("Game Settings");
	private int difficulty = 1;
	private boolean p1Mouse = false;
	private int score = 0;
	
	public GameSetup() {
		
		setLayout(new GridLayout(4, 1));
		add(new JLabel("Select Settings"));
		
		upper.add(new JLabel("Enter Score Limit : "));
		upper.add(scoreTf);
		upper.add(new JLabel("Select Difficulty: "));
		upper.add(levelCb);
		add(upper);
		
		mouseSec.add(new JLabel("Mouse For "));
		mouseSec.add(p1Rb);
		mouseSec.add(p2Rb);
		add(mouseSec);
		
		add(doneBt);
		
		f.setLayout(new BorderLayout()); //checklist 8.
		f.add(this);
		f.setVisible(true);
		f.setSize(400, 300);
		
		levelCb.addItemListener(this);
		doneBt.addActionListener(this);
		p1Rb.addActionListener(this);
		p2Rb.addActionListener(this);
		f.addWindowListener(this);
	}
	public static void main (String[] args)
	{
		new GameSetup();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == doneBt) {
			//collect settings
			try {
				score = Integer.parseInt(scoreTf.getText());
				p1Mouse = p1Rb.isSelected();
				
				//run game here
				System.out.println(score + "  "+ p1Mouse + "  " +difficulty);
				f.dispose();
			}
			catch(NumberFormatException nfe) {
				//do nothing if you get invalid value
			}
			
		} else if (e.getSource() == p1Rb) {
			p2Rb.setSelected(false);
		} else if (e.getSource() == p2Rb) {
			p1Rb.setSelected(false);
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (levelCb.getSelectedItem() == "Crazy" && e.getStateChange() == 1) {
			difficulty = 4;
		} else if (levelCb.getSelectedItem() == "Hard" && e.getStateChange() == 1) {
			difficulty = 3;
		} else if (levelCb.getSelectedItem() == "Easy" && e.getStateChange() == 1) {
			difficulty = 2;
		} else if (levelCb.getSelectedItem() == "Easiest" && e.getStateChange() == 1) {
			difficulty = 1;
		}
		//System.out.println(difficulty);
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Window Closed");
		GameMechanics gm = new GameMechanics(score, difficulty, p1Mouse);
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
