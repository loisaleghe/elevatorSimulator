package elevatorSimulator;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Ediomoabasi Emah
*/

class ElevatorTest {
	
	private Elevator elevator;

	@BeforeEach
	void setUp() throws Exception {
		elevator = new Elevator();
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	/*
	 * Checks floors are added correctly in the right 
	 * queue
	 */
	@Test
	public void testAddFloorsToUpQueue() {
		elevator.setCurrentFloor(new Floor (2));
		elevator.addFloor(new Floor(4));
		elevator.addFloor(new Floor(5));
		elevator.addFloor(new Floor(3));
		assertTrue((elevator.getUpQueue().peek()).equals(new Floor(3)));	
	}
	
	/*
	 * Checks floors are added correctly in the right 
	 * queue
	 */
	@Test
	public void testAddFloorsToDownQueue() {
		elevator.setCurrentFloor(new Floor (7));
		elevator.addFloor(new Floor (2));
		elevator.addFloor(new Floor (6));
		elevator.addFloor(new Floor (4));
		elevator.addFloor(new Floor (3));
		assertTrue((elevator.getDownQueue().peek()).equals(new Floor(6)));
	}
	
	/*
	 * Prints the size of the two queues
	 */
	@Test
	public void testSizeOfElevatorFloors() {
		elevator.setCurrentFloor(new Floor (5));
		elevator.addFloor(new Floor (2));
		elevator.addFloor(new Floor (7));
		elevator.addFloor(new Floor (9));
		elevator.addFloor(new Floor (4));
		System.out.println(elevator.getUpQueue().size());
		System.out.println(elevator.getDownQueue().size());
	}
	
	/*
	 * tests if the current floor of the elevator changes when the
	 * elevator moves
	 */
	@Test
	public void testMove() {
		elevator.setCurrentFloor(new Floor (3));
		elevator.addFloor(new Floor(7));
		elevator.setCurrentDirection(Direction.UP);
		elevator.move();
		assertTrue(elevator.getCurrentFloor().equals(new Floor (4)));
	}
	
	/*
	 * Checks if the elevator is empty after moving up to a
	 * destination
	 */
	@Test
	public void testMoveUpAFloor() {
		elevator.setCurrentFloor(new Floor (2));
		elevator.addFloor(new Floor (3));
		elevator.setCurrentDirection(Direction.UP);
		elevator.move();
		assertTrue((elevator.getUpQueue().isEmpty()));
	}
	
	/*
	 * Checks if the elevator is empty after moving down to a 
	 * destination
	 */
	@Test
	public void testMoveDownAFloor() {
		elevator.setCurrentFloor(new Floor (7));
		elevator.addFloor(new Floor (6));
		elevator.setCurrentDirection(Direction.DOWN);
		elevator.move();
		assertTrue((elevator.getDownQueue().isEmpty()));
	}
	
	/*
	 * Checks if the elevator's direction is DOWN when the 
	 * elevator has completed moving to a higher destination
	 */
	@Test
	public void testCurrentDirectionAfterElevatorGoingUp() {
		elevator.setCurrentFloor(new Floor (4));
		elevator.addFloor(new Floor (3));
		elevator.addFloor(new Floor (5));
		elevator.setCurrentDirection(Direction.UP);
		elevator.move();
		assertTrue((elevator.getCurrentDirection().equals(Direction.DOWN)));
		assertFalse((elevator.getCurrentDirection().equals(Direction.UP)));
	}
	
	/*
	 * Checks if the elevator's direction is UP when the elevator
	 * has completed moving to a lower destination
	 */
	@Test
	public void testCurrentDirectionAfterElevatorGoingDown() {
		elevator.setCurrentFloor(new Floor (4));
		elevator.addFloor(new Floor (3));
		elevator.addFloor(new Floor (5));
		elevator.setCurrentDirection(Direction.DOWN);
		elevator.move();
		assertTrue((elevator.getCurrentDirection().equals(Direction.UP)));
		assertFalse((elevator.getCurrentDirection().equals(Direction.DOWN)));
	}
	
	/*
	 * Tests to see if when an elevator moves up considering there are 
	 * multiple destinations, there are still more destinations for it to visit
	 */
	@Test
	public void testMoveUpMultipleDestinations() {
		elevator.setCurrentFloor(new Floor (3));
		elevator.addFloor(new Floor (4));
		elevator.addFloor(new Floor (6));
		elevator.addFloor(new Floor (7));
		elevator.setCurrentDirection(Direction.UP);
		elevator.move();
		assertTrue((elevator.getUpQueue().size() == 2));
	}
	
	/*
	 * Tests to see if when an elevator moves down considering there are 
	 * multiple destinations, there are still more destinations for it to visit
	 */
	@Test
	public void testMoveDownMultipleDestinations() {
		elevator.setCurrentFloor(new Floor (8));
		elevator.addFloor(new Floor (4));
		elevator.addFloor(new Floor (6));
		elevator.addFloor(new Floor (7));
		elevator.addFloor(new Floor (3));
		elevator.setCurrentDirection(Direction.DOWN);
		elevator.move();
		assertTrue((elevator.getDownQueue().size() == 3));
	}
	

}
