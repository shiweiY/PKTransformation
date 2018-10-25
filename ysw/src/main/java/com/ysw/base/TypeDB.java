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
import com.ysw.Util.TypeFilter;
import com.ysw.model.Typer;

/***
 * 
 * @author ysw
 *
 *	对oracle type进行参数读取 生成假数据并输出
 *
 */
public class TypeDB 
{

	public static final List<String> DATATYPE = new ArrayList<String>(Arrays.asList(" varchar",
			" number"," date"," integer"," smallint"));

	public static void main( String[] args ) throws Exception
	{

		String fileDir = "D:\\pck\\type";
		File getFile = new File(fileDir);
		if(getFile.exists()){
			getFile.mkdirs();
		}

		TypeFilter filterRec = new TypeFilter("rec_");//rec_文件过滤规则
//		TypeFilter filterNt = new TypeFilter("nt_rec_");//nt文件过滤规则

		String[] recList = getFile.list(filterRec);//过滤选择的rec文件名列表
//		String[] ntList = getFile.list(filterNt);//过滤选择的nt文件名列表
		System.out.println("rec文件数量:"+recList.length);
		System.out.println("nt文件数量:"+recList.length);

		long startTime=System.currentTimeMillis();//开始工作时间

		recStart(fileDir,recList);

//		ntStart("1","2");

		long endTime=System.currentTimeMillis(); 
		System.out.println(recList.length+"个文件运行了: "+(endTime-startTime)/1000+"s");
	}

	/**
	 * type 遍历转换
	 * @param fileDir
	 * @param recList
	 * @throws Exception
	 */
	public static void recStart(String fileDir,String[] recList) throws Exception {

		if(!fileDir.isEmpty() && !recList.toString().isEmpty()) {
			for (int i = 0; i < recList.length ; i++) {

				String filePath = fileDir.toString()+"\\"+recList[i].toString();

				String typeStr = readFile(filePath);//获取type 文本
				Typer tp = getTyper(typeStr);//文本转换为model

				writerFile(tp);//输出转换
			}
		}

	}

	/***
	 * nt生成   提供给生成PROCEDURE或者FUNCTION时调用,以获取nt假数据
	 * @param returnStyle &nbsp; nt在存储过程的返回方式，return返回或者out出参返回
	 * @param ntName &nbsp; nt 的名字
	 * @throws Exception
	 * @return String
	 */
	public static String ntStart(String returnStyle,String ntName) throws Exception {
		if(!ntName.isEmpty()) {
			StringBuffer ntTypeStr = new StringBuffer();
			String recTypeDBPath = "D:\\pk_temp\\typeTFM\\typeDB.txt";//转换后type假数据的储存位置
			String recTypeDBStr = readFile(recTypeDBPath);//读取为字符串

			String recName = ntName.substring(ntName.indexOf("rec_"),ntName.length());//获取子type的名字

			int recIndex = recTypeDBStr.indexOf(recName);//rec type的位置
			if(recIndex >= 0) {
				int newIndex = recTypeDBStr.indexOf("new",recIndex);//new 关键字的位置
				int endSymbol = recTypeDBStr.indexOf(";", newIndex);//结束的;的位置
				String recTF = recTypeDBStr.substring(newIndex, endSymbol+1);
				System.out.println(recTF);

			}
		}
		return "";
	}


	/**
	 * 输出文件
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

			int nameLeft = typeStr.indexOf("\"");//typeName的左边的 "
			typeStr = typeStr.substring(nameLeft+1);
			int nameRight =typeStr.indexOf("\"");//typeName的右边的 "

			String typeName = typeStr.substring(0, nameRight);
			tp.setTypeName(typeName);

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
		return sb.toString().toLowerCase();//返回时转小写
	}
}
