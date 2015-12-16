package zeejfps.bgfw.math;

public class Vec4 {

public final float x, y, z, w;
	
	public Vec4() {
		this(0, 0, 0, 0);
	}
	
	public Vec4(float x, float y, float z, float w) {
		this.x = x; 
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	@Override
	public String toString() {
		return "Vec4 [" + x + ", " + y + ", " + z + ", " + w + "]";
	}
	
}
