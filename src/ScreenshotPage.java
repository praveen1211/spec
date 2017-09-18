import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class ScreenshotPage {
	String imagepath;
	File f;
public ScreenshotPage(WebDriver driver, String dest){
	imagepath="C:\\testing\\screen\\"+dest+".png";
	try {
		TakesScreenshot ts=(TakesScreenshot)driver;
		f=ts.getScreenshotAs(OutputType.FILE);
		
		FileUtils.copyFile(f, new File(imagepath) );
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public String subImage(WebElement element, String dest) throws Exception{
	String subPath="C:\\testing\\screen\\"+dest+".png";
	int width=element.getSize().getWidth();
	int height= element.getSize().getHeight();
	
	int x=element.getLocation().getX();
	int y=element.getLocation().getY();
	
	BufferedImage image=ImageIO.read(f);
	BufferedImage test=image.getSubimage(x, y, width, height);
	ImageIO.write(test, "png", f);
	FileUtils.copyFile(f, new File(subPath));
	return subPath;
}
}
