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
		/*
		Floor currFloor = new Floor(1);
		Direction currDirection = Direction.IDLE;
		*/
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	public void testAddFloorsToUpQueue() {
		elevator.setCurrentFloor(new Floor (2));
		elevator.addFloor(new Floor(4));
		elevator.addFloor(new Floor(5));
		elevator.addFloor(new Floor(3));
		assertTrue(elevator.getUpQueue().peek() == new Floor(3));
		
		
	}

}
