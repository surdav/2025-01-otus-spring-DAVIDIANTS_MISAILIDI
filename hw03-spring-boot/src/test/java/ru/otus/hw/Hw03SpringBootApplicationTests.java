package ru.otus.hw;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Activates the "test" profile during tests
class Hw03SpringBootApplicationTests {

	@Test
	void contextLoads() {
		// Verify that the context loads successfully by asserting true.
		assert true;
	}

}
