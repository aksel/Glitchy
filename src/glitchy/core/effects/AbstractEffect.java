package glitchy.core.effects;

import java.io.Serializable;

import glitchy.core.imageProcessing.PixelStream;

/**
 * Abstract class that defines Effects used by EffectWorker.
 * @author Rasmus and Aksel
 */
public abstract class AbstractEffect implements Serializable{
	private static final long serialVersionUID = 3592940786938748249L;
	
	int[] modifiers;
	int[] range;
	private boolean enabled = true;
	
	public AbstractEffect(int[] modifiers, int[] range){
		this.modifiers = modifiers;
		this.range = range;
	}
	
	/**
	 * Applies an effect to the PixelStream.
	 * @param pixelStream PixelStream to apply effect to.
	 */
	public abstract void applyEffect(PixelStream pixelStream);
	
	/**
	 * Returns a String that describes the effect that was applied.
	 * @return Description of effect.
	 */
	public abstract String toString();
	
	/**
	 * Returns channel names for masks.
	 * @param masks Masks
	 * @return Channel names
	 */
	public String getChannelNames(int [] masks) {
		String channels = "";
		for (int i : masks) {
			switch (i) {
			case 0xff000000:
				channels += "A ";
				break;
			case 0xff0000:
				channels += "R ";
				break;
			case 0xff00:
				channels += "G ";
				break;
			case 0xff:
				channels += "B ";
				break;
			}
		}
		return channels;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void toggleEnabled() {
		enabled = !enabled;
	}
}