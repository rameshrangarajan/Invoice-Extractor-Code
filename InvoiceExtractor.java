package InvoiceExtractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONArray; 
//import org.json.simple.JSONObject; 
//import org.json.simple.JSONStreamAware;
//import org.json.simple.parser.*; 
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellAlignment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import InvoiceExtractor.DataComparison;
//import org.json.simple.parser.JSONParser;


public class InvoiceExtractor {
	static int rownum = 0;
	static FileInputStream file = null;
	static FileOutputStream file1 = null;
	public static String fileAbsolutePath = null;
	static FileOutputStream out = null;
	static File[] files = null;
	static BufferedWriter bw = null;
	static int temp;
  @SuppressWarnings("deprecation")
public static void main(String[] args) throws Exception {
	  file = new FileInputStream(new File("D:\\NLProAutomation\\InvoiceExtraction\\Data\\AccuracyCheckResult.xlsx"));
	  XSSFWorkbook workbook = new XSSFWorkbook(file);
	  XSSFSheet sheet = workbook.getSheetAt(1);
    HttpClient httpclient = new DefaultHttpClient();
    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

    HttpPost httppost = new HttpPost("http://10.20.14.88:5005/get_info");
    
    //File f = new File("D:\\Files.txt");
    String filefolder = args[0];
    File f = new File(filefolder);
    files = f.listFiles();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");  
    LocalDateTime now = LocalDateTime.now();
    String dateTime = dtf.format(now); 
    System.out.println(dateTime);
    File file = new File(args[1] + dateTime + ".txt");
    file.createNewFile();
    FileOutputStream fos = new FileOutputStream(file);
    
	bw = new BufferedWriter(new OutputStreamWriter(fos));
    /*List<File> filesInFolder = Files.walk(Paths.get("D:/InvoiceExtractorFiles"))
            .filter(Files::isRegularFile)
            .map(Path::toFile)
            .collect(Collectors.toList());*/

    //BufferedReader b = new BufferedReader(new FileReader(f));
	Double cgstaccuracy = 0.0;
	  Double sgstaccuracy = 0.0;
	  Double igstaccuracy = 0.0;
	  Double gstnumberaccuracy = 0.0;
	  Double invdateaccuracy = 0.0;
	  Double invnumberaccuracy = 0.0;
	  Double totamountaccuracy = 0.0;
	  Double vendornameaccuracy = 0.0;
	  Double amountsaccuracy = 0.0;
	  Double descriptionaccuracy = 0.0;
	  Double[] fieldaccuracy = {cgstaccuracy, sgstaccuracy,igstaccuracy,gstnumberaccuracy,invdateaccuracy,invnumberaccuracy,totamountaccuracy,vendornameaccuracy,amountsaccuracy,descriptionaccuracy};
    //String readLine = "";
   
    //System.out.println("Reading file using Buffered Reader");
    temp = 0;
    while(temp < files.length) {
    //while ((readLine = b.readLine()) != null) {
        //System.out.println(readLine);
    	int rownum1 = 1;
    	String filename = files[temp].getName();
    	System.out.println(filename);
    	Iterator<Row> rowIterator = sheet.iterator();
    	Row row = rowIterator.next();
    	//Iterator<Cell> cellIterator = row.cellIterator();
    	while(rowIterator.hasNext()) {
    		row = rowIterator.next();
    		Cell cell = row.getCell(1);
    		//Cell cell = cellIterator.next();
    		System.out.println("FILENAME CELL VALUE " + cell.getStringCellValue());
    		if(filename.equalsIgnoreCase(cell.getStringCellValue())) {
    			rownum = rownum1;
    			System.out.println("ROW NUMBER ----> " + rownum);
    			break;
    		}
    		else {
    		rownum1++;
    		continue;
    		}
    		
    	}
    
    File file1 = new File(files[temp].getAbsolutePath());
    fileAbsolutePath = file1.getAbsolutePath();
     System.out.println(file1.getAbsolutePath());
     String fileType = Files.probeContentType(file1.toPath());
     //outputFileName = file1.getName()+".txt"
//     String[] filename = file1.getName().split(".");
    MultipartEntity mpEntity = new MultipartEntity();
    ContentBody cbFile = null;
    if(fileType.equalsIgnoreCase("png")) { cbFile = new FileBody(file1, "image/jpeg"); }
    else { cbFile = new FileBody(file1, "pdf"); }
    mpEntity.addPart("file", cbFile);


    httppost.setEntity(mpEntity);
    System.out.println("executing request " + httppost.getRequestLine());
    HttpResponse response = httpclient.execute(httppost);
    HttpEntity resEntity = response.getEntity();

    System.out.println(response.getStatusLine());
    if (resEntity != null) {
    	String output = EntityUtils.toString(resEntity);
    	if(output.contains("Internal Server Error")) {
    		System.out.println(output); 
    		String[] extractedData = null;
    		readWriteToExcel(extractedData, fileAbsolutePath, rownum, workbook,fieldaccuracy);
    		temp++; 
    		//rownum++;
    		//continue;
    		}
    	else {
    	System.out.println(output);
    	String cgst = null;
    	String sgst = null;
    	String igst = null;
        JSONArray gstnumber = null;
        String invoicedate = null;
        String invoicenumber = null;
        String totalamount = null;
        String vendorname = null;
        String cgst1 = "";
        String sgst1 = "";
        String igst1 = "";
        String gstnumber1 = "";
        String invoicedate1 = "";
        String invoicenumber1 = "";
        String totalamount1 = "";
        String vendorname1 = "";
        String items1 = "";
        JSONArray items = null;
    	//Object obj = new JSONParser().parse(output); 
    	JSONTokener tokener = new JSONTokener(output);
    	JSONObject jo = new JSONObject(tokener);
        // typecasting obj to JSONObject 
        //JSONObject jo = (JSONObject) obj; 
   try {
	   JSONObject gstamount = null;
	   if(jo.has("GST_Amount")) {
	   if(jo.get("GST_Amount") != null) {
         gstamount = (JSONObject) jo.get("GST_Amount");
         if(gstamount.has("CGST")) {
         cgst = String.valueOf(gstamount.get("CGST"));
         }
         if(gstamount.has("SGST")) {
         sgst = String.valueOf(gstamount.get("SGST"));
         }
         if(gstamount.has("IGST")) {
             igst = String.valueOf(gstamount.get("IGST"));
             }
         
   }
	   }
        //if(gstamount != null) {
        //cgst = (Double) gstamount.get("CGST"); 
       // sgst = (Double) gstamount.get("SGST"); 
        //}
	   if(jo.has("GST_Number")) {
        gstnumber = (JSONArray) jo.get("GST_Number");}
	   if(jo.has("Invoice_Date")) {
        invoicedate = (String) jo.get("Invoice_Date");}
	   else { invoicedate1 = "NO VALUE"; }
	   if(jo.has("Invoice_Number")) {
        invoicenumber = (String) jo.get("Invoice_Number");
       }
	   else { invoicenumber1 = "NO VALUE"; }
	   if(jo.has("Total_Amount")) {
        if(jo.get("Total_Amount") != null)
        {
        totalamount = jo.get("Total_Amount").toString();
        }
	   }
        //totalamount = (String) jo.get("Total_Amount");
	   if(jo.has("Vendor_Name")) {
        vendorname = (String) jo.get("Vendor_Name");}
	   if(jo.has("Items")) {
        items = (JSONArray) jo.get("Items");}
        if(gstnumber != null) 
        {
        	gstnumber1 = gstnumber.toString();
        	
        }
        else {
        	gstnumber1 = "NO VALUE";
        }
        
        if(vendorname != null) 
        {
        	vendorname1 = vendorname;
         }
        else {
        	
        	vendorname1 = "NO VALUE";
        }
          
        
       if(cgst != null) 
        {
        	cgst1 = cgst.toString();
        }
        else {
        	cgst1 = "NO VALUE";
        }
        
        if(sgst != null) 
        {
        	sgst1 = sgst.toString();
        }
        else {
        	sgst1 = "NO VALUE";
        }
        if(igst != null) 
        {
        	igst1 = igst.toString();
        }
        else {
        	igst1 = "NO VALUE";
        }
        
        
        if(totalamount != null) 
        {
        	totalamount1 = totalamount.toString();
        }
        else {
        	totalamount1 = "NO VALUE";
        }
        
        if(invoicedate != null) 
        {
        	invoicedate1 = invoicedate;
        }
        else {
        	invoicedate1 = "NO VALUE";
        }
        if(invoicenumber != null) 
        {
        	invoicenumber1 = invoicenumber;
        }
        else {
        	invoicenumber1 = "NO VALUE";
        }
        if(items != null)
        {
        	items1 = items.toString();
        }
        else
        {
        	items1 = "NO VALUE";
        }
        
        System.out.println("CGST: " + cgst1); 
        System.out.println("SGST: " + sgst1); 
        System.out.println("IGST: " + igst1);
        System.out.println("GST NUMBER: " + gstnumber1); 
        System.out.println("INVOICE DATE: " + invoicedate1);
        System.out.println("INVOICE NUMBER: " + invoicenumber1); 
        System.out.println("TOTAL AMOUNT: " + totalamount1);
        System.out.println("VENDOR NAME: " + vendorname1);
        System.out.println("ITEMS: " + items1);
        
        } catch (NullPointerException ne) {ne.printStackTrace();}
        
   String amounts = "\"";
   String descriptions = "\"";
   String amounts1 = "";
   String descriptions1 = "";
	   
   //JSONObject itemsobj = (JSONObject)items;
   if(items != null) {
	   
   for(int y = 0; y < items.length(); y++) {
	   JSONObject temp1 = (JSONObject) items.get(y);
	   if(temp1.has("amount")) {
	   if(temp1.get("amount") != null) {
	   Double amount = (Double) temp1.get("amount");
	   amounts = amounts + amount.toString().trim() + "\"" + "," + "\"";
	   }
	   }
	   if(temp1.has("description")) {
	   if(temp1.get("description") != null) {
	   String description = (String) temp1.get("description");
	   if(description.contains("\"")) {description = description.replace("\"", "\\\""); }
	   //if(description.contains(",")) {description = description.replace(",", "\\,"); }
	   //if(description.contains(":")) {description = description.replace(":", "\\:"); }
	   //if(description.contains("/")) {description = description.replace("/", "\\/"); }
	   descriptions = descriptions + description.trim() + "\"" + "," + "\"";
	   }
	   }
   }
   int len1 = 0; int len2 = 0;
   if(!amounts.equals("\"")) {
    len1 = amounts.length() - 2;
    amounts1 = amounts.substring(0, len1);
    }
   else {
	   amounts1 = "NO VALUE";
   }
   //amounts1 = amounts.substring(0, len1);
   if(!descriptions.equals("\"")) {
    len2 = descriptions.length() - 2; 
    descriptions1 = descriptions.substring(0, len2); 
   }
   else {
	   descriptions1 = "NO VALUE";
   }
   //descriptions1 = descriptions.substring(0, len2);
   
   //amounts1 = amounts.toString();
   }
   
   
        System.out.println("GST NUMBER: " + gstnumber1.toString());
        System.out.println("AMOUNTS: " + amounts1);
        System.out.println("DESCRIPTIONS: " + descriptions1);
        //String[] labels = {"CGST", "SGST", "GST NUMBER", "INVOICE DATE", "INVOICE NUMBER", "TOTAL AMOUNT", "VENDOR NAME"};
        String[] extractedData = {cgst1,sgst1,igst1, gstnumber1, invoicedate1,invoicenumber1,
        		totalamount1, vendorname1, amounts1, descriptions1};
        
        
        
        readWriteToExcel(extractedData, fileAbsolutePath, rownum, workbook, fieldaccuracy);
        
    	//bw.write(files[temp].getName());
    	//bw.newLine();
      //Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "utf-8"));
    	
      //for(int k = 0; k < extractedData.length; k++) {
    	  
    	  
      //bw.write(labels[k] + " : " + extractedData[k][0]);
      
      //}
      
        //rownum++;
        temp++;
        /*if(temp != files.length) {
        	bw.newLine();
            bw.write("--------------------------NEXT FILE----------------------------------");
            bw.newLine();
            }*/
      
      
        //cleanUp();
       
    }
    }
    if (resEntity != null) {
      resEntity.consumeContent();
    }
   
    }
    file1 = new FileOutputStream(new File("D:\\NLProAutomation\\InvoiceExtraction\\Data\\AccuracyCheckResult.xlsx"));
    //XSSFWorkbook workbook1 = new XSSFWorkbook(file1);
	  //XSSFSheet sheet = workbook.getSheetAt(1);
    int k = 0;
    int lastrownumber = sheet.getLastRowNum();
    //lastrownumber++;
    System.out.println("LAST ROW NUMBER VALUE : " + lastrownumber);
    System.out.println("CGST ACCURACY : " + fieldaccuracy[0]);
    Row row = sheet.getRow(lastrownumber);
    Iterator<Cell> cellIterator = row.cellIterator();
    cellIterator.next();
    cellIterator.next();
    while(cellIterator.hasNext() && k < fieldaccuracy.length) {
    	Cell cell = cellIterator.next();
    	Double fldaccuracy = (fieldaccuracy[k]/(lastrownumber - 1)) * 100;
    	String finalaccuracy;
		if(fldaccuracy.toString().length() >= 5) {
            finalaccuracy = fldaccuracy.toString().substring(0, 5);
           }
           else {
           	 finalaccuracy = fldaccuracy.toString();
           }
    	String accuracy = finalaccuracy + "%";
        cell.setCellValue(accuracy);
        cellIterator.next();
        //cell = cellIterator.next();
        k++;
    }
    
    workbook.write(file1);
    workbook.close();
    bw.close();
    if(args.length == 2) {
    DataComparison dc = new DataComparison();
    dc.compareData(args[1]);}
    httpclient.getConnectionManager().shutdown();
    
  }
  
  public static void readWriteToExcel(String[] extractedData, String fileAbsolutePath, int rownum, XSSFWorkbook workbook, Double[] fieldaccuracy) throws JSONException {
	  
	  int i = 0; double j = 0;
	  String[] labels = {"CGST", "SGST", "IGST", "GST NUMBER", "INVOICE DATE", "INVOICE NUMBER", "TOTAL AMOUNT", "VENDOR NAME", "AMOUNTS", "DESCRIPTIONS"};
	  
	  try {
		//XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(1);
		System.out.println("ROW NUMBER: " + rownum);
		Row row = sheet.getRow(rownum);
        
            Iterator<Cell> cellIterator = row.cellIterator();
            cellIterator.next();
            cellIterator.next();
            String result = "PASS";
            String servererror = "";
            String result1 = "";
            Cell cell = null;
            String cellValue = null;
            //CellStyle style = workbook.createCellStyle();
            XSSFCellStyle style = workbook.createCellStyle();
            XSSFCellStyle style1 = workbook.createCellStyle();
            XSSFCellStyle style2 = workbook.createCellStyle();
            XSSFCellStyle style3 = workbook.createCellStyle();
            XSSFCellStyle style4 = workbook.createCellStyle();
            XSSFCellStyle style5 = workbook.createCellStyle();
            XSSFCellStyle style6 = workbook.createCellStyle();
            XSSFCellStyle style7 = workbook.createCellStyle();
            bw.write(files[temp].getName());
            while (cellIterator.hasNext() && i < labels.length)
            {
            	if(extractedData == null) {
            		cell = cellIterator.next();
            		cellValue = cell.getStringCellValue();
            		style6.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 0, 0)));
                	
                    style6.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    
                    style6.setAlignment(HorizontalAlignment.RIGHT);
                    style6.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                    
                    style6.setWrapText(true);
                    cell.setCellStyle(style6);
            		result = "FAIL";
            		servererror = "Internal Server Error";
            		System.out.println(labels[i] + " ------> Expected Value: " + cellValue + ", Actual Value: " + 
                    		"NULL");
            		bw.newLine();
            		bw.write("EXPECTED " + labels[i] + " : " + cellValue + ", ACTUAL " + labels[i] + " : [ NULL ] ------------ FAILED");
            		//bw.write("FAILED");
            		cell = cellIterator.next();
            		cell.setCellValue("NULL");
            		j++;
            		i++;
            		continue;
            	}
            	int m = 0;
            	JSONArray expectedValues = new JSONArray();
        		JSONArray actualValues = new JSONArray();
                cell = cellIterator.next();
                if(cell.getCellTypeEnum() == CellType.STRING) {
                cellValue = cell.getStringCellValue();}
                if(cell.getCellTypeEnum() == CellType.NUMERIC) {
                	cellValue = String.valueOf(cell.getNumericCellValue());
                }
                System.out.println(labels[i] + " ------> Expected Value: " + cellValue + ", Actual Value: " + 
                		extractedData[i]);
                String expectedValue1 = cellValue.replaceAll("\\s+", "");
                String actualValue1 = extractedData[i].replaceAll("\\s+", "");
            	bw.newLine();
            	//bw.write("EXPECTED " + labels[i] + " : " + cellValue + ", ACTUAL " + labels[i] + " : " + extractedData[i][0]);
            	if(labels[i].equalsIgnoreCase("GST NUMBER") || labels[i].equalsIgnoreCase("AMOUNTS") || labels[i].equalsIgnoreCase("DESCRIPTIONS")) {
            		String c = "";
            		String b = "";
            		String temp1 = "";
            		if((labels[i].equalsIgnoreCase("AMOUNTS") || labels[i].equalsIgnoreCase("DESCRIPTIONS")) 
            				&& !extractedData[i].equalsIgnoreCase("NO VALUE")) {
            		 temp1 = "[" + extractedData[i] + "]";
            			b = "[" + actualValue1 + "]";
            		}
            		if(labels[i].equalsIgnoreCase("GST NUMBER") && !extractedData[i].equalsIgnoreCase("NO VALUE")) {
            			temp1 = extractedData[i];
            			b = actualValue1;
            		}
            		if(extractedData[i].equalsIgnoreCase("NO VALUE")) {
            			temp1 = "[" + extractedData[i] + "]";
            			b = "[" + actualValue1 + "]";
            			
            		}
            		System.out.println(b);
            		JSONArray jsonArray = new JSONArray(b);
            		if(cellValue.equalsIgnoreCase("NO VALUE")) {
            		 c = "[" + expectedValue1 + "]";
            		}
            		else {
            			c = expectedValue1;
            		}
            		JSONArray jsonArray1 = new JSONArray(c);
            		//JSONArray h = b.
            		//Object obj = (Object)b;
            		//JSONObject jsonobj = ((StringList)extractedData[i]).toJSON();
            		//JSONArray arr = new JSONArray(b);
            		 expectedValues = new JSONArray(c);
            		 actualValues = new JSONArray(b);
            		//if(jsonArray.length() != jsonArray1.length()) {
            		for(int l = 0; l < jsonArray.length(); l++) {
            			for(int p = 0; p < jsonArray1.length(); p++) {
            				if(jsonArray.get(l).toString().equalsIgnoreCase(jsonArray1.get(p).toString())) {
            					m++;
            					//continue;
            					jsonArray1.remove(p);
            					break;
            				}
            				
            			}
            		}
            		System.out.println("EXPECTED VALUES OF " + labels[i] + "----> " + jsonArray1);
            		System.out.println(expectedValues + "==> Length of expectedValues array = " + expectedValues.length());
            		System.out.println("m = " + m);
            		//}
            		
            		if((expectedValues.length() == actualValues.length()) && m == expectedValues.length()) { 
            			System.out.println("INSIDE PASS IF CONSTRUCT");
            			//result = "PASS";
            			fieldaccuracy[i]++;
            			bw.write("EXPECTED " + labels[i] + " : " + cellValue + ", ACTUAL " + labels[i] + " : " + temp1 
                    			+ " ------------ PASSED");
                    	style3.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 255, 0)));
                    	
                        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        
                        style3.setAlignment(HorizontalAlignment.RIGHT);
                        style3.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                        
                        style3.setWrapText(true);
                        cell.setCellStyle(style3);
                        }
            		
                       if((expectedValues.length() != actualValues.length()) && m > 0) { 
                    	   System.out.println("INSIDE PARTIALLY PASS IF CONSTRUCT");
                    	   fieldaccuracy[i] += 0.5;
                    	   result = "FAIL";
               			j = j + 0.5;
               			bw.write("EXPECTED " + labels[i] + " : " + cellValue + ", ACTUAL " + labels[i] + " : " + temp1 
                       			+ " ------------ PARTIALLY PASS");
               			style7.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 100, 255)));
                       	
               			style7.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                           
               			style7.setAlignment(HorizontalAlignment.RIGHT);
               			style7.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                           
               			style7.setWrapText(true);
                           cell.setCellStyle(style7);
                        }
                       
            		if((actualValues.length() == expectedValues.length()) && (m > 0 && m < expectedValues.length())) { 
            			System.out.println("INSIDE PARTIALLY PASS IF CONSTRUCT");
            			fieldaccuracy[i] += 0.5;
            			result = "FAIL";
            			j = j + 0.5;
            			bw.write("EXPECTED " + labels[i] + " : " + cellValue + ", ACTUAL " + labels[i] + " : [" + extractedData[i] 
                    			+ "] ------------ PARTIALLY PASS");
                    	style4.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 100, 255)));
                    	
                        style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        
                        style4.setAlignment(HorizontalAlignment.RIGHT);
                        style4.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                        
                        style4.setWrapText(true);
                        cell.setCellStyle(style4);	
            		}
            		if(m == 0) {
            			System.out.println("INSIDE FAIL IF CONSTRUCT");
            			result = "FAIL";
            			j = j + 1;
            			bw.write("EXPECTED " + labels[i] + " : " + cellValue + ", ACTUAL " + labels[i] + " : " + temp1 
                    			+ " ------------ FAILED");
                    	style5.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 0, 0)));
                    	
                        style5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        
                        style5.setAlignment(HorizontalAlignment.RIGHT);
                        style5.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                        
                        style5.setWrapText(true);
                        cell.setCellStyle(style5);
            			}
            		
            		//for(int l = 0; l < extractedData[i][j].length())
            	}
            	else {
          try {
                if(!actualValue1.equalsIgnoreCase(expectedValue1)) {
                	result = "FAIL";
                	j = j + 1;
                	bw.write("EXPECTED " + labels[i] + " : " + cellValue + ", ACTUAL " + labels[i] + " : " + extractedData[i] 
                			+ " ------------ FAILED");
                	//style = workbook.createCellStyle();
                	style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 0, 0)));
                	//style1.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    //style.setFillBackgroundColor(IndexedColors.DARK_RED.getIndex());
                    //style.setFillPattern(FillPatternType.NO_FILL);
                    style.setAlignment(HorizontalAlignment.RIGHT);
                    style.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                    //style.setAlignment(CellStyle.VERTICAL_TOP);
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    
                }
                if(actualValue1.equalsIgnoreCase(expectedValue1)) {
                	fieldaccuracy[i]++;
                	//style = workbook.createCellStyle();
                	bw.write("EXPECTED " + labels[i] + " : " + cellValue + ", ACTUAL " + labels[i] + " : " + extractedData[i] 
                			+ " ------------ PASSED");
                	style2.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 255, 0)));
                	//style1.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
                    style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                	//style2.setFillBackgroundColor(IndexedColors.DARK_GREEN.getIndex());
                    //style2.setFillPattern(FillPatternType.NO_FILL);
                    style2.setAlignment(HorizontalAlignment.RIGHT);
                    style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                    //style2.setAlignment(XSSFCellStyle.VERTICAL_TOP);
                    style2.setWrapText(true);
                    cell.setCellStyle(style2);
                }
                //cell.setCellStyle(style);
                
              } 
            catch (NullPointerException ne) {ne.printStackTrace();}
            	}     
                    cell = cellIterator.next();
                    
                    cell.setCellValue(extractedData[i]);
                    style1.setAlignment(HorizontalAlignment.RIGHT);
                    style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                    //style1.setAlignment(VerticalAlignment.TOP);
                    style1.setWrapText(true);
                    cell.setCellStyle(style1);
                    i++;
                    //continue;
            }
            if(temp != files.length - 1) {
            	bw.newLine();
                bw.write("--------------------------NEXT FILE----------------------------------");
                bw.newLine();
                }
            cell = cellIterator.next();
            //Font font = workbook.createFont();
            //CellStyle style1 = workbook.createCellStyle();
            //XSSFCellStyle style1 = workbook.createCellStyle();
            if(result.equalsIgnoreCase("FAIL")) 
            {
            	
            	System.out.println(fileAbsolutePath + "----- FAIL");
            	//font.setColor(XSSFColor.toXSSFColor(RED.index));
            	//XSSFCellStyle style1 = (XSSFCellStyle)cell.getCellStyle();
            	//XSSFColor myColor = new XSSFColor(Color.);
            	
            	cell.setCellValue(result);
            	
                style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 0, 0)));
            	//style1.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style.setAlignment(HorizontalAlignment.RIGHT);
                //style.setAlignment(CellStyle.VERTICAL_TOP);
                style.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                style.setWrapText(true);
            	cell.setCellStyle(style);
            }
            else
            {
            	//result = "PASS";
            	
            	System.out.println(fileAbsolutePath + "----- PASS");
            	
            	cell.setCellValue(result);
            	style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 255, 0)));
            	style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            	style.setAlignment(HorizontalAlignment.RIGHT);
                style.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
                style.setWrapText(true);
            	//style1.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
            	cell.setCellStyle(style);
            }
            cell = cellIterator.next();
            String finalaccuracy = "";
            Double accuracy = (1 - (double) (j/(labels.length))) * 100;
            System.out.println("ACCURACY: " + accuracy);
            if(accuracy.toString().length() >= 5) {
             finalaccuracy = accuracy.toString().substring(0, 5);
            }
            else {
            	 finalaccuracy = accuracy.toString();
            }
            if(!servererror.equals("")){
            cell.setCellValue(servererror + " " + "0.00%");
            }
            else {
            	cell.setCellValue(finalaccuracy + "%");
            }
            FileOutputStream out = new FileOutputStream(new File("D:\\NLProAutomation\\InvoiceExtraction\\Data\\AccuracyCheckResult.xlsx"));
            workbook.write(out);
            //workbook.close();
            //out.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        }
  
  public static void cleanUp() throws IOException {
  //out.close();
  file.close();
  }
}
  