package cn.edu.scau.cmi.hombio.layout;

import java.net.URL;

public class LayoutURL {
	public static URL get(String layoutName) {
		return LayoutURL.class.getResource(layoutName);
	}
}
