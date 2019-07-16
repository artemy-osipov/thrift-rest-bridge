package ru.osipov.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import ru.osipov.thrift.bridge.config.BridgeAutoConfiguration

@Import(BridgeAutoConfiguration)
@SpringBootApplication
class TestApplication {

	static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args)
	}
}
