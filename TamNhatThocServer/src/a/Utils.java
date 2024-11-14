package a;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; // Thêm import cho LocalDateTime

public class Utils {
//	static private Utils instance;
//
//	public static Utils getInstance() {
//		if (instance == null) {
//			instance = new Utils();
//		}
//		
//		return instance;
//	}



	public static String nowTime() {
	    LocalDateTime now = LocalDateTime.now(); // Sử dụng LocalDateTime
	    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    return now.format(format);
	}

	
	
}
