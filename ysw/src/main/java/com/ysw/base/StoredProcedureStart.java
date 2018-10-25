package com.ysw.base;

import java.io.File;

import com.ysw.Util.TypeFilter;

public class StoredProcedureStart {
	public static void main(String[] args) {
		String pkDirPath = "D:\\pk_temp\\temp";
		
		File file = new File(pkDirPath);
		if(file.exists()){
			file.mkdirs();
		}
		
		TypeFilter tf = new TypeFilter(".bdy");//bdy 存储过程body文件,源文件内包含逻辑
		
		String[] pk_file = file.list(tf);
		System.out.println("pk数量: "+pk_file.length);
		
		for (int i = 0; i < pk_file.length; i++) {
			String pkPath = pkDirPath+"\\"+pk_file[i];
			
			String pkStr = TypeDB.readFile(pkPath);
			System.out.println(pkStr);
			pck(pkStr);
		}
		
	}
	
	
	public static void pck(String pkStr){
		if(!pkStr.isEmpty()){
			int cIndex = pkStr.indexOf("create");
			int isIndex = pkStr.indexOf("is")+2;
			
			String pkHead = pkStr.substring(cIndex, isIndex).toUpperCase();//存储过程头转大写
			System.out.println(pkHead);
		}
	}
	
	
}
