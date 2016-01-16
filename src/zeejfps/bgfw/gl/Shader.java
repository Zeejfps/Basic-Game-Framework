package zeejfps.bgfw.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public final class Shader {

	public final int id;
	public final int type;
	
	private Shader(int id, int type) {
		this.id = id;
		this.type = type;
	}
	
	public static Shader compile(String src, int type) {
		
		int id = glCreateShader(type);
		glShaderSource(id, src);
		glCompileShader(id);
		
		if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(id));
			id = 0;
		}
		
		return new Shader(id, type);
	}
	
}
