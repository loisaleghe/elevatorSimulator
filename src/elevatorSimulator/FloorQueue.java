/**
 * 
 */
package elevatorSimulator;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author djani
 *
 */
public class FloorQueue extends AbstractQueue<Floor> {
	
	private ArrayList<Floor> floors;

	/**
	 * 
	 */
	public FloorQueue() {
		this.floors = new ArrayList<Floor>();
	}
	
	public void sort(boolean asc) {
			this.floors.sort((floor1, floor2) -> asc ? floor1.getNumber().compareTo(floor2.getNumber()) : floor2.getNumber().compareTo(floor1.getNumber()));	
	}

	/**
	 * Adds a floor at the tail of the floor queue
	 */
	@Override
	public boolean offer(Floor fl) {
		if(!this.floors.contains(fl)) 
			return this.floors.add(fl);
		
		return false;
	}

	/**
	 * Removes the floor at the head of the floor queue
	 */
	@Override
	public Floor poll() {
		Floor fl = new Floor(this.floors.remove(0));
		return fl;
	}

	/**
	 * Fetches the floor at the head of the floor queue
	 */
	@Override
	public Floor peek() {
		return this.floors.get(0);
	}

	@Override
	public Iterator<Floor> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return this.floors.size();
	}
	
	public String toString() {
		return this.floors.toString();
	}

}
