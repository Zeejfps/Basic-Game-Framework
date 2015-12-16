package zeejfps.bgfw.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Shader {

	private final int id;
	private final int type;
	private final String src;
	
	private Shader(int id, int type, String src) {
		this.id = id;
		this.type = type;
		this.src = src;
	}
	
	public String getSrc() {
		return src;
	}
	
	public int getType() {
		return type;
	}
	
	public int getPointer() {
		return id;
	}
	
	public static Shader compile(String src, int type) {
		
		int id = glCreateShader(type);
		glShaderSource(id, src);
		glCompileShader(id);
		
		if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(id));
			id = 0;
		}
		
		return new Shader(id, type, src);
	}
	
	public static Shader load(InputStream is, int type) {
	
		String src = "";
		try {
			src = readSrc(is);
		} catch (IOException e) {
			System.err.println("Failed to read shader src!");
		}
		return compile(src, type);
	}
	
	private static String readSrc(InputStream is) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		br.close();
		return sb.toString();
	}
	
}
