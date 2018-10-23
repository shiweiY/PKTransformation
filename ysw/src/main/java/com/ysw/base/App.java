package com.ysw.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ysw.model.Typer;

public class App 
{
	public static final List<String> DATATYPE = new ArrayList<String>();
	
	public static void main( String[] args ) throws Exception
	{
		String fileDir = "D:\\pk_temp\\type\\test";
		File getFile = new File(fileDir);
		String[] strFileList = getFile.list();

		String filePath = fileDir.toString()+"\\"+strFileList[0].toString();

		String str = readFile(filePath);
		getTyper(str);
		System.out.println(str);
	}

	public static Typer getTyper(String str){
		if(!"".equals(str) && str != null){
//			str = str.toLowerCase();//转换小写
			str = str.toUpperCase();//转换大写
			
			DATATYPE.add("VARCHAR");
			DATATYPE.add("INTEGER");
			DATATYPE.add("DATE");
			DATATYPE.add("NUMBER");
			
			
			int nameLeft = str.indexOf("\"");//typeName的左边的 "
			str = str.substring(nameLeft+1);
			int nameRight =str.indexOf("\"");//typeName的右边的 "
			
			String typeName = str.substring(0, nameRight);
			
			Typer tp = new Typer();
			tp.setTypeName(typeName);
			
			List<Integer> li = new ArrayList<Integer>();
			Map<Object,Object> map = new HashMap<Object,Object>();
			for (String dataType : DATATYPE) {
				int index = str.indexOf(dataType);
//				String var = str.substring(varIndex);
			}
				
			
			
		}
		
		
		return null;
	}


	public static String readFile(String filePath){
		StringBuffer sb = new StringBuffer();
		try {
			if(!"".equals(filePath) && filePath != null){
				File file = new File(filePath);
				FileInputStream fs = new FileInputStream(file);
				
				//        byte[] bt = new byte[128];w
				int content = fs.read();
				
				while(content != -1){
					sb.append((char)content);
					content = fs.read();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}
