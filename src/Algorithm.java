import java.util.Vector;
import java.util.Random;
import java.util.Collections;

public class Algorithm {
	private Vector<Integer> board = new Vector<> ();
	private int house_1, house_2 = 0;
	@SuppressWarnings("unused")
	private int max_coin = 0;
	private boolean extra_move = false;

/*--------------------------------------/
/Constructor and Init                   / 
/--------------------------------------*/	
	//Constructor for the board, takes in cell number, coin number, 
	//and whether it is standard or random configuration
	public Algorithm (int cell_number, int coin_number, char config) {
		Vector<Integer> player_1_cells = new Vector<> ();
		Vector<Integer> player_2_cells = new Vector<> ();
		if(config == 'S'){
			for(int i = 0; i < cell_number; i++){
				player_1_cells.add(coin_number);
				player_2_cells.add(coin_number);
			}
			max_coin = cell_number * coin_number * 2;
		}
		else if(config == 'R'){
			//Total number of coins
			int total_coin = cell_number * coin_number;
			max_coin = total_coin;
			
			//Random generators
			Random rand = new Random();
			int random_coins[] = new int [cell_number];
			total_coin -= cell_number;
			
			for (int i = 0; i < cell_number - 1; i++) {
				random_coins[i] = rand.nextInt(total_coin);
			}
			
			random_coins[cell_number - 1] = total_coin;
			java.util.Arrays.sort(random_coins);
			
			for (int i = cell_number - 1; i > 0; --i) {
				random_coins[i] -= random_coins[i-1];
			}
			
			for (int i = 0; i < cell_number; ++i) {
				++random_coins[i];
			}
			
			//Adding the generated coins into each cell
			for (int i = 0; i < cell_number; i++) {
				player_1_cells.add(random_coins[i]);
				player_2_cells.add(random_coins[i]);
			}
			
		}
		//Add to the board
		board.addAll(player_1_cells);
		board.add(0);
		house_1 = board.size() - 1;
		board.addAll(player_2_cells);
		board.add(0);
		house_2 = board.size() - 1;
		
		//TEST BLOCK -- PASSED
		// print_board();
	}
	
	//to create a copy of the board
	public Algorithm (Vector<Integer> b){
		board.addAll(b);
		house_1 = (board.size() / 2) - 1;
		house_2 = board.size() - 1;
	}
	
	//Display the current board of the game
	public void print_board(){
		for (int i = house_2; i > house_1; i--){
			System.out.print(" " + board.get(i) + " ");
		}
		System.out.println();
		System.out.print("   ");
		for (int i = 0; i < house_1 + 1; i++){
			System.out.print(" " + board.get(i) + " ");
		}
		System.out.println();
		System.out.println();
	}
	
/*--------------------------------------/
/Info Getter and Setter                / 
/--------------------------------------*/
	
	//Return Score for indicated player: takes integer, 1 for player 1, player 2 otherwise
	public int get_score(int player) {
		if (player == 1) {
			return board.get(house_1);
		} else {
			return board.get(house_2);
		}
	}
	public int get_size() {
		return board.size();
	}
	
	public int get_cell(int index) {
		return board.get(index);
	}
	//Verify if this move warrant another move
	public boolean extra_left(){
		return extra_move;
	}
	
	//Allow another move
	public void reward(){
		extra_move = true;
	}
	
	//Commit the extra move
	public void spend_move(){
		extra_move = false;
	}
	
	//Decide on who is leading
	public int ref (){
		if (get_score(1) == get_score(2))
			return 0;
		if (get_score(1) > get_score(2))
			return 1;
		return 2;
	}
	
	//Determine if the game is over
	public boolean end_game(){
		//Game ends when a player has no available options
		int add_coins = 0;
		if(generate_moves(board, 1).size() == 0){
			for(int i = house_2 - 1; i > house_1; i--){
				add_coins += board.get(i);
				board.set(i, 0);
			}
			board.set(house_2, board.get(house_2) + add_coins);
			return true;
		}
		else if(generate_moves(board, 2).size() == 0){
			for(int i = 0; i < house_1; i++){
				add_coins += board.get(i);
				board.set(i, 0);
			}
			board.set(house_1, board.get(house_1) + add_coins);
			return true;
		}
		return false;
	}
	
	//Generate a list of possible moves
	public Vector<Integer> generate_moves(Vector <Integer> new_board, int player){
		Vector<Integer> possible_moves = new Vector <> ();
		if (player == 1){
			for (int i = 0; i < new_board.size()/2-1; i++){
				if (new_board.get(i) > 0){
					possible_moves.add(i);
				}
			}
		} else {
			for (int i = new_board.size()-2; i > new_board.size()/2-1; i--){
				if (new_board.get(i) > 0){
					possible_moves.add(i);
				}
			}
		}
		//Test Block -- Passed
		// System.out.println(possible_moves);
		return possible_moves;
	}
	
	public void print_winner(){
		if(ref() == 1){
			System.out.println("Game is over, the winner is Player 1");
		}
		else if(ref() == 2){
			System.out.println("Game is over, the winner is Player 2");
		}
		else{
			System.out.println("Game is over, players tied");
		}
		
	}
	
/*--------------------------------------/
/Board Modification Functions           / 
/--------------------------------------*/
    
    //Implement pie rule for the board: 1 swap, keep otherwise
	public void pie_rule(int options) {
		if (options == 1) {
			Vector <Integer> swap1 = new Vector <> ();
			Vector <Integer> swap2 = new Vector <> ();
			for (int i = house_2; i > house_1; i--){
				swap1.add(board.get(i));
			}
			for (int i = 0; i < house_1 + 1; i++){
				swap2.add(board.get(i));
			}
			board.removeAll(board);
			board.addAll(swap2);
			board.addAll(swap1);
		}
		
		//Check block --
		//print_board();
	}
	
	//Make a move from the indexed cells: player 1 or 2, index of the cell that make the move
	public boolean move(int index) {
		int players;
		
		//Invalid move, house cell
		if (index == house_1 || index == house_2){
			System.out.println("This is a house!");
			return false;
		}
		
		//Invalid move, empty cell
		if (board.elementAt(index) == 0){
			System.out.println("There are no coins!");
			return false;
		}
		
		if(index < house_1){
			players = 1;
		} else {
			players = 2;
		}
		
		//Obtain the coins, empty the chosen index
		int cointained = board.get(index);
		board.set(index, 0);
		
		//Move Algorithm
		int loc = index + 1;
		while (cointained > 0){
			board.set(loc, board.get(loc) + 1);
	
			//Update the remaining coins
			cointained--;
			
			//COIN MAKE FINAL LAND FALL
			if (cointained == 0){
				//Land on house
				if (loc == house_1 || loc == house_2){
					reward();
					//Test block -- Passed
					//print_board();
				}
				//Land on empty
				if (players == 1){
					if (board.get(loc) == 1 && (loc) != house_1 && loc < house_1 && board.get(house_2 - 1 - loc) != 0){
						//Eat the other side
						board.set(house_1, board.get(house_2 - 1 - loc) + board.get(house_1) + 1);
						board.set(house_2 - 1 - loc, 0);
						board.set(loc, 0);
						//Test block -- PASSED
						//print_board();
					}
				} else{
					if (board.get(loc) == 1 && (loc) != house_2 && loc > house_1 && board.get(house_2 - 1 - loc) != 0){
						//Eat the other side
							board.set(house_2, board.get(house_2 - 1 - loc) + board.get(house_2) + 1);
							board.set(house_2 - 1 - loc, 0);
							board.set(loc, 0);
						//Test block -- PASSED
						//print_board();
					}
				}
			}
			
			loc++;
			//Restart the loop
			if (players == 1){
				if (loc >= house_2){
					loc = 0;
				}
			} else {
				if (loc == house_1){
					loc++;
				}
				if (loc > house_2){
					loc = 0;
				}
			}
		}

		//Test block -- Passed
		// print_board();
		return true;
	}
	
/*--------------------------------------/
/ AI Functions                          / 
/--------------------------------------*/
	//AI random move
	public void AI_random(int player){
		Random rand = new Random();
		int r;
		Vector<Integer> possible_moves = generate_moves(board, player);
		
		r = rand.nextInt(possible_moves.size());
		
		// if (player == 1){
			// r = Math.abs(rand.nextInt()) % house_1;
		// } else {
		// 	r = rand.nextInt((house_2 - 1 - house_1) + 1) + house_1 + 1;
		// }
		
		//Test block -- Passed
		System.out.println("Player " + player + " moved: " + possible_moves.get(r));
		
		move(possible_moves.get(r));
	}
	
	int INFP = 1000;
	int INFN = -1000;
	
	//AI Minimax Tree
	public int AI_mm(int player){
		int mm = minimax(board, player, 10, -1000, 1000).get(1);
		//Test block -- Passed
		System.out.println("Player " + player + " moved: " + mm);
		
		return mm;
	}
	
	//return the index of the best move
	public Vector<Integer> minimax (Vector <Integer> new_board, int player, int depth, int a, int b){
		//contains score and the move made
		Algorithm board_copy = new Algorithm(new_board);
		Vector<Integer> result = new Vector <> ();
		int score;
		int place;
		
		Vector<Integer> possible_moves = generate_moves(new_board, player);
		
		//if game ended or depth==0, return
		if(board_copy.end_game() || depth == 0){
			score = board_copy.get_score(2) - board_copy.get_score(1);
			place = -1;
			result.add(score);
			result.add(place);
			
			//Test Block -- Passed
			// System.out.println("score: "+ board_copy.get_score(2) + " - " + board_copy.get_score(1) + " = " + score);
			return result;
		}
		
		Vector<Integer> move_results = new Vector <> ();
		int move_result;
		//else keep recursing
		if(player == 1){ //player 1
			for(int i=0; i<possible_moves.size(); i++){
				// board_copy.board = new_board;
				Collections.copy(board_copy.board, new_board);
				// System.out.println(player + " " + depth);
				board_copy.move(possible_moves.get(i));
				if(board_copy.extra_left()){
					board_copy.spend_move();
					move_results.add(minimax(board_copy.board, 1, depth-1, a, b).get(0));
				}
				else{
					move_result = (minimax(board_copy.board, 2, depth-1, a, b).get(0));
					
					if(move_result < b){
						b = move_result;
						// System.out.println("set beta to  " + b);
					}
					
					if(move_result <= a){
						// System.out.println(" alpha prune");
						score = move_result;
						place = -1;
						result.add(score);
						result.add(place);
						return result;
					}
					move_results.add(move_result);
				}
				
				
			}
			//find min of move_results
			score = Collections.min(move_results);
		}
		else{ //player 2
			for(int i=0; i<possible_moves.size(); i++){
				// board_copy.board = new_board;
				Collections.copy(board_copy.board, new_board);
				// System.out.println(player + " " + depth);
				board_copy.move(possible_moves.get(i));
				if(board_copy.extra_left()){
					board_copy.spend_move();
					move_results.add(minimax(board_copy.board, 2, depth-1, a, b).get(0));
				}
				else{
					move_result = (minimax(board_copy.board, 1, depth-1, a, b).get(0));
					
					if(move_result > a){
						a = move_result;
						// System.out.println("set alpha to  " + a);
					}
					
					if(move_result >= b){
						// System.out.println(" beta prune");
						score = move_result;
						place = -1;
						result.add(score);
						result.add(place);
						return result;
					}
					move_results.add(move_result);
				}
			}
			//find max of move_results
			score = Collections.max(move_results);
		}
		
		place = possible_moves.get(move_results.indexOf(score));
		
		result.add(score);
		result.add(place);
		
		return result;
	}
}
