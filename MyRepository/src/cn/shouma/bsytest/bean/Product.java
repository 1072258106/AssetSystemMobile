package cn.shouma.bsytest.bean;

import java.io.Serializable;

public class Product implements Serializable {
	private String pName;
	private int pCode;
	private String pGZ;
	public Product() {
		super();
	}
	public Product(String pName, int pCode, String pGZ) {
		super();
		this.pName = pName;
		this.pCode = pCode;
		this.pGZ = pGZ;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public int getpCode() {
		return pCode;
	}
	public void setpCode(int pCode) {
		this.pCode = pCode;
	}
	public String getpGZ() {
		return pGZ;
	}
	public void setpGZ(String pGZ) {
		this.pGZ = pGZ;
	}
	@Override
	public String toString() {
		return "Product [pName=" + pName + ", pCode=" + pCode + ", pGZ=" + pGZ
				+ "]";
	}
	
}
