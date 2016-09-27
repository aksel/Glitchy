package glitchy.core.io;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class is used for raw data conversion from bytes to integers.
 * @author Mikkel and Aksel
 *
 */
public class RAWDataConverter {

	interface ByteConverter{
		int bytesToPixel(int byteIndex, byte[] buffer);
	}

	//100mb
	final int maxLength = 104857600;

	/**
	 * This method takes a FileInputStream, reads its bytes, 
	 * and merges them into packed integers.
	 * @param fis FileInputStream.
	 * @return int[] of Bytes merged into a pixel.
	 */
	public int[] readRaw(FileInputStream fis, boolean alpha) {

		try {
			//number of channels per pixel
			int channelNumber;

			ByteConverter converter;
			int bufferSize;

			if(alpha){
				converter = (byteIndex, buffer) -> {
					return mergeBytes(
							buffer[byteIndex++],
							buffer[byteIndex++],
							buffer[byteIndex++],
							buffer[byteIndex]);
				};

				channelNumber = 4;
				bufferSize = 1024;
			}

			else{
				converter = (byteIndex, buffer) -> {
					return mergeBytes(
							(byte) 0xff,
							buffer[byteIndex++],
							buffer[byteIndex++],
							buffer[byteIndex]);
				};

				channelNumber = 3;
				bufferSize = 1215;
			}

			int length = fis.available();
			if(length > maxLength)
				length = maxLength;

			int[] container = new int[length/channelNumber];
			byte[] buffer   = new byte[bufferSize];

			int pixelIndex = 0;
			while(fis.read(buffer) != -1){
				for(int byteIndex  = 0;
						byteIndex  < bufferSize &&
						pixelIndex < container.length;
						byteIndex += channelNumber){
					container[pixelIndex++] = converter.bytesToPixel(byteIndex,buffer);
				}
			}

			fis.close();
			fis = null;

			return container;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method merges 4 bytes into a packed integer.
	 * @param a A byte which represents an Alpha channel value.
	 * @param r A byte which represents a Red channel value.
	 * @param g A byte which represents a Green channel value.
	 * @param b A byte which represents a Blue channel value.
	 * @return A packed integer, that represents a pixel.
	 */
	public static int mergeBytes(byte a, byte r, byte g, byte b){
		return  (Byte.toUnsignedInt(a) << 24) |
				(Byte.toUnsignedInt(r) << 16) |
				(Byte.toUnsignedInt(g) <<  8) |
				(Byte.toUnsignedInt(b));
	}
}