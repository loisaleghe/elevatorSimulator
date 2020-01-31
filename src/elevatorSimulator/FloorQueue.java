/**
 * 
 */
package elevatorSimulator;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author djani
 *
 */
public class FloorQueue<Floor> extends AbstractQueue<Floor> {
	
	private ArrayList<Floor> floors;

	/**
	 * 
	 */
	public FloorQueue() {
		this.floors = new ArrayList<Floor>();
	}
	
	public void sort(boolean asc) {
		if(asc) {
			Collections.sort(this.floors, new Comparator<Floor>(){
				@Override
			    public int compare(Floor f1, Floor f2) {
					Floor fl = new Floor();
					return 1;
			        //return f1.getFLoorNumber().compareTo(f2.getFloorNumber());
			    }
			});
		}
	}

	@Override
	public boolean offer(Floor e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Floor poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Floor peek() {
		// TODO Auto-generated method stub
		return this.floors.get(0);
	}

	@Override
	public Iterator<Floor> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.floors.size();
	}

}
