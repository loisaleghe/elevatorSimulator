/**
 * 
 */
package elevatorSimulatorTests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import elevatorSimulator.FloorData;

/**
 * @author abdul-rahmaanrufai
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

	@Test
	public void test() throws IOException {
		String f; 
		f = br.readLine();
		fd = new FloorData(FloorData.parseString(f));
		assertNotNull(fd);	
	}

}
