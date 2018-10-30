package com.ysw.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ysw.FileFactory.FileFactory;
import com.ysw.Util.TypeFilter;
import com.ysw.model.Typer;


/***
 * 
 * @author ysw
 *
 *	对oracle type进行参数读取 生成假数据并输出
 *  
 *
 */
public class TypeDB 
{
	public static final String READFILEDIR = "D:\\pck\\test";
	public static final String OUTFILEDIR = "D:\\pck\\TypeDB";
	public static File FILEPATH = new File(OUTFILEDIR);
	public static final List<String> DATATYPE = new ArrayList<String>(Arrays.asList(" varchar",
			" number"," date"," integer"," smallint"));

	/***
	 * 初始化读取/输出 文件路径
	 */
	static {
		if(FILEPATH.exists())
			FILEPATH.mkdirs();

		FILEPATH = new File(READFILEDIR);
	}

	public static void main( String[] args ) throws Exception
	{

		//过滤关键字
		String type_head_1 = "rec_";
		String type_head_2 = "ob_";
		String type_head_3 = "nt_";

		TypeFilter filterRec = new TypeFilter(type_head_1);//rec_文件过滤规则
		TypeFilter filterOb = new TypeFilter(type_head_2);//ob_文件过滤规则
		TypeFilter filterNt = new TypeFilter(type_head_3);//ob_文件过滤规则

		String[] list_1 = FILEPATH.list(filterRec);
		String[] list_2 = FILEPATH.list(filterOb);
		String[] list_3 = FILEPATH.list(filterNt);

		Map<String,String[]> mapList = new HashMap<String,String[]>();
		//		mapList.put(type_head_1, list_1);
		//		mapList.put(type_head_2, list_2);
		mapList.put(type_head_3, list_3);

		long startTime=System.currentTimeMillis();//开始工作时间

		typeStart(mapList);

		long endTime=System.currentTimeMillis(); 
		System.out.println("type_DB生成时间: "+(endTime-startTime)/1000+"s");
	}


	/**
	 * type 遍历转换
	 * @param fileDir
	 * @param recList
	 * @throws Exception
	 */
	public static void typeStart(Map<String,String[]> mapList) throws Exception {

		if(mapList != null) {
			for(Map.Entry<String,String[]> entry : mapList.entrySet()) {
				if(entry.getKey() != "nt"){


					File file = new File(OUTFILEDIR+"\\"+entry.getKey().toUpperCase()+"DB.json");

					OutputStream os = new FileOutputStream(file,true);
					byte[] left = "{".getBytes();//json文件的左右括弧
					byte[] right = "}".getBytes();

					os.write(left);
					os.write("\r\n".getBytes());

					for (String type : entry.getValue()) {

						String filePath = READFILEDIR+"\\"+type;

						String typeStr = FileFactory.getFileStrLowerCase(filePath);//获取type 小写文本
						Typer tp = getTyper(typeStr);//文本转换为model

						//FileFactory.writerFile(tp);//输出为文本
						FileFactory.writeJson(tp,file);//输出为json
					}
					os.write(right);
					os.close();

				}
			}
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


}
