package com.ysw.Util;

import java.io.File;
import java.io.FilenameFilter;

public class TypeFilter implements FilenameFilter {
	private String typePattern;
	
	public String getTypeStartName() {
		return typePattern;
	}
	public void setTypeStartName(String typeStartName) {
		this.typePattern = typeStartName;
	}

	public TypeFilter(String typeStartName) {
		this.typePattern = typeStartName;
	}

	//过滤条件
	public boolean accept(File dir, String name) {
		if(name.startsWith(typePattern)){
			return name.startsWith(typePattern);
		}
		return name.endsWith(typePattern);
	}

}
