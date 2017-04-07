package com.capitalcode.assetsystemmobile.model;

/*
 * 资产盘点主model
 */
public class MsStockModel {
	
	public String BatchId;
	public String BatchMemo;	//批次说明
	public String BatchNumer;	//批次编号
	public String BatchState;	//批次状态
	public String StockTimeStart;	//开始时间
	@Override
	public String toString() {
		return "MsStockModel [BatchId=" + BatchId + ", BatchMemo=" + BatchMemo
				+ ", BatchNumer=" + BatchNumer + ", BatchState=" + BatchState
				+ ", StockTimeStart=" + StockTimeStart + "]";
	}
}
