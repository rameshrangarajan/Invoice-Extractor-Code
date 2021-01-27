package InvoiceExtractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
//import InvoiceExtractor.InvoiceExtractor;

public class DataComparison {

	public static String filefolder = "";
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//filefolder = args[0];
		
		DataComparison dc = new DataComparison();
		//dc.compareData(filefolder);
	}
		//File file1 = new File("D://InvoiceExtractorOutputData//20-06-2019 12-47-37.txt");
		//File file2 = new File("D://InvoiceExtractorOutputData//20-06-2019 13-23-48.txt");
		public void compareData(String filefolder) throws IOException {
		File[] files = null;
		//File f = new File(filefolder);
		 System.out.println("InvoiceExtractorOutputData -: " + filefolder);
		File f = new File(filefolder);
	    files = f.listFiles();
	    /*Arrays.sort( files, new Comparator()
	    {
	        public int compare(Object o1, Object o2) {

	            if (((File)o1).lastModified() > ((File)o2).lastModified()) {
	                return -1;
	            } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
	                return +1;
	            } else {
	                return 0;
	            }
	        }

	    }); */
	    
	    Arrays.sort(files, Comparator.comparingLong(File::lastModified));
	    int x = files.length;
	    String file2 = files[x-1].getAbsolutePath();
	    String file1 = files[x-2].getAbsolutePath();
		System.out.println(file1);
		System.out.println(file2);
		BufferedReader reader1 = new BufferedReader(new FileReader(file1));
		BufferedReader reader2 = new BufferedReader(new FileReader(file2));
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");  
	    LocalDateTime now = LocalDateTime.now();
	    String dateTime = dtf.format(now);
		File file = new File("D:/" + "Invoice Extractor Comparison Result/" + "Changed Comparison Result " + dateTime + ".txt");
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
	    
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		String line1 = reader1.readLine();
		String line2 = reader2.readLine();
		String temp = null;
		while(line1 != null && line2 != null) {
			//String line1 = reader1.readLine();
			//String line2 = reader2.readLine();
			/*if((!(line1.contains("PASSED") || line1.contains("FAILED") || line1.contains("NEXT FILE") || line1.contains("PARTIALLY PASS")) && 
					(line1.equalsIgnoreCase(line2)))) {*/
			if(line1.contains(".png") || line1.contains(".pdf")) {
				temp = line1;
				
				System.out.println("\n" + "FOR FILE : " + line1 + "-------->" + "\n");
				//continue;
			}
			if((line1.contains("PASSED") && line2.contains("PASSED")) || 
					(line1.contains("FAILED") && line2.contains("FAILED")) || 
					(line1.contains("PARTIALLY PASS") && line2.contains("PARTIALLY PASS"))) {
				System.out.println(line1 +  "-----------> " + "NO CHANGE");
				//continue;
			}
			if((line1.contains("PASSED") && line2.contains("FAILED"))) {
				bw.newLine();
				bw.write("FOR FILE : " + temp + "-------->");
				bw.newLine();
				bw.newLine();
				bw.write("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS PASS" + "======> NOW FAIL");
				bw.newLine();
				System.out.println("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS PASS" + "======> NOW FAIL");
			}
				
			if((line1.contains("FAILED") && line2.contains("PASSED"))) {
				bw.newLine();
				bw.write("FOR FILE : " + temp + "-------->");
				bw.newLine();
				bw.newLine();
				bw.write("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS FAIL" + "======> NOW PASS");
				bw.newLine();
				System.out.println("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS FAIL" + "======> NOW PASS");
				}
			if(line1.contains("PARTIALLY PASS") && line2.contains("FAILED")) {
				bw.newLine();
				bw.write("FOR FILE : " + temp + "-------->");
				bw.newLine();
				bw.newLine();
				bw.write("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS PARTIALLY PASS" + "======> NOW FAIL");
				bw.newLine();
				System.out.println("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS PARTIALLY PASS" + "======> NOW FAIL");
			}
			if(line1.contains("PARTIALLY PASS") && line2.contains("PASSED")) {
				bw.newLine();
				bw.write("FOR FILE : " + temp + "-------->");
				bw.newLine();
				bw.newLine();
				bw.write("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS PARTIALLY PASS" + "======> NOW PASS");
				bw.newLine();
				System.out.println("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS PARTIALLY PASS" + "======> NOW PASS");
			}
			if(line1.contains("PASSED") && line2.contains("PARTIALLY PASS")) {
				bw.newLine();
				bw.write("FOR FILE : " + temp + "-------->");
				bw.newLine();
				bw.newLine();
				bw.write("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS PASS" + "======> NOW PARTIALLY PASS");
				bw.newLine();
				System.out.println("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS PASS" + "======> NOW PARTIALLY PASS");
			}
			if(line1.contains("FAILED") && line2.contains("PARTIALLY PASS")) {
				bw.newLine();
				bw.write("FOR FILE : " + temp + "-------->");
				bw.newLine();
				bw.newLine();
				bw.write("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS FAIL" + "======> NOW PARTIALLY PASS");
				bw.newLine();
				System.out.println("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS FAIL" + "======> NOW PARTIALLY PASS");
			}
			/*if(line1.contains("Internal Server Error") && !line2.contains("Internal Server Error")) {
				bw.newLine();
				bw.write("FOR FILE : " + temp + "-------->");
				bw.newLine();
				bw.newLine();
				bw.write("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS FAIL" + "======> NOW PARTIALLY PASS");
				bw.newLine();
				System.out.println("[PREVIOUS => " + line1 + "]" + " [CURRENT => " + line2 + "] " + "======> WAS FAIL" + "======> NOW PARTIALLY PASS");
			}*/
			line1 = reader1.readLine();
			line2 = reader2.readLine();
			}
		bw.close();
		}
	}




