import java.util.Properties;


import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;



public class Extents {
	protected static ExtentReports er1;
	protected static ExtentTest test,parent;
	protected static String folder;
	// for saving reports
	public Extents(){
	folder="C:\\testing\\Reports\\Kohler"+new Timing().timeReport()+".html";
	er1= new ExtentReports(folder,true);
	information();
	}
	protected void information(){
		er1.addSystemInfo("Automation Tool","Selenium");
		er1.addSystemInfo("Framework","Keyword Driven framework");
		er1.addSystemInfo("Created by","Vamsi Kattamudi");
	}
	
	protected void endExtent(){
		// end test
        er1.endTest(test);
        
        // calling flush writes everything to the log file
        er1.flush();
	}

	protected void endParentExtent(){
		// end test
        er1.endTest(parent);
        
        // calling flush writes everything to the log file
        er1.flush();
	}

}
