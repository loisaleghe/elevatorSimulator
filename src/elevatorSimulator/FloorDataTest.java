/**
 * 
 */
package elevatorSimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author abdul-rahmaanrufai
 *
 */
class FloorDataTest {
	
	private BufferedReader br;
	private FloorData fd;


	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		br = new BufferedReader(new FileReader("floorRequestTest.txt"));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testReadFile() throws IOException {
		String f; 
		f = br.readLine();
		fd = new FloorData(FloorData.parseString(f));
		assertNotNull(fd);	
	}
}
