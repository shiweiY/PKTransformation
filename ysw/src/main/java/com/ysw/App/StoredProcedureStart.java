package com.ysw.App;
//package com.ysw.base;
//
//import java.io.File;
//
//import com.ysw.Util.TypeFilter;
//
//public class StoredProcedureStart {
//	
//	public static final String PKDIRPATH = "D:\\pck\\test";
//	public static final String[] PEDURE = {"PROCEDURE","FUNCTION"};
//	
//	public static void main(String[] args) {
//		
//		File file = new File(PKDIRPATH);
//		if(file.exists()){
//			file.mkdirs();
//		}
//		
//		TypeFilter tf = new TypeFilter(".bdy");//bdy 存储过程body文件,源文件内包含逻辑
//		
//		String[] pk_array = file.list(tf);
//		System.out.println("pk数量: "+pk_array.length);
//		pck(pk_array);
//		
//	}
//	
//	
//	public static void pck(String[] pk_array){
//		if(pk_array.length > 0){
//			for (int i = 0; i < pk_array.length; i++) {
//				String pkPath = PKDIRPATH+"\\"+pk_array[i];
//				
//				String pkStr = TypeDB.getFileStr(pkPath);
//				
//				while(true){
//					int beginIndex = pkStr.indexOf(PEDURE[i]);
//					if(beginIndex != -1){
//						
//						int isIndex = pkStr.indexOf("IS", beginIndex);
//						
//						if(isIndex != -1){
//							String temporary = pkStr.substring(beginIndex, isIndex);
//							
//							System.out.println(temporary);
//							
//							pkStr = pkStr.substring(isIndex+1);
//							
//							System.out.println(pkStr);
//						}
//						
//					}else{
//						break;
//					}
//				}
//			}
//		}
//	}
//	
//	
//}
