package application;
	
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Launch Class
 * @author Shady
 */
public class Main extends Application {
	public static long TimeWanted;
	
	/**
	 * Will create the Scene/Window for Centaur Chess.
	 * Before the chess board is loaded, the user is prompted to choose between regular chess or speed chess.
	 */
	@Override
	public void start(Stage stage) throws Exception{
		int choice = -1;
		int minutes = 0;
		int seconds = 0;
		String minInput;
		String secInput;
		
		JPanel MainPanel = new JPanel();
		MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.Y_AXIS));
		Object[] Options = {"Set Time", "Regular Chess Wanted"};
		
		JPanel MinutesPanel = new JPanel();
		JLabel MinutesLabel = new JLabel("Minutes: ");
		JTextField Minutes = new JTextField(5);
		MinutesPanel.add(MinutesLabel); MinutesPanel.add(Minutes);
		
		JPanel SecondsPanel = new JPanel();
		JLabel SecondsLabel = new JLabel("Seconds: ");
		JTextField Seconds = new JTextField(5);
		SecondsPanel.add(SecondsLabel); SecondsPanel.add(Seconds);
		
		MainPanel.add(MinutesPanel); MainPanel.add(SecondsPanel);
		
		choice = JOptionPane.showOptionDialog(null, MainPanel, "Centaur Chess", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, Options, null);
		if(choice == 0) {
			minInput = Minutes.getText().trim(); // removes all whitespace
			secInput = Seconds.getText().trim(); // removes all whitespace
			
			try{
				minutes = Integer.parseInt(minInput);
			}
			catch(NumberFormatException e) {
				minutes = 0;
			}
			try {
				seconds = Integer.parseInt(secInput);
			}
			catch(NumberFormatException e) {
				seconds = 0;
			}
			
			TimeWanted = (minutes * 60) + seconds;
		}
		else if(choice == -1)
			return;
		else
			TimeWanted = -1;
		
		Parent parent = FXMLLoader.load(getClass().getResource("Board.fxml"));
		Scene scene = new Scene(parent);
		stage.setTitle("Centaur Chess");
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
