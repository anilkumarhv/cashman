package in.anil;

import in.anil.model.ATMBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 */
@SpringBootApplication
@ComponentScan(basePackages = {"in.anil"})
public class App {
    public static void main(final String[] args) {
        System.out.println("Hello World!");
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private ATMMeta atmMeta;

    @Bean
    public ATMBase getATMBase() {
        return ATMBase.getInstance(atmMeta);
    }
}
