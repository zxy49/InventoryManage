package team.androidinventory.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="outputList")
public class OutputList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int outputID;
	private int workerID;
	private Date outputTime;
	
	public OutputList(){
		super();
	}






	public int getOutputID() {
		return outputID;
	}



	public void setOutputID(int outputID) {
		this.outputID = outputID;
	}






	public int getWorkerID() {
		return workerID;
	}






	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}






	public Date getOutputTime() {
		return outputTime;
	}



	public void setOutputTime(Date outputTime) {
		this.outputTime = outputTime;
	}




}
