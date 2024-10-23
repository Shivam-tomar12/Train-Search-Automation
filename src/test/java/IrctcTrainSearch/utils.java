package IrctcTrainSearch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class utils {
 
	static String[] data = new String[5];

public static String[] readdata(String fileName) throws IOException, FileNotFoundException
{
	FileInputStream file=new FileInputStream("C:\\Users\\DELL\\eclipse-workspace\\IrctcTrain\\target\\Book.xlsx");
	XSSFWorkbook workbook=new XSSFWorkbook(file);
	XSSFSheet sheet=workbook.getSheetAt(0);
	XSSFRow row=sheet.getRow(1);
	int tot_col=sheet.getRow(1).getLastCellNum();
	for(int i=0;i<tot_col;i++)
	{
		data[i]=String.valueOf(row.getCell(i));
	}
	workbook.close();
	file.close();
	return data;
}
}