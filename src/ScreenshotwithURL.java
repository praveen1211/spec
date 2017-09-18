import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;


public class ScreenshotwithURL {
	String imagepath;
public ScreenshotwithURL(WebDriver driver, String dest){
	try {
		imagepath="C:\\testing\\screen\\"+dest+".png";
		BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, "png", new File("C:\\testing\\office\\result.png"));
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
