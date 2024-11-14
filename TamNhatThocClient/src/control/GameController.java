package control;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color; // Thêm import này
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {
    private Communication commu;

    public Communication getCommu() {
        return commu;
    }

    public void setCommu(Communication commu) {
        this.commu = commu;
    }

    @FXML
    private Pane gamePane;

    private List<ImageView> imageViews = new ArrayList<>();
    private Image[] images;

    private double xOffset = 0;
    private double yOffset = 0;

    private static int START_TIME = 60; // Đếm ngược từ 60 giây
    private int timeRemaining;
    private Label timerLabel; // Hiển thị đồng hồ đếm ngược
    private Timeline countdownTimeline;

    public Timeline getCountdownTimeline() {
        return countdownTimeline;
    }

    public void setCountdownTimeline(Timeline countdownTimeline) {
        this.countdownTimeline = countdownTimeline;
    }

    public void initialize() {
        // Tăng kích thước Pane để phù hợp với màn hình ngang
        gamePane.setPrefSize(800, 550); // Kích thước ngang 800x500

        // Thiết lập màu nền cho gamePane
        gamePane.setStyle("-fx-background-color: gray;"); // Thiết lập màu nền xám

        // Thêm ảnh nền BG.png
        ImageView background = new ImageView(getImage("BG.png"));
        background.setFitWidth(800);
        background.setFitHeight(500);
        gamePane.getChildren().add(background);
    }

    private void setupCountdownTimer() {
        timeRemaining = START_TIME;
        timerLabel = new Label("Time: " + timeRemaining);
        timerLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        timerLabel.setLayoutX(10);
        timerLabel.setLayoutY(10);
        gamePane.getChildren().add(timerLabel);

        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            timerLabel.setText("Time: " + timeRemaining);

            if (timeRemaining <= 0) {
                countdownTimeline.stop();
                timerLabel.setText("Time's up!");
                try {
					commu.sendMessage(new Message("TIME_UP", null));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                // Thực hiện thêm hành động khi hết giờ, ví dụ: kết thúc trò chơi
            }
        }));

        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        countdownTimeline.play();
    }

    public void createGame(int numImages, int numTempFAKE, int time, long randomSeed) {
        START_TIME = time;
        System.out.println("time: " + START_TIME);
        setupCountdownTimer();

        Random random = new Random(randomSeed);

        // Tạo hình ảnh từ tệp PNG
        images = new Image[2];
//        images[0] = new Image(getClass().getResource("a.png").toExternalForm());
//        images[1] = new Image(getClass().getResource("b.png").toExternalForm());
	      images[0] = getImage("a.png");
	      images[1] = getImage("b.png");
        String[] basketNames = {"Thóc", "Gạo"}; // Danh sách tên giỏ
        // Tạo các hình ảnh cho trò chơi xuất hiện trong phạm vi của vòng tròn
        double centerX = 400; // Tọa độ X tâm
        double centerY = 250; // Tọa độ Y tâm
        double radius = 100;  // Bán kính

        for (int i = 0; i < numImages; i++) {
            Image image = images[random.nextInt(2)];
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(80); // Kích thước ảnh lớn hơn
            imageView.setFitHeight(80);

            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = random.nextDouble() * radius;
            double x = centerX + distance * Math.cos(angle) - imageView.getFitWidth() / 2;
            double y = centerY + distance * Math.sin(angle) - imageView.getFitHeight() / 2;

            // Đảm bảo ảnh nằm hoàn toàn trong vòng tròn
            while (Math.pow(x + imageView.getFitWidth() / 2 - centerX, 2) +
                   Math.pow(y + imageView.getFitHeight() / 2 - centerY, 2) > Math.pow(radius, 2)) {
                distance = random.nextDouble() * radius;
                x = centerX + distance * Math.cos(angle) - imageView.getFitWidth() / 2;
                y = centerY + distance * Math.sin(angle) - imageView.getFitHeight() / 2;
            }

            imageView.setX(x);
            imageView.setY(y);
            imageViews.add(imageView);
            gamePane.getChildren().add(imageView);

            addDragFunctionality(imageView);
        }

        // Tạo các hình chữ nhật để thả ảnh vào và thêm hình ảnh giỏ
        for (int i = 0; i < images.length; i++) {
            Rectangle rectangle = new Rectangle(150, 100); // Kích thước hình chữ nhật
            rectangle.setX(100 + i * 450); // Điều chỉnh khoảng cách giữa các hình chữ nhật cho màn hình ngang
            rectangle.setY(400);
            rectangle.setFill(null); // Không tô màu nền
            rectangle.setStrokeWidth(2);
            rectangle.setStroke(javafx.scene.paint.Color.BLACK);
            gamePane.getChildren().add(rectangle);

            // Tạo ImageView cho giỏ
//            ImageView basketView = new ImageView(new Image(getClass().getResource("gio.png").toExternalForm()));
            ImageView basketView = new ImageView(getImage("gio.png"));
            basketView.setFitWidth(150); // Kích thước giỏ
            basketView.setFitHeight(100);
            basketView.setX(rectangle.getX()); // Đặt vị trí giỏ theo hình chữ nhật
            basketView.setY(rectangle.getY());
            gamePane.getChildren().add(basketView);
            
            // Thêm Label dưới mỗi giỏ (thóc và gạo)
            Label basketLabel = new Label(basketNames[i]); // Dùng tên giỏ từ mảng basketNames
            basketLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: black;");
            basketLabel.setLayoutX(rectangle.getX() + (rectangle.getWidth() - basketLabel.getWidth()) / 2);
            basketLabel.setLayoutY(rectangle.getY() + rectangle.getHeight()+5); // Đặt dưới giỏ
            gamePane.getChildren().add(basketLabel);
            
        }
    }

    private void addDragFunctionality(ImageView imageView) {
        imageView.setOnMousePressed((MouseEvent event) -> {
            gamePane.getChildren().remove(imageView);
            gamePane.getChildren().add(imageView);

            xOffset = event.getSceneX() - imageView.getX();
            yOffset = event.getSceneY() - imageView.getY();
        });

        imageView.setOnMouseDragged((MouseEvent event) -> {
            imageView.setX(event.getSceneX() - xOffset);
            imageView.setY(event.getSceneY() - yOffset);
        });

        imageView.setOnMouseReleased((MouseEvent event) -> {
            for (int i = 0; i < images.length; i++) {
                if (isImageViewInRectangle(imageView, 100 + i * 450, 400, 150, 100)) {
                    if (imageView.getImage().equals(images[i])) {
                        gamePane.getChildren().remove(imageView);
                        imageViews.remove(imageView);
                        if (imageViews.isEmpty()) {
                            System.out.println("You Win!");
                            try {
                                commu.sendMessage(new Message("I_WIN", Integer.valueOf(timeRemaining)));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return;
                    }
                }
            }
        });
    }

    private boolean isImageViewInRectangle(ImageView imageView, double rectX, double rectY, double rectWidth, double rectHeight) {
        double imageViewX = imageView.getX();
        double imageViewY = imageView.getY();
        double imageWidth = imageView.getFitWidth();
        double imageHeight = imageView.getFitHeight();

        return imageViewX + imageWidth > rectX && imageViewX < rectX + rectWidth &&
               imageViewY + imageHeight > rectY && imageViewY < rectY + rectHeight;
    }
	private Image getImage(String imageName) {
	    // Đường dẫn tuyệt đối từ thư mục gốc của dự án
	    String imagePath = "res/" + imageName;

	    try {
	        // Tạo đối tượng File từ đường dẫn
	        File file = new File(imagePath);
	        
	        // Kiểm tra xem file có tồn tại không
	        if (file.exists()) {
	            // Tạo đối tượng Image từ tệp ảnh
	            Image image = new Image(file.toURI().toString());
	            return image;
	        } else {
	            System.out.println("Không tìm thấy ảnh: " + imagePath);
	            return null;
	        }
	    } catch (Exception e) {
	        System.out.println("Lỗi khi lấy ảnh: " + e.getMessage());
	        return null;
	    }
	}
}
