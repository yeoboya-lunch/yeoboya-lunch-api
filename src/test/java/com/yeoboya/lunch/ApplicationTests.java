package com.yeoboya.lunch;

import com.yeoboya.lunch.api.container.ContainerDI;
import com.yeoboya.lunch.config.util.Helper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ApplicationTests extends ContainerDI {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		if (applicationContext != null) {
			Helper.printBeanNames(applicationContext);
		}
	}

}
