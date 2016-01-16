package zeejfps.bgfw.gfx;

import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import zeejfps.bgfw.util.IOUtils;

public class Bitmap {

	private int width, height;
	private ByteBuffer pixels;
	
	public Bitmap(int width, int height, ByteBuffer pixels) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public ByteBuffer getPixels() {
		return pixels;
	}
	
	public static Bitmap loadFromFile(String file) throws IOException {	
		
		ByteBuffer fileBuffer      = IOUtils.resourceToBuffer(file);
		IntBuffer  widthBuffer 	   = BufferUtils.createIntBuffer(1);
		IntBuffer  heightBuffer	   = BufferUtils.createIntBuffer(1);
		IntBuffer  componentBuffer = BufferUtils.createIntBuffer(1);
		
		ByteBuffer pixels;
		pixels = stbi_load_from_memory(fileBuffer, widthBuffer, heightBuffer, componentBuffer, 0);
	
		int width = widthBuffer.get(0);
		int height = heightBuffer.get(0);

		Bitmap b = new Bitmap(width, height, pixels);
		stbi_image_free(pixels);
		return b;
	}
	
}
