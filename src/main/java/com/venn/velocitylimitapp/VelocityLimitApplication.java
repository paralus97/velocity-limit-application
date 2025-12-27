package com.venn.velocitylimitapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VelocityLimitApplication {

	public static final String VENN_BACK_END_INPUT_PATH = "assets/Venn-Back-End-Input.txt";
	public static final String VENN_BACK_END_OUTPUT_PATH = "assets/Venn-Back-End-Output.txt";
	public static final String APPLICATION_OUTPUT_PATH = "output.txt";

	public static void main(String[] args) {
		SpringApplication.run(VelocityLimitApplication.class, args);
	}
}
