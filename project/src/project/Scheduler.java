package project;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
public class Scheduler {

	private Queue<Program> readyQueue;
	private List<String> ganttChart;
	private int clockCycle; 

	public Scheduler() {
		readyQueue = new LinkedList<>();
		ganttChart = new ArrayList<>();
		clockCycle = 0;
	}

	public void executeProgram(Program program, Memory memory, String schedulingAlgorithm) {
		if ("SJF".equals(schedulingAlgorithm)) {
			executeSJF(program, memory);
		} else if ("RoundRobin".equals(schedulingAlgorithm)) {
			if (program instanceof List) {
				executeRoundRobin((List<Program>) program, memory);
			} else {
				List<Program> singleProgramList = Collections.singletonList(program);
				executeRoundRobin(singleProgramList, memory);
			}
		} else {
			throw new IllegalArgumentException("Invalid scheduling algorithm: " + schedulingAlgorithm);
		}
	}

	private void executeSJF(Program program, Memory memory) {
		List<Program> programs = Collections.singletonList(program);
		executeSJF(programs, memory);
	}

	void executeSJF(List<Program> programs, Memory memory) {
		// saev number of instruction leh kol program
		Map<Program, Integer> programInstructionsMap = new HashMap<>();

		for (Program p : programs) {
			programInstructionsMap.put(p, p.getNumberOfInstructions());
		}

		//sort ascendingly
		List<Map.Entry<Program, Integer>> sortedPrograms = programInstructionsMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());

		for (Map.Entry<Program, Integer> entry : sortedPrograms) {
			Program sortedProgram = entry.getKey();
			readyQueue.offer(sortedProgram);
		}
		while (!readyQueue.isEmpty()) {
			Program currentProgram = readyQueue.poll();
			ganttChart.add(currentProgram.getName());

			List<String> instructions = currentProgram.getInstructions();
			System.out.println(currentProgram.getName() + " is executing");
			for (String instruction : instructions) {
				executeInstruction(instruction, memory);
				displayStatus(currentProgram.getName(), memory);
			}
			System.out.println("----------------------------  " + currentProgram.getName() + "  ended");
		}
	}

	void executeRoundRobin(List<Program> programs, Memory memory) {
		int quantum = 2;

		readyQueue.addAll(programs);

		int totalInstructions = programs.stream().mapToInt(program -> program.getInstructions().size()).sum();

		int totalClockCycles = totalInstructions / quantum + (totalInstructions % quantum == 0 ? 0 : 1);

		for (int clockCycle = 0; clockCycle < totalClockCycles; clockCycle++) {
			for (Iterator<Program> iterator = readyQueue.iterator(); iterator.hasNext();) {
				Program currentProgram = iterator.next();
				ganttChart.add(currentProgram.getName());

				List<String> instructions = currentProgram.getInstructions();
				System.out.println(currentProgram.getName() + " is executing");
				

				for (int i = 0; i < quantum && (clockCycle * quantum + i) < instructions.size(); i++) {
					executeInstruction(instructions.get(clockCycle * quantum + i), memory);
				}

				displayStatus(currentProgram.getName(), memory);

				if (instructions.size() <= (clockCycle + 1) * quantum) {
					System.out.println("Program " + currentProgram.getName() + " completed.");
					System.out.println("---------------------");
					iterator.remove();
				}
				 if(readyQueue.isEmpty()) {
	                	
	                	System.out.println("Queue is now Empty " + readyQueue);
	                	
	                }

			}
		}
	}

	private void executeProgram(Program program, Memory memory) {
		List<String> instructions = program.getInstructions();

		for (String instruction : instructions) {
			executeInstruction(instruction, memory);
			displayStatus(program.getName(), memory);
		}
	}

	private void executeInstruction(String instruction, Memory memory) {
		String[] parts = instruction.split("\\s+");

		if (parts.length == 0) {
			return;
		}

		if (parts.length < 2) {
			return;
		}

		String variable = parts[1];
		String operation = parts[0];

		switch (operation) {
		case "assign":
			if (parts.length == 3 && "input".equals(parts[2])) {
				System.out.print("Enter the value for " + variable + ": ");
				int value;
				Scanner scanner = new Scanner(System.in);
				try {
					value = scanner.nextInt();
				} catch (Exception e) {
					System.err.println("Error: Invalid input. Please enter an integer.");
					scanner.nextLine(); 
					return;
				}
				memory.assign(variable, value);
			} else if (parts.length == 5) {
				performArithmeticOperation(parts, memory, variable);
			} else if (parts.length == 4 && "readFile".equals(parts[2])) {
				// Example: assign b readFile a
				String variableToRead = parts[3];
				int valueToAssign = memory.get(variableToRead);
				memory.assign(variable, valueToAssign);
			} else {
				throw new IllegalArgumentException("Invalid 'assign' operation - " + instruction);
			}
			break;
		case "print":
			int value = memory.get(variable);
			System.out.println(variable + " = " + value);
			break;
		case "writeFile":
			if (parts.length == 3) {
				String variableToWrite = parts[1];
				String fileName = parts[2];
				writeFile(variableToWrite, fileName, memory);
			} else {
				throw new IllegalArgumentException("Invalid 'writeFile' operation - " + instruction);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown operation - " + operation);
		}
		clockCycle++;
		System.out.println("Clock Cycle: " + clockCycle);
	}

	private void performArithmeticOperation(String[] parts, Memory memory, String variable) {
		int a = memory.get(parts[3]);
		int b = memory.get(parts[4]);
		int result = 0;

		switch (parts[2]) {
		case "add":
			result = a + b;
			break;
		case "multiply":
			result = a * b;
			break;
		case "divide":
			if (b != 0) {
				result = a / b;
			} else {
				System.err.println("Error: Division by zero");
				return;
			}
			break;
		case "subtract":
			result = a - b;
			break;
		default:
			throw new IllegalArgumentException("Unknown arithmetic operation - " + parts[2]);
		}

		memory.assign(variable, result);
	}

	private void displayStatus(String currentProgramName, Memory memory) {
		System.out.println("Ready Queue: " + readyQueue);

		System.out.print("Memory at clock cycle " + clockCycle + ": ");
		memory.printMemory();
		ganttChart.add(currentProgramName);
	}

	private void writeFile(String variableName, String fileName, Memory memory) {
		int valueToWrite = memory.get(variableName);
		memory.assign(fileName, valueToWrite);
	}
}