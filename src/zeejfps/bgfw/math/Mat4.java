package zeejfps.bgfw.math;

import java.nio.FloatBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;

public class Mat4 {

	private final float[] components;

	public Mat4() {
		this(new float[16]);
	}

	public Mat4(float[] components){		
		if (components.length != 16)
			throw new IllegalArgumentException("Invalid float[], must be of size 16!");
		this.components = components;
	}
	
	public Mat4(Mat4 copy) {
		this(Arrays.copyOf(copy.components, copy.components.length));
	}
	
	public FloatBuffer toFloatBuffer() {	
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(components);
		buffer.flip();
		
		return buffer;
	}
	
	public Mat4 multiply(Mat4 other) {	
		float[] components = new float[16];
		int i = 0, j = 0, k = 0;
		float sum = 0;
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				for (k = 0; k < 4; k++) {				
					sum += this.components[i*4 + k] * other.components[k*4 + j];	
				}
				components[i*4 + j] = sum; 	
				sum = 0;
			}	
		}
		return new Mat4(components);	
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		for (int col = 0; col < 4; col++) {
			for (int row = 0; row < 4; row++) {
				sb.append(components[col*4 + row]).append(" ");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public static Mat4 identity() {		
		float[] components = {
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1
		};
		return new Mat4(components);
	}
	
}
