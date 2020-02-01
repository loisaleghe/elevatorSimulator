/**
 * 
 */
package elevatorSimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author djani
 *
 */
class SchedulerTest extends Scheduler {
	
	private Scheduler scheduler;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		this.scheduler = new Scheduler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		this.scheduler = null;
	}

	@Test
	void testSendAndFetchData() {
		FloorData fd = new FloorData(new Time(System.currentTimeMillis()),5, Direction.UP, 8);
		this.scheduler.sendData(fd);
		FloorData newFloorData = this.scheduler.getData();
		
		assertEquals(fd, newFloorData, "Floor data must be equal");
		fail("Not yet implemented");
	}

}
