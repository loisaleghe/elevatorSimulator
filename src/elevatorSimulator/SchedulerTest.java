package elevatorSimulator;

import static org.junit.Assert.*;

import java.sql.Time;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SchedulerTest {

	private Scheduler scheduler;

	@Before
	public void setUp() throws Exception {
		this.scheduler = new Scheduler();
	}

	@After
	public void tearDown() throws Exception {
		this.scheduler = null;
	}

//	@Test
//	public void testSendAndFetchData() {
//		FloorData fd = new FloorData(new Time(System.currentTimeMillis()),5, Direction.UP, 8);
//		this.scheduler.sendData(fd);
//		FloorData newFloorData = this.scheduler.getData();
//		assertEquals("Floor data must be equal",fd, newFloorData);
//	}


}
