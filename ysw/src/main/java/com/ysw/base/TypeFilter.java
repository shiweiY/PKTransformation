package com.ysw.base;

import java.io.File;
import java.io.FilenameFilter;

public class TypeFilter implements FilenameFilter {
	private String typeStartName;
	
	public String getTypeStartName() {
		return typeStartName;
	}
	public void setTypeStartName(String typeStartName) {
		this.typeStartName = typeStartName;
	}

	public TypeFilter(String typeStartName) {
		this.typeStartName = typeStartName;
	}

	//过滤条件
	public boolean accept(File dir, String name) {
		return name.startsWith(typeStartName);
	}

}
