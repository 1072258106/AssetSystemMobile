package com.capitalcode.assetsystemmobile.utils;

public class StaticUtil {

	private static String spinner = "部门盘点";
	public static String People = "未设置";
	public static int flag = 1;
	public static int forPeopleCheckActivity = 1;
	public static int forAssetAddActivity = 1;
	public static int forRegisterActivity =1;
	public static String getSpinner() {
		return spinner;
	}

	public static void setSpinner(String spinner) {
		StaticUtil.spinner = spinner;
	}
}
