package elevatorSimulator;

import java.io.Serializable;

/**
 * @author 
 */
public class Floor implements Serializable {

	private Integer number;

	/**
	 * 
	 */
	public Floor(int number) {
		this.number = number;
	}
	
	public Floor(Floor fl) {
		this.number = fl.number;
		// TODO Auto-generated constructor stub
	}
	
	public Integer getNumber() {
		return this.number;
	}
	
	/*
	 * sets the number of the floor
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * Checks if this floor is equal to another given floor
	 * @param o, an Object, represents the floor being compared to this floor
	 * @return a boolean, true if this floor is the same as the given floor, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if(this == o)	
			return true;
		
		if(o == null)
			return false;
		
		if (this.getClass() != o.getClass())
	        return false;
		
		Floor f = (Floor) o;
		
		return this.number == f.number;
	}
	
	/**
	 * Gives a string representation of this floor
	 * @return a String, representing this floor
	 */
	public String toString() {
		return "floor: " + this.number;
	}

}
