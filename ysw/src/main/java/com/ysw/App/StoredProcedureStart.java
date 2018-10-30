package com.ysw.App;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.ysw.FileFactory.FileFactory;
import com.ysw.Util.TypeFilter;

public class StoredProcedureStart {
	
	public static final String PKDIRPATH = "D:\\pck\\test";
	public static final String[] PEDURE = {"PROCEDURE","FUNCTION"};
	
	public static void main(String[] args) {
		
		File file = new File(PKDIRPATH);
		if(file.exists()){
			file.mkdirs();
		}
		
		TypeFilter tf = new TypeFilter(".bdy");//bdy 存储过程body文件,源文件内包含逻辑
		
		String[] pk_array = file.list(tf);
		System.out.println("pk数量: "+pk_array.length);
		pck(pk_array);
		
	}
	
	
	public static void pck(String[] pk_array){
		if(pk_array.length > 0){
			for (int i = 0; i < pk_array.length; i++) {
				String pkPath = PKDIRPATH+"\\"+pk_array[i];
				
				String pkStr = FileFactory.getFileStr(pkPath);
				
				Map<String,String> funcHead = new HashMap<String,String>();
				String head = "CREATE OR REPLACE PACKAGE BODY \""+pk_array[i].toUpperCase()+"\" IS";
				while(true){
					int beginIndex = pkStr.indexOf(PEDURE[i]);
					if(beginIndex != -1){
						
						int isIndex = pkStr.indexOf("IS", beginIndex);
						
						if(isIndex != -1){
							//获取function或者procedure 的is之前的函数头
							String temporary = pkStr.substring(beginIndex, isIndex+2);
							
							int leftIndex = temporary.indexOf("(");
							int rightIndex = temporary.indexOf(")", leftIndex);
							String funcName = temporary.substring(9, leftIndex).trim();//函数名
							funcHead.put(funcName, temporary.trim());
							
							String params = temporary.substring(leftIndex+1, rightIndex);
							
							
							
							System.out.println(temporary);
							
							pkStr = pkStr.substring(isIndex+2);
							
							System.out.println(pkStr);
						}
						
					}else{
						break;
					}
				}
			}
		}
	}
	
	
}
