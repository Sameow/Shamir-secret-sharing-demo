package engine;

//-----------------------------------------------------------------------------
//ReadFileIntoByteArray.java
//-----------------------------------------------------------------------------

/*
* =============================================================================
* Copyright (c) 1998-2011 Jeffrey M. Hunter. All rights reserved.
* 
* All source code and material located at the Internet address of
* http://www.idevelopment.info is the copyright of Jeffrey M. Hunter and
* is protected under copyright laws of the United States. This source code may
* not be hosted on any other site without my express, prior, written
* permission. Application to host any of the material elsewhere can be made by
* contacting me at jhunter@idevelopment.info.
*
* I have made every effort and taken great care in making sure that the source
* code and other content included on my web site is technically accurate, but I
* disclaim any and all responsibility for any loss, damage or destruction of
* data or any other property which may arise from relying on it. I will in no
* case be liable for any monetary damages arising from such loss, damage or
* destruction.
* 
* As with any code, ensure to test this code in a development environment 
* before attempting to run it in production.
* =============================================================================
*/

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;


public class ReadFileIntoByteArray {


 /**
  * method to convert a byte to a hex string.
  *
  * @param data the byte to convert
  * @return String the converted byte
  */
 public static String byteToHex(byte data) {

     StringBuffer buf = new StringBuffer();
     buf.append(toHexChar((data >>> 4) & 0x0F));
     buf.append(toHexChar(data & 0x0F));

     return buf.toString();
 }


 /**
  * Convenience method to convert an int to a hex char.
  *
  * @param i the int to convert
  * @return char the converted char
  */
 public static char toHexChar(int i) {
     if ((0 <= i) && (i <= 9)) {
         return (char) ('0' + i);
     } else {
         return (char) ('a' + (i - 10));
     }
 }


 /**
  * Returns the contents of the file in a byte array
  * @param file File this method should read
  * @return byte[] Returns a byte[] array of the contents of the file
  */
 public static byte[] getBytesFromFile(File file) throws IOException {
	 
	 FileInputStream fileInputStream=null;
	 byte[] fileByte = new byte[(int) file.length()];
	 
	 fileInputStream = new FileInputStream(file);
	 fileInputStream.read(fileByte);
	 fileInputStream.close();
	    
	 return fileByte;

 }

	/**
	 * Method to convert byte array to file
	 * 
	 * @param bytearray
	 * @return
	 * @throws IOException
	 */
	public static void byteArrayToFile(byte[] byteArray, String fileName) throws IOException {
		
		FileOutputStream fileOuputStream = new FileOutputStream(fileName); 
		for(int i=0; i<byteArray.length; i++){
	    fileOuputStream.write(byteArray[i]);
		}
	    
	    fileOuputStream.close();
	
	    
//		String content = new String(byteArray, "UTF-8");
//		System.out.println(content);
//		File file = new File(fileName);
//		FileWriter fw = new FileWriter(file.getAbsoluteFile());
//		BufferedWriter bw = new BufferedWriter(fw);
//		
//		bw.write(content);
//		bw.newLine();
//		
//		bw.close();
		
		//return file;
	}

 /**
  * Sole entry point to the class and application.
  * @param args Array of String arguments.
  */
// public static void main(String[] args) {
//
//     byte[] fileArray = null;
//
//     try {
//         fileArray = getBytesFromFile(new File("README_InputFile.txt"));
//     } catch (IOException e) {
//         e.printStackTrace();
//     }
//
//     if (fileArray != null) {
//         for (int i=0; i<fileArray.length; i++) {
//
//             System.out.println(
//                 "fileArray[" + i + "] = " +
//                 ((int)fileArray[i] < 9  ? "  " : "") +
//                 ( ((int)fileArray[i] > 9 && (int)fileArray[i] <= 99) ? " " : "") +
//                 fileArray[i] + " : " +
//                 " HEX=(0x" + byteToHex(fileArray[i]) + ") : " +
//                 " charValue=(" + (char)fileArray[i] + ")");
//         }
//     }
//
// }

}
