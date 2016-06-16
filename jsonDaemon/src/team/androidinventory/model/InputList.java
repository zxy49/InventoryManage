package team.androidinventory.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="inputList")
public class InputList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private int inputID;
	private int workerID;
	private Date inputTime;
	
	public InputList(){
		super();
	}

	public int getInputID() {
		return inputID;
	}



	public void setInputID(int inputID) {
		this.inputID = inputID;
	}



	public int getWorkerID() {
		return workerID;
	}



	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}



	public Date getInputTime() {
		return inputTime;
	}



	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}





}
