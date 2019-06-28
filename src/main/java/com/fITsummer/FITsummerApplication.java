package com.fITsummer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;

@SpringBootApplication
public class FITsummerApplication {


	public static void main(String[] args) {
		Database database = new Database();
		Database.userExists("jsgd");
		SpringApplication.run(FITsummerApplication.class, args);
	}
}
