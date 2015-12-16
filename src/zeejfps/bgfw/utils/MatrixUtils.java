package zeejfps.bgfw.utils;

import zeejfps.bgfw.math.Mat4;
import zeejfps.bgfw.math.Vec3;

public class MatrixUtils {

	private MatrixUtils() {}
	
	public static Mat4 createRotationMatrix(Vec3 rotation) {
		Mat4 rotX = createRotationX(rotation.x);
		Mat4 rotY = createRotationY(rotation.y);
		Mat4 rotZ = createRotationZ(rotation.z);
		
		return rotZ.multiply(rotX).multiply(rotY);
	}
	
	public static Mat4 createScaleMatrix(Vec3 scale) {
		float[] components = {
				scale.x, 0, 0, 0,
				0, scale.y, 0, 0,
				0, 0, scale.z, 0,
				0, 0, 0, 1
		};
		return new Mat4(components);
	}
	
	public static Mat4 createTranslationMatrix(Vec3 translation) {	
		float[] components = {
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				translation.x, translation.y, translation.z, 1
		};
		return new Mat4(components);
	}
	
	public static Mat4 createPerspective(float fov, float aspect, float near, float far) {
		float angle  = (float) Math.toRadians(fov);
		float yScale = (float) (1f / Math.tan(angle/2f));
		float xScale = yScale / aspect;
		float nearmfar = near - far;
		
		float[] components = {
			xScale, 0, 0, 0,
			0, yScale, 0, 0,
			0, 0, (far+near)/nearmfar, -1,
			0, 0, 2*far*near / nearmfar, 0
		};

		return new Mat4(components);
	}
	
	public static Mat4 createRotationX(float angle) {
		angle = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float[] components = {
			1, 0, 0, 0,
			0, cos, -sin, 0,
			0, sin, cos, 0, 
			0, 0, 0, 1
		};
		return new Mat4(components);
	}
	
	public static Mat4 createRotationY(float angle) {
		angle = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float[] components = {
			cos, 0, sin, 0,
			0, 1, 0, 0,
			-sin, 0, cos, 0, 
			0, 0, 0, 1
		};
		return new Mat4(components);
	}
	
	public static Mat4 createRotationZ(float angle) {
		angle = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float[] components = {
			cos, -sin, 0, 0,
			sin, cos, 0, 0,
			0, 0, 1, 0, 
			0, 0, 0, 1
		};
		return new Mat4(components);
	}
	
}
