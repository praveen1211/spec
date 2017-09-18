import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReadExcelSheet {
XSSFWorkbook wb;
XSSFSheet sheet;
File f;
FileInputStream fis;
public ReadExcelSheet(String path){
	try {
		f= new File(path);
		fis = new FileInputStream(f);
		wb= new XSSFWorkbook(fis);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
public String readData(String index, int row, int cell){
	sheet = wb.getSheet(index);
	Row rows=sheet.getRow(row);
	Cell cell1=rows.getCell(cell);
	String data;
	if(cell1 != null){
	
		if(cell1.getCellType()==cell1.CELL_TYPE_NUMERIC){
//		int value=(int) cell1.getNumericCellValue();
		return NumberToTextConverter.toText(cell1.getNumericCellValue());
		}
		else{
		data=cell1.toString();
		return data;
		}
	}
	else{
		data= "empty";
		return data;
	}
}

public int columnCount(String index){
	sheet=wb.getSheet(index);
	int numColumns=sheet.getRow(0).getPhysicalNumberOfCells();
	return numColumns;
}
public int count(String index){
	sheet = wb.getSheet(index);
	int totalrows=sheet.getLastRowNum();
	return totalrows;
}
public void removeCells(String name, int cellNumber) throws Exception{
	
	sheet=wb.getSheet(name);
	CellStyle style=wb.createCellStyle();
	for (int i=2;i<=count(name);i++){
		Row getRow=sheet.getRow(i);
		Cell cell1=getRow.getCell(cellNumber);
		if(cell1!=null){
		getRow.removeCell(cell1);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cell1= sheet.getRow(i).createCell(cellNumber);
		cell1.setCellStyle(style);
		FileOutputStream dest= new FileOutputStream(f);
		wb.write(dest);
		}
	}
	
}
}
