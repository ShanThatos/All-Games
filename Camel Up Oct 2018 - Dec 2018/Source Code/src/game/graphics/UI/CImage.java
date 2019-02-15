package game.graphics.UI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class CImage {
	
	private static Path2D[] imgData;
	private static String[] imgNames;
	
	private int id;
	
	public CImage(String imgName) {
		if (imgData == null) {
			try { readFiles(this); } 
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Couldn't read core game files", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		id = -1;
		for (int i = 0; i < imgNames.length; i++)
			if (imgName.equals(imgNames[i]))
				id = i;
	}
	
	public static void readFiles(CImage ci) throws IOException {
		Scanner in = new Scanner(ci.getClass().getClassLoader().getResourceAsStream("files/image_data/allImageData.dat"));
		ArrayList<String> allImgNames = new ArrayList<>();
		ArrayList<Path2D> allImgData = new ArrayList<>();
		
		while (in.hasNext()) 
			allImgNames.add(in.nextLine().trim());
		in.close();
		
		for (int i = 0; i < allImgNames.size(); i++) {
			Path2D path = new Path2D.Double();
			in = new Scanner(ci.getClass().getClassLoader().getResourceAsStream("files/image_data/" + allImgNames.get(i) + ".dat"));
			while (in.hasNextDouble()) {
				double x = in.nextDouble(), y = in.nextDouble();
				if (path.getCurrentPoint() == null)
					path.moveTo(x, y);
				else
					path.lineTo(x, y);
			}
			path.closePath();
			allImgData.add(path);
		}
		
		
		imgData = new Path2D[allImgData.size()];
		imgNames = new String[allImgNames.size()];
		for (int i = 0; i < allImgNames.size(); i++) {
			imgData[i] = allImgData.get(i);
			imgNames[i] = allImgNames.get(i);
		}
	}
	
	public static void drawImage(Graphics2D gg, String imgName, int x, int y, double rot, int size, Color c, boolean flip) {
		if (imgData == null) {
			try { readFiles((new CImage(""))); } 
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Couldn't read core game files", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		
		int id = -1;
		for (int i = 0; i < imgNames.length; i++)
			if (imgName.equals(imgNames[i]))
				id = i;
		if (id == -1)
			return;
		
		Shape path = imgData[id].createTransformedShape(AffineTransform.getScaleInstance(size * (flip ? -1 : 1), size));
		Graphics2D g = (Graphics2D) gg.create();
		g.translate(x, y);
		g.rotate(rot);
		g.setColor(c);
		g.fill(path);
		g.setColor(Color.black);
		g.draw(path);
	}
}
