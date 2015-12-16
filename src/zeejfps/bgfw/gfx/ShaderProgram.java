package zeejfps.bgfw.gfx;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

public class ShaderProgram {

	private  final int id;
	private final Map<String, Integer> uniforms;
	private final Map<String, Integer> attribs;
	
	private ShaderProgram(int id) {
		this.id = id;
		uniforms = new HashMap<String, Integer>();
		attribs = new HashMap<String, Integer>();
	}

	public int getUniformLocation(String name) {
		if (!uniforms.containsKey(name)) {
			int loc = glGetUniformLocation(id, name);
			if (loc != -1) {
				uniforms.put(name, loc);
			}
			else {
				System.err.println("No uniform found: '" + name + "'");
			}
			return loc;
		}
		return uniforms.get(name);
	}
	
	public int getAttribLocation(String name) {
		if (!attribs.containsKey(name)) {
			int loc = glGetAttribLocation(id, name);
			if (loc != -1) {
				attribs.put(name, loc);
			}
			else {
				System.err.println("No attribute found: '" + name + "'");
			}
			return loc;
		}
		return attribs.get(name);
	}
	
	public int getPointer() {
		return id;
	}
	
	public static ShaderProgram create(Shader... shaders) {
		
		if (shaders.length == 0) {
			throw new IllegalArgumentException("No shaders provided");
		}
			
		int id = glCreateProgram();
		for (int i = 0; i < shaders.length; i++) {
			int shaderID = shaders[i].getPointer();
			glAttachShader(id, shaderID);
		}
		
		glLinkProgram(id);
		glValidateProgram(id);
		
		for (int i = 0; i < shaders.length; i++) {
			int shaderID = shaders[i].getPointer();
			glDetachShader(id, shaderID);
		}
		
		return new ShaderProgram(id);
	}
	
}
