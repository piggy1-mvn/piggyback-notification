package com.incentives.piggyback.notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@SpringBootTest
public class NotificationApplicationTests {

	@Test
	@PrepareForTest(SpringApplication.class)
	public void contextLoads() {
		mockStatic(SpringApplication.class);
		NotificationApplication.main(new String[]{"Hello", "World"});
		verifyStatic(SpringApplication.class);
		SpringApplication.run(NotificationApplication.class, "Hello", "World");
	}

}