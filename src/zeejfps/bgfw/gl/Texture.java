package zeejfps.bgfw.gl;

import static org.lwjgl.opengl.GL11.*;

import zeejfps.bgfw.gfx.Bitmap;

public class Texture {

	public final int id;
	public final int width, height, target;
	
	private Texture(int id, int target, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.target = target;
	}
	
	public static TextureMaker gen(int target, int level, int internalformat, 
			int border, int format, int type, Bitmap bitmap) {
		
		int id = glGenTextures();
		glBindTexture(target, id);
		glTexImage2D(target, level, internalformat, bitmap.getWidth(), bitmap.getHeight(),
				border, format, type, bitmap.getPixels());
		
		Texture t = new Texture(id, target, bitmap.getWidth(), bitmap.getHeight());
		return t.new TextureMaker();
	}
	
	public class TextureMaker {	
		
		public TextureMaker param(int pname, int value) {
			glTexParameterf(target, pname, value);
			return this;
		}
		
		public Texture get() {
			glBindTexture(target, 0);
			return Texture.this;
		}
	}
	
}
