package com.iqmsoft.cb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.driver.core.utils.UUIDs;
import com.iqmsoft.cassandra.Vendor;
import com.iqmsoft.cassandra.VendorRepository;

@EnableAutoConfiguration
@EnableCassandraRepositories
@SpringBootApplication
public class Application implements CommandLineRunner {
	
	
	@Autowired
	private VendorRepository repository;

	
	@Override
	public void run(String... args) throws Exception {

		this.repository.deleteAll();

		
		this.repository.save(new Vendor(UUIDs.timeBased(), "Alice", "Smith"));
		this.repository.save(new Vendor(UUIDs.timeBased(), "Bob", "Smith"));

		System.out.println("Vendors found with findAll():");
		System.out.println("-------------------------------");
		for (Vendor customer : this.repository.findAll()) {
			System.out.println(customer);
		}
		
		System.out.println();

	}
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
