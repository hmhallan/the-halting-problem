import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class TestHalt {
	
	@Test
	public void testa_problema_parada() throws Exception {
		java.io.Reader input = new FileReader("src/main/resources/parada.in");
		Main.start(input);
	}

}
