package com.ysw.FileFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.List;

import com.ysw.Util.DateUtil;
import com.ysw.model.Typer;

/***
 * 文本处理
 * @author ysw
 *
 */
public class FileFactory {


	/**
	 * 获取纯小写文本
	 * @param filePath
	 * @return
	 */
	public static String getFileStrLowerCase(String filePath){
		return readFile(filePath,0);
	}
	
	/**
	 * 获取纯大写文本
	 * @param filePath
	 * @return
	 */
	public static String getFileStrUpperCase(String filePath){
		return readFile(filePath,-1);
	}
	
	/**
	 * 获取纯源文件文本
	 * @param filePath
	 * @return
	 */
	public static String getFileStr(String filePath){
		return readFile(filePath,1);
	}
	
	
	/**
	 * 输出文件为json
	 * @param tp,file
	 * @throws Exception
	 */
	public static void writeJson(Typer tp,File file) throws Exception {

		try {
			if(tp != null){
				String str = "";

				String typeName = tp.getTypeName();//type name
				List<String> attribute = tp.getTypeAttribute();//type 属性

				StringBuffer typeStr = new StringBuffer(" \t\t \""+typeName+
											"\" : \""+"g_"+typeName+" := new "+typeName+"("); 

				for (int i = 0 ; i < attribute.size(); i++) {
					String attr = attribute.get(i);
					if(attr != null && !attr.isEmpty()){
						switch(attr){
						case "varchar":
							typeStr.append("'"+i+"',");
							break;
						case "number":
							typeStr.append(i+",");
							break;
						case "integer":
							typeStr.append(i+",");
							break;
						case "date":
							typeStr.append("date'"+DateUtil.getIncreaseDate()+"',");
							break;
						case "smallint":
							typeStr.append(i+",");
							break;

						}

					}

				}
				str = typeStr.substring(0, typeStr.length()-1).concat(");\",");

				byte[] b = str.getBytes();

				OutputStream os = new FileOutputStream(file,true);
				os.write(b);
				os.write("\r\n".getBytes());

				os.close();


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 输出文件为文本   --2018/10/30 此方式暂时弃用，数据输出改为json格式
	 * @param tp
	 * @throws Exception
	 */
	public static String writerFile(Typer tp) throws Exception {
		File file = new File("D:\\pck\\TypeDB");
		String str = "";
		if(!file.exists()){
			file.mkdirs();
		}
		file = new File(file.getPath()+"\\typeDB.txt");

		try {
			if(tp != null){

				String typeName = tp.getTypeName();//type name
				List<String> attribute = tp.getTypeAttribute();//type 属性

				StringBuffer typeStr = new StringBuffer("g_"+typeName+" := new "+typeName+"("); 
				for (int i = 0 ; i < attribute.size(); i++) {
					String attr = attribute.get(i);
					if(attr != null && !attr.isEmpty()){
						switch(attr){
						case "varchar":
							typeStr.append("'"+i+"',");
							break;
						case "number":
							typeStr.append(i+",");
							break;
						case "integer":
							typeStr.append(i+",");
							break;
						case "date":
							typeStr.append("date'"+DateUtil.getIncreaseDate()+"',");
							break;
						case "smallint":
							typeStr.append(i+",");
							break;

						}

					}

				}
				str = typeStr.substring(0, typeStr.length()-1).concat(");");

				byte[] b = str.getBytes();

				OutputStream os = new FileOutputStream(file,true);
				os.write(b);
				os.write("\r\n".getBytes());
				os.write("\r\n".getBytes());

				os.close();


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;

	}
	
	
	
	
	
	
	
	/***
	 * 读取type 以String形式返回,参数i为0返回小写,-1为大写,否则为原文件
	 * 
	 * @param filePath i
	 * @return String
	 */
	public static String readFile(String filePath,int i){
		StringBuffer sb = new StringBuffer();
		try {
			if(!"".equals(filePath) && filePath != null){
				File file = new File(filePath);

				//字符流按行读取文件
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);

				while(true) {
					String str = br.readLine();
					if(str != null) {
						sb.append(str);
					}else {
						break;
					}
				}

				br.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(i == 0){
			return sb.toString().toLowerCase();//返回时转小写
		}else if(i == -1){
			return sb.toString().toUpperCase();//返回时转大写写
		}else{
			return sb.toString();//返回源文件字符
		}

	}
}
