package team.androidinventory.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="sellList")
public class SellList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int sellListID;
	private int houseID;
	private int outputID;
	private String commodityID;
	private int providerID;
	private int customerID;
	private Date time;
	private int totalNumber;
	
	public int getSellListID() {
		return sellListID;
	}
	public void setSellListID(int sellListID) {
		this.sellListID = sellListID;
	}
	public int gethouseID() {
		return houseID;
	}
	public void sethouseID(int houseID) {
		this.houseID = houseID;
	}
	public int getOutputID() {
		return outputID;
	}
	public void setOutputID(int outputID) {
		this.outputID = outputID;
	}
	public String getCommodityID() {
		return commodityID;
	}
	public void setCommodityID(String commodityID) {
		this.commodityID = commodityID;
	}
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	
	public int getProviderID() {
		return providerID;
	}
	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}

	
	
}