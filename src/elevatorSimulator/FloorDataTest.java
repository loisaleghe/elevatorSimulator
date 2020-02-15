/**
 * 
 */
package elevatorSimulator;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author abdul-rahmaanrufai, Ediomoabasi Emah
 *
 */
public class FloorDataTest {

	private BufferedReader br;
	private FloorData fd;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		br = new BufferedReader(new FileReader("floorRequestTest.txt"));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		br = null;
		fd = null;
	}

	/*
	 * Tests that the floorData read from the file is not null 
	 */
	@Test
	public void testFloorRequestTxtIsNotNull() throws IOException {
		String f; 
		f = br.readLine();
		fd = new FloorData(FloorData.parseString(f));
		assertNotNull(fd);	
	}

}
