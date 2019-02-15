package main;

import java.io.IOException;

import create.Tile;

public class MainRunner {
	
	public static void main(String[] args) throws IOException {
		readData();
		new MainFrame();
	}
	
	private static void readData() throws IOException {
		(new Tile()).read();
	}
}
