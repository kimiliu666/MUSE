package muse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import muse.util.publicParameter;

public class FileWrite {
	
	
	
	public static void main(String[] args) throws IOException {
		
		FileOutputStream fileOutputStream = null;
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			File file=new File("./log.txt");
			fileOutputStream = new FileOutputStream(file,true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		OutputStreamWriter ow=new OutputStreamWriter(fileOutputStream);
		ow.append("\r\n");
		ow.append("=============="+dateformat.format(new Date(System.currentTimeMillis()))+"=================att_number"+publicParameter.ATT_SIZE);
		ow.append("\r\n");
		MUSEMain.pbcExperiments(30,ow,2,3);
		
		ow.close();
		System.out.println("========–¥»ÎÕÍ≥…£°========");
	}
	
}
