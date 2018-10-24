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

import com.ysw.model.Typer;

//
public class App 
{

	public static final List<String> DATATYPE = new ArrayList<String>(Arrays.asList(" varchar",
			" number"," date"," integer"," smallint"));

	public static void main( String[] args ) throws Exception
	{
		String filedir = "D:\\pk_temp\\type\\test";
		File getFile = new File(filedir);
		String[] strFileList = getFile.list();

		for (int i = 0; i < strFileList.length ; i++) {
			String filePath = filedir.toString()+"\\"+strFileList[i].toString();

			String typeStr = readFile(filePath);
			Typer tp = getTyper(typeStr);

			writer(tp);
		}
		
	}



	public static void writer(Typer tp) throws Exception {
		File file = new File("D:\\pk_temp\\type\\typeTFM\\Rec.txt");
		if(file.exists()){
			file.mkdir();
		}
		OutputStream os = new FileOutputStream(file,true);
		try {
			if(tp != null){

				String typeName = tp.getTypeName();
				System.out.println(typeName);
				List<String> attribute = tp.getTypeAttribute();
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
							typeStr.append("date'2018-10-24',");
							break;
						case "smallint":
							typeStr.append(i+",");
							break;

						}

					}

				}
				String str = typeStr.substring(0, typeStr.length()-1).concat(");");

				byte[] b = str.getBytes();

				os.write(b);
				os.write("\r\n".getBytes());
				os.write("\r\n".getBytes());
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			os.close();
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

			int firIndex = 0;
			for (String dataType : DATATYPE) {
				while(true) {
					Integer idx = typeStr.indexOf(dataType,firIndex);
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

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}
