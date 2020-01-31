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
public class FloorQueue<Floor> extends AbstractQueue<Floor> {
	
	private ArrayList<Floor> floors = new ArrayList();

	/**
	 * 
	 */
	public FloorQueue() {
		// TODO Auto-generated constructor stub
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
		return null;
	}

	@Override
	public Iterator<Floor> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
