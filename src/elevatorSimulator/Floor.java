package elevatorSimulator;

public class Floor {

	private Integer number;

	/**
	 * 
	 */
	public Floor(int number) {
		this.number = number;
		// TODO Auto-generated constructor stub
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

}
