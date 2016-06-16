package team.androidinventory.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="stockList")
public class StockList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int stockListID;
	private int inputID;
	private String commodityID;
	private int houseID;
	private int providerID;
	private int time;
	private int totalNumber;
	
	public int getStockListID() {
		return stockListID;
	}
	public void setStockListID(int stockListID) {
		this.stockListID = stockListID;
	}
	public int getInputID() {
		return inputID;
	}
	public void setInputID(int inputID) {
		this.inputID = inputID;
	}
	public String getCommodityID() {
		return commodityID;
	}
	public void setCommodityID(String commodityID) {
		this.commodityID = commodityID;
	}
	public int gethouseID() {
		return houseID;
	}
	public void sethouseID(int houseID) {
		this.houseID = houseID;
	}
	public int getProviderID() {
		return providerID;
	}
	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

}