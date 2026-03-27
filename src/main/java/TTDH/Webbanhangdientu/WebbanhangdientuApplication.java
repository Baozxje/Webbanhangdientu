package TTDH.Webbanhangdientu;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebbanhangdientuApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebbanhangdientuApplication.class, args);
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://lamgiabao73_db_user:5F7uk6t8HoGZD0aD@wdt.n2jlejk.mongodb.net/webbanhang?retryWrites=true&w=majority");
    }
}