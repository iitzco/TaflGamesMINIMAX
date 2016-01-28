package view;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import board.Cell;

public class ImageManager {

	private Map<String, Image> images = new HashMap<String, Image>();

	public ImageManager() {
		initImages();
	}

	public void initImages() {
		try{		
			images.put("E", ImageUtils.loadImage("resources/Opponent.png"));
			images.put("C", ImageUtils.loadImage("resources/Castle.png"));
			images.put("G", ImageUtils.loadImage("resources/Guard.png"));
			images.put("K", ImageUtils.loadImage("resources/King2.png"));
			images.put("T", ImageUtils.loadImage("resources/throne.png"));
			images.put("CELL", ImageUtils.loadImage("resources/floor0.png"));
			images.put("select", ImageUtils.loadImage("resources/floor2.png"));
			images.put("DORADO", ImageUtils.loadImage("resources/baseselected.png"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public Image get(String base, String piece, Cell cell) {
		Image b;
		if (base=="C")
			b=ImageUtils.overlap(images.get("CELL"), images.get("C"));
		else
			b = images.get(base);

		if (!piece.equals("")) {
			Image combined;
			Image img = images.get(piece);
			if (cell.isSelected()) {
				b = images.get("DORADO");
			}
			combined = ImageUtils.overlap(b, img);

			return combined;
		}
		return b;

	}

}
