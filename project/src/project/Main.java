package project;

import java.io.IOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Main {
	public static void main(String[] args) {
		List<String> programPaths = new ArrayList<>();
		programPaths.add("C:/Users/Karim/Documents/GIU/projects/operating system project/Program_1.txt");
		programPaths.add("C:/Users/Karim/Documents/GIU/projects/operating system project/Program_2.txt");
		programPaths.add("C:/Users/Karim/Documents/GIU/projects/operating system project/Program_3.txt");

		Scanner scanner = new Scanner(System.in);

		System.out.println("Choose a scheduling algorithm:");
		System.out.println("1. Shortest Job First (SJF)");
		System.out.println("2. Round Robin");
		System.out.print("Enter the number of your choice: ");

		int choice = scanner.nextInt();

		String schedulingAlgorithm;
		switch (choice) {
		case 1:
			schedulingAlgorithm = "SJF";
			break;
		case 2:
			schedulingAlgorithm = "RoundRobin";
			break;
		default:
			System.err.println("Invalid choice. Exiting...");
			return;
		}

		Memory memory = new Memory();
		Scheduler scheduler = new Scheduler();

		if ("SJF".equals(schedulingAlgorithm)) {
			List<Program> programs = new ArrayList<>();
			for (String programPath : programPaths) {
				try {
					Program program = new Program("Program_" + getProgramNumber(programPath), programPath);
					programs.add(program);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			scheduler.executeSJF(programs, memory);
		} else if ("RoundRobin".equals(schedulingAlgorithm)) {
			List<Program> programs = new ArrayList<>();

			for (String programPath : programPaths) {
				try {
					Program program = new Program("Program_" + getProgramNumber(programPath), programPath);
					programs.add(program);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			scheduler.executeRoundRobin(programs, memory);

			for (Program program : programs) {
				System.out.println("Program " + program.getName() + " completed.");
				System.out.println("---------------------");
			}

		}

		scanner.close();
	}

	private static String getProgramNumber(String programPath) {
		File file = new File(programPath);
		String fileName = file.getName();
		return fileName.replaceAll("[^0-9]", "");
	}
}