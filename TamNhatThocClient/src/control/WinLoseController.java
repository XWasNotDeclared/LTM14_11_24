package control;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class WinLoseController {
	private Communication commu;
	

    public Communication getCommu() {
		return commu;
	}
	public void setCommu(Communication commu) {
		this.commu = commu;
	}
	@FXML
    private Text WinLose;

    @FXML
    void choiTiepClick(ActionEvent event) {
    	commu.getNavigation().switchTo("Room.fxml");
    	commu.getNavigation().resetScene("WinLose.fxml");
    	commu.getNavigation().resetScene("Game.fxml");
    }
    public void setWinLoseText(String message) {
        WinLose.setText(message);
    }
}
