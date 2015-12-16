package zeejfps.bgfw.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Texture {

	private final int id;
	private final int width, height;
	private final int filter, wrap;
	
	private Texture(int id, int width, int height, int filter, int wrap) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.filter = filter;
		this.wrap = wrap;
	}
	
	public int getPointer() {
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getFilterMode() {
		return filter;
	}
	
	public int getWrapMode() {
		return wrap;
	}

	public static Texture load(InputStream is, int filter, int wrap) {	
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int b;
		try {
			while ((b = is.read()) != -1) {
				bos.write(b);
			}
		} catch (IOException e) {
			System.err.println("Failed to load image from input stream!");
			return new Texture(-1, 0, 0, 0, 0);
		}
		byte[] bytes = bos.toByteArray();
		ByteBuffer fileBuffer = BufferUtils.createByteBuffer(bytes.length);
		fileBuffer.put(bytes);
		fileBuffer.flip();
	
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer componentBuffer = BufferUtils.createIntBuffer(1);
		
		ByteBuffer imageBuffer;
		imageBuffer = stbi_load_from_memory(fileBuffer, widthBuffer, heightBuffer, componentBuffer, 0);
	
		int width = widthBuffer.get(0);
		int height = heightBuffer.get(0);

		Texture texture = load(imageBuffer, width, height, filter, wrap);	
		stbi_image_free(imageBuffer);
		
		return texture;				
	}
	
	public static Texture load(ByteBuffer image, int width, int height, int filter, int wrap) {
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return new Texture(id, width, height, filter, wrap);
	}

}
