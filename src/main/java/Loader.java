import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import bagel.Image;
import bagel.Window;

/** static class for loading methods */

public class Loader {
	/** current lvl index */
    private static int curLvl = 0;
    
    /* peg images */
    private static Image pegImage = new Image("res/peg.png");
    private static Image vPegImage = new Image("res/vertical-peg.png");
    private static Image hPegImage = new Image("res/horizontal-peg.png");
    private static Image grayPegImage = new Image("res/grey-peg.png");
    private static Image vGrayPegImage = new Image("res/grey-vertical-peg.png");
    private static Image hGrayPegImage = new Image("res/grey-horizontal-peg.png");
    private static Image redPegImage = new Image("res/red-peg.png");
    private static Image vRedPegImage = new Image("res/red-vertical-peg.png");
    private static Image hRedPegImage = new Image("res/red-horizontal-peg.png");
    private static Image greenPegImage = new Image("res/green-peg.png");
    private static Image vGreenPegImage = new Image("res/green-vertical-peg.png");
    private static Image hGreenPegImage = new Image("res/green-horizontal-peg.png");
    
	/** list of levels to be loaded */
	private static String[] lvlList = new String[]{"res/0.csv"};
	
	/** random generator for red and green pegs */
    private static Random randomGenerator = new Random();

	/** creates a single peg according to pegType */
    private static Peg createPeg(PegType pegType, double x, double y) {
    	switch (pegType) {
    	case blue_peg:
    		return new Peg(pegImage, x, y, pegType);
    	case blue_vertical_peg:
    		return new Peg(vPegImage, x, y, pegType);
    	case blue_horizontal_peg:
    		return new Peg(hPegImage, x, y, pegType);
    	case grey_peg:
    		return new Peg(grayPegImage, x, y, pegType);
    	case grey_vertical_peg:
    		return new Peg(vGrayPegImage, x, y, pegType);
    	case grey_horizontal_peg:
    		return new Peg(hGrayPegImage, x, y, pegType);
    	case red_peg:
    		return new Peg(redPegImage, x, y, pegType);
    	case red_vertical_peg:
    		return new Peg(vRedPegImage, x, y, pegType);
    	case red_horizontal_peg:
    		return new Peg(hRedPegImage, x, y, pegType);
    	case green_peg:
    		return new Peg(greenPegImage, x, y, pegType);
    	case green_vertical_peg:
    		return new Peg(vGreenPegImage, x, y, pegType);
    	case green_horizontal_peg:
    		return new Peg(hGreenPegImage, x, y, pegType);
    	}
    	return null;
    }
    
    /** load next level, and if all boards are cleared, exit game */
    public static ArrayList<Peg> loadNextLvl() {
    	curLvl++;
    	if (curLvl >= lvlList.length) {
    		System.exit(0);
    	}
    	return loadPegs();
    }
    
    public static ArrayList<Peg> loadPegs() {
    	String filename = lvlList[curLvl];
        ArrayList<Peg> pegs = new ArrayList<>();
        
        // Read board file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        	String text = br.readLine();
        	while ((text = br.readLine()) != null) {
        		String[] columns = text.split(",");
        		PegType pegType;
        		double x, y;
        		pegType = PegType.valueOf(columns[0]);
        		x = Double.parseDouble(columns[1]);
        		y = Double.parseDouble(columns[2]);
        		pegs.add(createPeg(pegType, x, y));
        	}
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        // load red pegs
        int numOfRed = pegs.size() / 5;
        for (int i=0; i<numOfRed; i++) {
        	int index = randomGenerator.nextInt(pegs.size());
        	switch (pegs.get(index).getType()) {
        		case blue_peg:
        			pegs.get(index).setType(PegType.red_peg);
        			pegs.get(index).setImage(redPegImage);
        			break;
        		case blue_vertical_peg:
        			pegs.get(index).setType(PegType.red_vertical_peg);
        			pegs.get(index).setImage(vRedPegImage);
        			break;
        		case blue_horizontal_peg:
        			pegs.get(index).setType(PegType.red_horizontal_peg);
        			pegs.get(index).setImage(hRedPegImage);
        			break;
        		default:
        			i--;
        	}
        }
        
        // load green peg
        boolean greenLoaded = false;
        while (!greenLoaded) {
        	greenLoaded = true;
        	int index = randomGenerator.nextInt(pegs.size());
        	switch (pegs.get(index).getType()) {
        		case blue_peg:
        			pegs.get(index).setType(PegType.green_peg);
        			pegs.get(index).setImage(greenPegImage);
        			break;
        		case blue_vertical_peg:
        			pegs.get(index).setType(PegType.green_vertical_peg);
        			pegs.get(index).setImage(vGreenPegImage);
        			break;
        		case blue_horizontal_peg:
        			pegs.get(index).setType(PegType.green_horizontal_peg);
        			pegs.get(index).setImage(hGreenPegImage);
        			break;
        		default:
        			greenLoaded = false;
        	}
        }
        
        return pegs;
    }
    
}
