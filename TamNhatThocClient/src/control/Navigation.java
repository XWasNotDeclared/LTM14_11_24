package control;
import utils.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigation {
	
	private Stage stage;
	
	
	public Stage getStage() {
		return stage;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	private class SceneControllerPair<T>{
		private Scene scene;
		private T controller;
		public SceneControllerPair(Scene scene, T controller) {
			super();
			this.scene = scene;
			this.controller = controller;
		}
		public Scene getScene() {
			return scene;
		}
		public T getController() {
			return controller;
		}
		
	}
	
	
	
	private Map <String, SceneControllerPair<?>> sceneCache = new HashMap<>();
	
	public Navigation(Stage stage) {
		super();
		this.stage = stage;
	}
	
//	public static synchronized Navigation getInstance() {
//		if (instance  == null) {
//			instance = new Na
//		}
//	}
	public void resetScene (String fxmlFile) {
		if (sceneCache.containsKey(fxmlFile)) {
			sceneCache.remove(fxmlFile);
			System.out.println("reset thanh cong" + fxmlFile);
			
		}else {
			System.out.println("khong can reset, chua co" + fxmlFile);
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T switchTo (String fxmlFile) {
		
		try {		
			if (sceneCache.containsKey(fxmlFile)) {
				SceneControllerPair<?> pair = sceneCache.get(fxmlFile);
				stage.setScene(pair.getScene());
				stage.show();
				System.out.println("Da co scene" + fxmlFile + " tra ve controller");
				return (T) pair.getController();	
			}else {
			
			System.out.println("Chua co scene" + fxmlFile + " dang tao");
			String realPathFxmlFile = "/view/"+fxmlFile;
			FXMLLoader loader = new FXMLLoader(getClass().getResource(realPathFxmlFile));
			Parent root = loader.load();
			T controller = loader.getController();
			Scene scene = new Scene(root);
			////////add to cache
			sceneCache.put(fxmlFile, new SceneControllerPair<T>(scene, controller));
			
			
			//////////
			stage.setScene(scene);
			stage.show();
			return controller;
			}
			
		} catch(IOException e) {
			e.printStackTrace();
			Util.showError("Không thể chuyển đến trang: " + fxmlFile);
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public <T> T getController (String fxmlFile) {
		if (sceneCache.containsKey(fxmlFile)){
			return (T) sceneCache.get(fxmlFile).getController();
		}else {
			System.out.println("Chưa có controller cho scene" + fxmlFile);
			return null;
		}
	}
	
	

}
