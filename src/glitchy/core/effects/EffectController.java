package glitchy.core.effects;

import java.util.ArrayList;

import glitchy.core.imageProcessing.PixelStream;

/**
 * This class controls the effects subpackage.
 * @author Aksel, Mikkel and Rasmus
 *
 */
public class EffectController{

	/**
	 * Runs through a pixelstream's effects, and applies them as desired.
	 * @param pixelStream PixelStream to apply effects to.
	 */
	public void applyEffects(PixelStream pixelStream){
		ArrayList<AbstractEffect> effects = pixelStream.getEffects();
		for(AbstractEffect effect : effects){
			if(effect.isEnabled()){
				effect.applyEffect(pixelStream);
			}
		}
	}
	
	/**
	 * Creates the desired effect, and applies and adds it to the pixelstream.
	 * @param pixelStream PixelStream to apply an effect to.
	 * @param e Desired effect.
	 * @param modifiers Effect modifiers
	 */
	public void applyEffect(PixelStream pixelStream, Effect e, int[] modifiers){	
		
		AbstractEffect effect = null;

		int[] range = pixelStream.getSelectionRange();
		
		switch(e){
		case BITSHIFT:
			effect = new BitShiftEffect(modifiers,range);
			break;

		case INVERT:
			effect = new InvertEffect(modifiers,range);
			break;
			
		case REORDER:
			effect = new ReorderEffect(modifiers,range);
			break;
			
		case SHUFFLE:
			effect = new ShuffleEffect(modifiers,range);
			break;
			
		case SMEAR:
			effect = new SmearEffect(modifiers,range);
			break;
			
		case SORT:
			effect = new SortEffect(modifiers,range);
			break;
		}
		
		effect.applyEffect(pixelStream);
		pixelStream.addEffect(effect);
	}
}