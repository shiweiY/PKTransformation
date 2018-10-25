package com.ysw.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ysw.Util.DateUtil;
import com.ysw.model.Typer;


public class App 
{

	public static final List<String> DATATYPE = new ArrayList<String>(Arrays.asList(" varchar",
			" number"," date"," integer"," smallint"));
	
	public static void main( String[] args ) throws Exception
	{
		
		String fileDir = "D:\\pk_temp\\type";
		File getFile = new File(fileDir);
		
		TypeFilter filter = new TypeFilter("rec_");
		
		
		String[] strFileList = getFile.list(filter);//过滤选择的文件名列表
		System.out.println("文件数量:"+strFileList.length);

		for (int i = 0; i < strFileList.length ; i++) {
			String filePath = fileDir.toString()+"\\"+strFileList[i].toString();

			String typeStr = readFile(filePath);
			Typer tp = getTyper(typeStr);

			writer(tp);
		}
		
	}


	/**
	 * 输出文件
	 * @param tp
	 * @throws Exception
	 */
	public static void writer(Typer tp) throws Exception {
		File file = new File("D:\\pk_temp\\typeTFM");
		if(!file.exists()){
			file.mkdirs();
		}
		file = new File(file.getPath()+"\\Rec.txt");
		
		try {
			if(tp != null){

				String typeName = tp.getTypeName();//type name
				List<String> attribute = tp.getTypeAttribute();//type 属性
				
				StringBuffer typeStr = new StringBuffer(typeName+" := new "+typeName+"("); 
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
				String str = typeStr.substring(0, typeStr.length()-1).concat(");");

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

	}




	/***
	 * 
	 * 解析文本获取model
	 * 
	 * @param typeStr
	 * @return Typer
	 */
	public static Typer getTyper(String typeStr){
		Map<Integer,String> map = new HashMap<Integer,String>();
		Typer tp = new Typer();
		if(!"".equals(typeStr) && typeStr != null){
			typeStr = typeStr.toLowerCase();//转换小写
			//			typeStr = typeStr.toUpperCase();//转换大写

//			System.out.println(typeStr);
//			System.err.println("----------------------------------");

			int nameLeft = typeStr.indexOf("\"");//typeName的左边的 "
			typeStr = typeStr.substring(nameLeft+1);
			int nameRight =typeStr.indexOf("\"");//typeName的右边的 "

			String typeName = typeStr.substring(0, nameRight);
			tp.setTypeName(typeName);

			//			List<Integer> li = new ArrayList<Integer>();

			int friIndex = 0;
			for (String dataType : DATATYPE) {
				while(true) {
					Integer idx = typeStr.indexOf(dataType,friIndex);
					if(idx != -1) {
						map.put(idx, dataType.trim());
					}else {
						break;
					}
					friIndex = idx + 1;
				}
				friIndex = 0;
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
			System.out.println(tp.getTypeName()+" 属性数量:"+tp.getTypeAttribute().size());

		}else {
			return null;
		}


		return tp;
	}


	/***
	 * 读取type 以String形式返回
	 * 
	 * @param filePath
	 * @return String
	 */
	public static String readFile(String filePath){
		StringBuffer sb = new StringBuffer();
		try {
			if(!"".equals(filePath) && filePath != null){
				File file = new File(filePath);
				FileInputStream fs = new FileInputStream(file);

				//        byte[] bt = new byte[128];
				int content = fs.read();

				while(content != -1){
					sb.append((char)content);
					content = fs.read();
				}
				
				fs.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}
