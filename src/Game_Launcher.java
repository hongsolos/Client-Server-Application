import java.awt.EventQueue;
public class Game_Launcher {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Board window = new Main_Board();
					window.Game_Board_Frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
