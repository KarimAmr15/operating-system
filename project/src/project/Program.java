package project;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Program {
	private List<String> instructions;
	private int numberOfInstructions;
	private String path;
	private String name;

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public Program(String name, String filePath) throws IOException {
		instructions = readProgramFromFile(filePath);
		numberOfInstructions = instructions.size();
		path = filePath;
		this.name = name;
	}

	public List<String> getInstructions() {
		return instructions;
	}

	public int getNumberOfInstructions() {
		return numberOfInstructions;
	}

	public String getPath() {
		return path;
	}

	private List<String> readProgramFromFile(String filePath) throws IOException {
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		}
		return lines;
	}
}