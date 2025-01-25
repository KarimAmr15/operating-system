package project;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Memory {
	public Map<String, Integer> memory;

	public Memory() {
		this.memory = new HashMap<>();
	}

	public void assign(String variable, int value) {
		memory.put(variable, value);
	}

	public int get(String variable) {
		return memory.getOrDefault(variable, 0);
	}

	public void printMemory() {
		System.out.print("Memory: ");
		for (Map.Entry<String, Integer> entry : memory.entrySet()) {
			System.out.print(entry.getKey() + " = " + entry.getValue() + ", ");
		}
		System.out.println();
	}
}