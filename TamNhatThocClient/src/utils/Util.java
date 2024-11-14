package utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Util {
	public static void showError (String errorMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
//		alert.setTitle("Lỗi");
//		alert.setHeaderText("Xảy ra lỗi");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
	
    // Phương thức để lấy Stage hiện tại
    public static Stage getCurrentStage() {
        return (Stage) javafx.stage.Window.getWindows().stream()
            .filter(window -> window.isShowing())
            .findFirst()
            .orElse(null);
    }
	public static int showConfirmation (String confirmationMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
//		alert.setTitle("Lỗi");
//		alert.setHeaderText("Xảy ra lỗi");
		alert.setContentText(confirmationMessage);
		ButtonType buttonContinue = new ButtonType("Chơi tiếp");
		
		alert.getButtonTypes().setAll(buttonContinue);
		
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == buttonContinue) {
			System.out.println("choi tiep");
			return 1;
		}
		return 0;
	}
	public static String nowTime() {
		LocalDate now = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return now.format(format);
	}
	public static String showOptionAlert(String message, List<String> buttonLabels) {
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setContentText(message);

	    // Xóa tất cả các nút mặc định trước khi thêm nút tùy chỉnh
	    alert.getButtonTypes().clear();

	    // Tạo các nút bấm từ danh sách truyền vào
	    buttonLabels.forEach(label -> alert.getButtonTypes().add(new ButtonType(label)));

	    // Hiển thị hộp thoại và lấy kết quả
	    Optional<ButtonType> result = alert.showAndWait();
	    if (result.isPresent()) {
	        return result.get().getText();
	    }

	    return null; // Trả về null nếu không có nút nào được chọn
	}

}
