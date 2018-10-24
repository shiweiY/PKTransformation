package com.ysw.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ysw.model.Typer;

public class App 
{
	public static final List<String> DATATYPE = new ArrayList<String>(Arrays.asList(" varchar",
																		" number"," date"," integer"," smallint"));
	
	public static void main( String[] args ) throws Exception
	{
		String fileDir = "D:\\pk_temp\\type\\test";
		File getFile = new File(fileDir);
		String[] strFileList = getFile.list();

		String filePath = fileDir.toString()+"\\"+strFileList[0].toString();

		String str = readFile(filePath);
		Typer tp = getTyper(str);
		System.out.println(str);
	}

	public static Typer getTyper(String str){
		Map<Integer,String> map = new HashMap<Integer,String>();
		Typer tp = new Typer();
		if(!"".equals(str) && str != null){
			str = str.toLowerCase();//转换小写
//			str = str.toUpperCase();//转换大写
			
			System.out.println(str);
			System.err.println("----------------------------------");
			
			int nameLeft = str.indexOf("\"");//typeName的左边的 "
			str = str.substring(nameLeft+1);
			int nameRight =str.indexOf("\"");//typeName的右边的 "
			
			String typeName = str.substring(0, nameRight);
			tp.setTypeName(typeName);
			
//			List<Integer> li = new ArrayList<Integer>();
			
			int firIndex = 0;
			for (String dataType : DATATYPE) {
				while(true) {
					Integer idx = str.indexOf(dataType,firIndex);
//					System.out.println(idx);
					if(idx != -1) {
						map.put(idx, dataType.trim());
					}else {
						break;
					}
					firIndex = idx + 1;
				}
				firIndex = 0;
			}
			
			List<Integer> listIndex = new ArrayList<Integer>();
			for(Map.Entry<Integer, String> entry : map.entrySet()) {
				listIndex.add(entry.getKey());
			}
			Collections.sort(listIndex);
			
			List<String> attributeList = new ArrayList<String>();
			for(Integer attributeIndex : listIndex) {
				attributeList.add(map.get(attributeIndex));
			}
			
			tp.setTypeAttribute(attributeList);
			
			
		}else {
			return null;
		}
		
		
		return tp;
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
