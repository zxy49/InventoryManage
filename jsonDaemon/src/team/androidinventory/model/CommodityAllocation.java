package team.androidinventory.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="commodityAllocation")
public class CommodityAllocation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int commodityAllocationID;
	private int workerID;
	private Date time;
	
	public CommodityAllocation(){
		super();
	}

	public int getCommodityAllocationID() {
		return commodityAllocationID;
	}

	public void setCommodityAllocationID(int commodityAllocationID) {
		this.commodityAllocationID = commodityAllocationID;
	}



	public int getWorkerID() {
		return workerID;
	}

	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	
}
