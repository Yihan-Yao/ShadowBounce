import bagel.Image;

/**
 * The Peg class, defines a single peg.
 */
public class Peg extends Sprite{
	private PegType pegType;
	
	public Peg(Image image, double x, double y, PegType pegType) {
		super(image, x, y);
		this.pegType = pegType;
	}
	
	public PegType getType() {
		return pegType;
	}
	
	public void setType(PegType pegType) {
		this.pegType = pegType;
	}
	
}
