 import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import java.awt.Label;
import javax.swing.JTextField;
import java.awt.Choice;
import java.io.*;
import java.net.*;
import java.io.IOException;

public class Main_Board {

	public JFrame Game_Board_Frame;
	private JTextField coins;
	private int hole = 6;
	private int coin = 4;

	private boolean p1_turn = true;
	private int p1_count = 0;
	private int p2_count = 0;
	
	Socket s;
	//System.out.println("Server Connected!");
	PrintWriter out;
	BufferedReader in;

	/**
	 * Create the application.
	 */
	public Main_Board() {
		Game_Board_Frame = new JFrame();
		Game_Board_Frame.setResizable(false);
		Game_Board_Frame.setTitle("Kahla Online");
		Game_Board_Frame.setBounds(0, 0, 1366, 768);
		Game_Board_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Game_Board_Frame.getContentPane().setLayout(new CardLayout(0, 0));
		Game_Board_Frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		welcome_screen();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void welcome_screen() {
		//Welcome Screen Panel
		
		JPanel Welcome_Screen = new JPanel();
		Game_Board_Frame.getContentPane().add(Welcome_Screen, "name_176445363097357");
		
		Label coins_text = new Label("Coins Per Hole");
		coins_text.setAlignment(Label.CENTER);
		coins_text.setBounds(500, 310, 90, 20);
		Welcome_Screen.add(coins_text);
		
		Label holes_text = new Label("Holes");
		holes_text.setAlignment(Label.CENTER);
		holes_text.setBounds(500, 335, 90, 20);
		Welcome_Screen.add(holes_text);
		
		Label config_text = new Label("Distribution");
		config_text.setAlignment(Label.CENTER);
		config_text.setBounds(500, 360, 90, 20);
		Welcome_Screen.add(config_text);
		
		String instruct = "Game Instructions <br>" + 
				"<br>" + 
				"Welcome to Kalah! Please enter the number of holes (4 to 9) on each side and a random number of coins (1 to 10) will be initialized in each hole. The total number of coins will stay the same throughout the game. Both players will have the same arrangements of coins to start with. You may click on any of the holes on your side to move the coins counterclockwise, until no moves can be made, the player with the most number of coins in their “house” wins. \r\n" + 
				"The game will start following the “Pie rule”: after the player 1 makes a move, player 2 has a choice to let player 1’s move stand and continue the game or choose to switch positions with player 1. There is also a time limit option should you wish to set a timer for people to make a move. The Aggies lost the season (again) this year, but may the odds be ever in your favour!\r\n" + 
				"";
		JLabel Instruction_LB = new JLabel("<html>" + instruct + "<html>");
		Instruction_LB.setForeground(Color.BLACK);
		Instruction_LB.setHorizontalAlignment(SwingConstants.CENTER);
		Instruction_LB.setBounds(108, 168, 278, 335);
		//Instruction_LB.setOpaque(true);
		Welcome_Screen.add(Instruction_LB);
		
		JLabel inst_bg = new JLabel("");
		inst_bg.setBackground(new Color(250, 250, 210));
		inst_bg.setBounds(87, 166, 312, 355);
		inst_bg.setOpaque(true);
		Welcome_Screen.add(inst_bg);
		
		coins = new JTextField();
		coins.setColumns(10);
		coins.setBounds(595, 310, 106, 20);
		Welcome_Screen.add(coins);
		
		Choice choice = new Choice();
		choice.setBounds(595, 360, 106, 20);
		choice.add("Fixed");
		choice.add("Random");
		Welcome_Screen.add(choice);
		
		Choice choice_holes = new Choice();
		choice_holes.setBounds(595, 335, 106, 20);
		choice_holes.add("4");
		choice_holes.add("5");
		choice_holes.add("6");
		choice_holes.add("7");
		choice_holes.add("8");
		choice_holes.add("9");
		Welcome_Screen.add(choice_holes);
		
		//MULTIPLAYER PLAYER 1
		JButton Join_p1 = new JButton("");
		Join_p1.setIcon(new ImageIcon(Main_Board.class.getResource("/graphics/connect1.png")));
		Join_p1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (Integer.parseInt(coins.getText()) > 0 ){
						coin = Integer.parseInt(coins.getText());
						hole = Integer.parseInt(choice_holes.getSelectedItem());
					} else {
						coin = 4;
						hole = 6;
						JOptionPane.showMessageDialog(null, "Invalid option, the board has been set to default 6 holes, 4 beans");
					}
				} catch (NumberFormatException nfe) {     
				     coin = 4;
					 hole = 6;
				     JOptionPane.showMessageDialog(null,"Invalid option, the board has been set to default 6 holes, 4 beans");
				}
				
				Welcome_Screen.setVisible(false);
				char what = 'R';
				if (choice.getSelectedItem().equals("Fixed"))
					what = 'S';
				draw(3, what);
			}
		});
		
		Join_p1.setBounds(922, 205, 125, 85);
		Join_p1.setContentAreaFilled(false);
		Join_p1.setBorderPainted(false);
		Join_p1.setBorder(null);
		Welcome_Screen.add(Join_p1);
		
		//MULTIPLAYER PLAYER 2
		JButton Join_p2 = new JButton("");
		Join_p2.setIcon(new ImageIcon(Main_Board.class.getResource("/graphics/connect2.png")));
		Join_p2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (Integer.parseInt(coins.getText()) > 0 ){
						coin = Integer.parseInt(coins.getText());
						hole = Integer.parseInt(choice_holes.getSelectedItem());
					} else {
						coin = 4;
						hole = 6;
						JOptionPane.showMessageDialog(null, "Invalid option, the board has been set to default 6 holes, 4 beans");
					}
				} catch (NumberFormatException nfe) {     
				     coin = 4;
					 hole = 6;
				     JOptionPane.showMessageDialog(null,"Invalid option, the board has been set to default 6 holes, 4 beans");
				}
				
				Welcome_Screen.setVisible(false);
				char what = 'R';
				if (choice.getSelectedItem().equals("Fixed"))
					what = 'S';
				draw(4, what);
			}
		});
		Join_p2.setBounds(1057, 205, 137, 85);
		Join_p2.setContentAreaFilled(false);
		Join_p2.setBorderPainted(false);
		Join_p2.setBorder(null);
		Welcome_Screen.add(Join_p2);

		
		//Players Options =================================================================================
		//Player v. Player
		JButton p1v2 = new JButton("");
		p1v2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		p1v2.setBounds(762, 205, 150, 96);
		p1v2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (Integer.parseInt(coins.getText()) > 0 ){
						coin = Integer.parseInt(coins.getText());
						hole = Integer.parseInt(choice_holes.getSelectedItem());
					} else {
						coin = 4;
						hole = 6;
						JOptionPane.showMessageDialog(null, "Invalid option, the board has been set to default 6 holes, 4 beans");
					}
				} catch (NumberFormatException nfe) {     
				     coin = 4;
					 hole = 6;
				     JOptionPane.showMessageDialog(null,"Invalid option, the board has been set to default 6 holes, 4 beans");
				}
				Welcome_Screen.setVisible(false);
				char what = 'R';
				if (choice.getSelectedItem().equals("Fixed"))
					what = 'S';
				draw(1, what);
			}
		});
		p1v2.setContentAreaFilled(false);
		p1v2.setBorderPainted(false);
		p1v2.setBorder(null);
		p1v2.setIcon(new ImageIcon(Main_Board.class.getResource("/graphics/p1vp2.png")));
		Welcome_Screen.setLayout(null);
		Welcome_Screen.add(p1v2);
		
		//Player v. AI
		JButton pVai = new JButton("");
		pVai.setBounds(762, 295, 150, 96);
		pVai.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (Integer.parseInt(coins.getText()) > 0 ){
						coin = Integer.parseInt(coins.getText());
						hole = Integer.parseInt(choice_holes.getSelectedItem());
					} else {
						coin = 4;
						hole = 6;
						JOptionPane.showMessageDialog(null, "Invalid option, the board has been set to default 6 holes, 4 beans");
					}
				} catch (NumberFormatException nfe) {     
				     coin = 4;
					 hole = 6;
				     JOptionPane.showMessageDialog(null,"Invalid option, the board has been set to default 6 holes, 4 beans");
				}				
				
				Welcome_Screen.setVisible(false);
				char what = 'R';
				if (choice.getSelectedItem().equals("Fixed"))
					what = 'S';
				draw(2, what);
			}
		});
		pVai.setContentAreaFilled(false);
		pVai.setBorderPainted(false);
		pVai.setBorder(null);
		pVai.setIcon(new ImageIcon(Main_Board.class.getResource("/graphics/pvai.png")));
		Welcome_Screen.setLayout(null);
		Welcome_Screen.add(pVai);
		
		//AI v. AI
		JButton aiVai = new JButton("");
		aiVai.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		aiVai.setBounds(762, 385, 150, 96);
		aiVai.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (Integer.parseInt(coins.getText()) > 0 ){
						coin = Integer.parseInt(coins.getText());
						hole = Integer.parseInt(choice_holes.getSelectedItem());
					} else {
						coin = 4;
						hole = 6;
						JOptionPane.showMessageDialog(null, "Invalid option, the board has been set to default 6 holes, 4 beans");
					}
				} catch (NumberFormatException nfe) {     
				     coin = 4;
					 hole = 6;
				     JOptionPane.showMessageDialog(null,"Invalid option, the board has been set to default 6 holes, 4 beans");
				}				
				
				Welcome_Screen.setVisible(false);
				char what = 'R';
				if (choice.getSelectedItem().equals("Fixed"))
					what = 'S';
				draw(5, what);

			}
		});
		aiVai.setContentAreaFilled(false);
		aiVai.setBorderPainted(false);
		aiVai.setIcon(new ImageIcon(Main_Board.class.getResource("/graphics/aivai.png")));
		Welcome_Screen.setLayout(null);
		Welcome_Screen.add(aiVai);

		//Welcome Screen Background =========================================================================
		JLabel Wallpaper = new JLabel("");
		Wallpaper.setIcon(new ImageIcon(Main_Board.class.getResource("/graphics/welcome.jpg")));
		Wallpaper.setBounds(0, 0, 1360, 739);
		Welcome_Screen.add(Wallpaper);
		


	}
	public void draw(int options, char choice) {
		if (options == 3 || options == 4) {
			try {
				s = new Socket("localhost", 7896);
				out =  new PrintWriter(s.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch (IOException ex){
				System.out.println("Failed to connect to the server");
			}
		}
		
		//IMPLEMENTATION OF THE BOARD ==================================================================
		Algorithm board = new Algorithm(hole, coin, choice);
		
		//Layer Pane====================================================================================
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setOpaque(true);
		Game_Board_Frame.getContentPane().add(layeredPane, "name_1557251115221870");
		
		//This PANEL HOUSE OTHER INFO
		JPanel panel_12 = new JPanel();
		panel_12.setOpaque(false);
		panel_12.setBounds(0, 0, 1360, 739);
		layeredPane.add(panel_12, 2, 0);
		panel_12.setLayout(null);
		
		
		//THIS PANEL HOUSE ALL LABELS
		JPanel panel_1 = new JPanel();
		panel_1.setOpaque(false);
		panel_1.setBounds(0, 0, 1360, 739);
		layeredPane.add(panel_1, 2, 0);
		panel_1.setLayout(null);
		
		//THIS PANEL HOUSE ALL BUTTONS
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBounds(0, 0, 1360, 739);
		layeredPane.add(panel, 1, 0);
		panel.setLayout(null);
		
		//THIS PANEL HOUSE THE BACKGROUND
		JPanel Board_Screen = new JPanel();
		Board_Screen.setBounds(0, 0, 1360, 739);
		layeredPane.add(Board_Screen);
		Board_Screen.setLayout(null);

		//INITIAL DRAWING
		draw_update(panel_1, board);
		
		//DRAWING BACKBONE BUTTON FOR HOUSE 1 and 2
		JButton ne = new JButton("");
		ne.setBounds(260, 275, 150, 150);
		ne.setBackground(SystemColor.desktop);
		panel.add(ne);
		
		JButton he = new JButton("");
		he.setBounds(1010, 275, 150, 150);
		he.setBackground(SystemColor.desktop);
		panel.add(he);
		
		//POSITIONS
		int start = 260 + ((9 - hole)*50);
		int stop = 1160 - ((9 - hole)*50);
		int count2 = board.get_size()-2;
		int count = 0;
		
		//THIS IS WHERE PLAYER COMES IN
		
		switch(options) {
		//SET UP BUTTONS FOR PLAYER VS PLAYER
		case 1:{
			for (int i = start; i < stop; i = i + 100) {
				JButton newButton = new JButton("");
				newButton.setBounds(i, 450, 100, 100);
				newButton.setBackground(SystemColor.desktop);
				final int passed_in = count;
				newButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent arg0) {
						if(p1_turn == true && board.move(passed_in)) {
							p1_count++;
							draw_update(panel_1, board);
							if(!board.end_game()) {
								if(!board.extra_left()) {
									p1_turn = false;
								}
								else {
									board.spend_move();
								}
							}
							else {

								draw_update(panel_1, board);
								display_winner(board.ref());
								
								Game_Board_Frame.dispose();
							}
						}
					}
				});
				panel.add(newButton);
				
				JButton newButton2 = new JButton("");
				newButton2.setBounds(i, 150, 100, 100);
				newButton2.setBackground(SystemColor.desktop);
				final int passed_in2 = count2;
				newButton2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if(p1_turn == false && board.move(passed_in2)) {
							p2_count++;
							draw_update(panel_1, board);
							if(!board.end_game()) {
								if(!board.extra_left()) {
									p1_turn = true;
								}
								else {
									board.spend_move();
								}
							}
							else {
								draw_update(panel_1, board);
								display_winner(board.ref());
								Game_Board_Frame.dispose();
							}
						}
					}
				});
				panel.add(newButton2);
				count++;
				count2--;
			}
			break;
		}
		//SET UP BUTTONS FOR PLAYER VS AI
		case 2:{
			for (int i = start; i < stop; i = i + 100) {
				JButton newButton = new JButton("");
				newButton.setBounds(i, 450, 100, 100);
				newButton.setBackground(SystemColor.desktop);
				final int passed_in = count;
				newButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent arg0) {
						//Human make decision
						if(board.move(passed_in)) {
							p1_count++;
							draw_update(panel_1, board);
							
							if(!board.extra_left() && !board.end_game()) {
								do {
									board.spend_move();
									int AI_move = board.AI_mm(2);
									board.move(AI_move);
									p2_count++;
									draw_update(panel_1, board);
									
									String label = "AI moved this";
									JLabel lblNewLabel = new JLabel(label,  SwingConstants.CENTER);
									lblNewLabel.setBounds(start+100*(board.get_size()-AI_move-2), 110, 100, 100);
									lblNewLabel.setForeground(SystemColor.text);
									panel_1.add(lblNewLabel);
								}while(!board.end_game() && board.extra_left());
							}
							else {
								board.spend_move();
							}

							if(board.end_game()) {
								draw_update(panel_1, board);
								display_winner(board.ref());
								Game_Board_Frame.dispose();
							}
						}
						
					}
				});

				panel.add(newButton);
				JButton newButton2 = new JButton("");
				newButton2.setBounds(i, 150, 100, 100);
				newButton2.setBackground(SystemColor.desktop);
				panel.add(newButton2);
				count++;
			}
			break;
		}
		
		//SET UP BUTTONS FOR MULTIPLAYER for Player 1
		case 3:{
			for (int i = start; i < stop; i = i + 100) {
				JButton newButton = new JButton("");
				newButton.setBounds(i, 450, 100, 100);
				newButton.setBackground(SystemColor.desktop);
				final int passed_in = count;

					
				newButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent arg0) {
						board.move(passed_in);
						p1_count++;
						draw_update(panel_1, board);
						out.println(passed_in);
						try {
							String s = in.readLine();
							if(!s.isEmpty()) {
								board.move(Integer.parseInt(s));
								draw_update(panel_1, board);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				});

				panel.add(newButton);
				
				JButton newButton2 = new JButton("");
				newButton2.setBounds(i, 150, 100, 100);
				newButton2.setBackground(SystemColor.desktop);
				panel.add(newButton2);
				count++;
			}
			break;
		}
		
		//SET UP BUTTONS FOR MULTIPLAYER for Player 1
		case 4:{
			for (int i = start; i < stop; i = i + 100) {
				JButton newButton = new JButton("");
				newButton.setBounds(i, 450, 100, 100);
				newButton.setBackground(SystemColor.desktop);
				final int passed_in = count2;
	
				panel.add(newButton);
				
				JButton newButton2 = new JButton("");
				newButton2.setBounds(i, 150, 100, 100);
				newButton2.setBackground(SystemColor.desktop);
				newButton2.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent arg0) {
						try {
							String s = in.readLine();
							board.move(Integer.parseInt(s));
							draw_update(panel_1, board);
						} catch (IOException e) {
							e.printStackTrace();
						}
						board.move(passed_in);
						p2_count++;
						draw_update(panel_1, board);
						out.println(passed_in);
						
					}
				});
				
				panel.add(newButton2);
				count2--;
			}
			break;
		}
		
		//AI VS AI
		case 5:{
			for (int i = start; i < stop; i = i + 100) {
				JButton newButton = new JButton("");
				newButton.setBounds(i, 450, 100, 100);
				newButton.setBackground(SystemColor.desktop);
	
				panel.add(newButton);
				
				JButton newButton2 = new JButton("");
				newButton2.setBounds(i, 150, 100, 100);
				newButton2.setBackground(SystemColor.desktop);
				
				panel.add(newButton2);
			}
			
			Board_Screen.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent arg0) {
					if (p1_turn == true) {
						board.spend_move();
						int AI1_move = board.AI_mm(1);
						board.move(AI1_move);
						p1_count++;
						draw_update(panel_1, board);
						
						String label = "AI moved this";
						JLabel lblNewLabel = new JLabel(label,  SwingConstants.CENTER);
						lblNewLabel.setBounds(start+100*(AI1_move), 490, 100, 100);
						lblNewLabel.setForeground(SystemColor.text);
						panel_1.add(lblNewLabel);
						
						if(board.end_game()) {
							draw_update(panel_1, board);
							display_winner(board.ref());
							Game_Board_Frame.dispose();
						}
						if(!board.extra_left()) {
							p1_turn = false;
						}
					}
					else {
						board.spend_move();
						int AI2_move = board.AI_mm(2);
						board.move(AI2_move);
						p2_count++;
						draw_update(panel_1, board);

						String label = "AI moved this";
						JLabel lblNewLabel = new JLabel(label,  SwingConstants.CENTER);
						lblNewLabel.setBounds(start+100*(board.get_size()-AI2_move-2), 110, 100, 100);
						lblNewLabel.setForeground(SystemColor.text);
						panel_1.add(lblNewLabel);
						
						if(board.end_game()) {
							draw_update(panel_1, board);
							display_winner(board.ref());
							Game_Board_Frame.dispose();
						}
						if(!board.extra_left()) {
							p1_turn = true;
						}
					}
				}
			});
			break;
		}
		
		default:{
			break;
		}
		}	
		//MENU OPTIONS ====================================================================================
		JButton Save_Button = new JButton("Save");
		Save_Button.setForeground(SystemColor.text);
		Save_Button.setBackground(SystemColor.desktop);
		Save_Button.setBounds(53, 578, 89, 23);
		Board_Screen.add(Save_Button);
		Save_Button.setVisible(false);
		
		JButton Load_Button = new JButton("Load");
		Load_Button.setBackground(SystemColor.desktop);
		Load_Button.setForeground(SystemColor.text);
		Load_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		Load_Button.setBounds(53, 612, 89, 23);
		Board_Screen.add(Load_Button);
		Load_Button.setVisible(false);
		
		JButton Setting_Button = new JButton("Setting");
		Setting_Button.setForeground(SystemColor.text);
		Setting_Button.setBackground(SystemColor.desktop);
		Setting_Button.setBounds(53, 544, 89, 23);
		Board_Screen.add(Setting_Button);
		Setting_Button.setVisible(false);
		
		JButton Exit_Button = new JButton("Exit");
		Exit_Button.setBackground(SystemColor.desktop);
		Exit_Button.setForeground(SystemColor.text);
		Exit_Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Game_Board_Frame.dispose();
			}
		});
		Exit_Button.setBounds(53, 646, 89, 23);
		Board_Screen.add(Exit_Button);
		Exit_Button.setVisible(false);
		
		JLabel Menu_Background = new JLabel("");
		Menu_Background.setBackground(Color.ORANGE);
		Menu_Background.setOpaque(true);
		Menu_Background.setBounds(33, 527, 130, 159);
		Board_Screen.add(Menu_Background);
		Menu_Background.setVisible(false);
		
		JButton Menu_Button = new JButton("");
		Menu_Button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (Menu_Background.isVisible() == false) {
					Menu_Background.setVisible(true);
					Save_Button.setVisible(true);
					Load_Button.setVisible(true);
					Setting_Button.setVisible(true);
					Exit_Button.setVisible(true);
				} else {
					Menu_Background.setVisible(false);
					Save_Button.setVisible(false);
					Load_Button.setVisible(false);
					Setting_Button.setVisible(false);
					Exit_Button.setVisible(false);
				}
			}
		});
		Menu_Button.setBounds(10, 680, 205, 59);
		Menu_Button.setOpaque(false);
		Menu_Button.setContentAreaFilled(false);
		Menu_Button.setBorderPainted(false);
		Board_Screen.add(Menu_Button);
										
					
		//Main Board Layout ===============================================================================
		JLabel Board_Layout = new JLabel("");
		Board_Layout.setIcon(new ImageIcon(Main_Board.class.getResource("/graphics/rzGame_Board.jpg")));
		Board_Layout.setBounds(0, 0, 1366, 739);
		Board_Screen.add(Board_Layout);

		Game_Board_Frame.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{Game_Board_Frame.getContentPane()}));
		layeredPane.setVisible(true);
	}
	
	public void draw_update(JPanel panel_1, Algorithm board) {
		panel_1.removeAll();
		int start = 260 + ((9 - hole)*50);
		int stop = 1160 - ((9 - hole)*50);
		int count2 = board.get_size()-2;
		int count = 0;
		//DRAWING THE CELLS
		for (int i = start; i < stop; i = i + 100) {
			String label = Integer.toString(board.get_cell(count));
			JLabel lblNewLabel = new JLabel(label,  SwingConstants.CENTER);
			lblNewLabel.setBounds(i, 450, 100, 100);
			lblNewLabel.setForeground(SystemColor.text);
			panel_1.add(lblNewLabel);
			
			String label2 = Integer.toString(board.get_cell(count2));
			JLabel lblNewLabel2 = new JLabel(label2,  SwingConstants.CENTER);
			lblNewLabel2.setBounds(i, 150, 100, 100);
			lblNewLabel2.setForeground(SystemColor.text);
			panel_1.add(lblNewLabel2);
			
			count2--;
			count++;
		}
		
		//DRAWING THE HOUSES
		String house_2 = Integer.toString(board.get_cell(board.get_size() - 1));
		JLabel lbl2 = new JLabel(house_2,  SwingConstants.CENTER);
		lbl2.setBounds(260, 275, 150, 150);
		lbl2.setForeground(SystemColor.text);
		panel_1.add(lbl2);
		String house_1 = Integer.toString(board.get_cell(hole));
		JLabel lbl1 = new JLabel(house_1,  SwingConstants.CENTER);
		lbl1.setBounds(1010, 275, 150, 150);
		lbl1.setForeground(SystemColor.text);
		panel_1.add(lbl1);
		
		//Drawing the Score
		String score_1 = "SCORE: " + Integer.toString(board.get_score(1));
		String score_2 = "SCORE: " + Integer.toString(board.get_score(2));
		JLabel s1 = new JLabel(score_1, SwingConstants.CENTER);
		s1.setFont(new Font("Serif", Font.BOLD, 20));
		JLabel s2 = new JLabel(score_2, SwingConstants.CENTER);
		s2.setFont(new Font("Serif", Font.BOLD, 20));
		
		JLabel pp1 = new JLabel("PLAYER 1", SwingConstants.CENTER);
		pp1.setFont(new Font("Serif", Font.BOLD, 20));
		JLabel pp2 = new JLabel("PLAYER 2", SwingConstants.CENTER);
		pp2.setFont(new Font("Serif", Font.BOLD, 20));
		pp1.setBounds(1175, 690, 125, 50);
		pp2.setBounds(75, 65, 125, 50);
		
		s2.setBounds(75, 25, 125, 50);
		s1.setBounds(1175, 650, 125, 50);
		
		pp1.setForeground(Color.BLACK);
		pp2.setForeground(Color.BLACK);
		s1.setForeground(Color.BLACK);
		s2.setForeground(Color.BLACK);
		
		panel_1.add(s1);
		panel_1.add(s2);
		panel_1.add(pp1);
		panel_1.add(pp2);
		
		//MOVE HISTORY
//		Label move_history = new Label("");
//		move_history.setAlignment(Label.CENTER);
//		move_history.setBounds(610, 623, 200, 60);
//		move_history.setBackground(Color.BLACK);
//		move_history.setForeground(Color.WHITE);
//		panel_1.add(move_history);
		
		//UPDATE
		panel_1.validate();
		panel_1.repaint();
	}
	
	//DISPLAY THE WINNER
	public void display_winner(int win) {
		if(win == 1){
			JOptionPane.showMessageDialog(null, "Game is over, the winner is Player 1\n P1 moves: " + p1_count + "       P2 moves: " + p2_count);
		}
		else if(win == 2){
			JOptionPane.showMessageDialog(null, "Game is over, the winner is Player 2\n P1 moves: " + p1_count + "       P2 moves: " + p2_count);
		}
		else{
			JOptionPane.showMessageDialog(null, "Game is over, players tied\n P1 moves: " + p1_count + "       P2 moves: " + p2_count);
		}
	}
}
