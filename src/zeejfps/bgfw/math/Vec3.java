package zeejfps.bgfw.math;

public class Vec3 {
	
	public final float x, y, z;
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3 add(float x, float y, float z) {
		return new Vec3(this.x + x, this.y + y, this.z + z);
	}
	
	public Vec3 add(float value) {
		return add(value, value, value);
	}
	
	public Vec3 add(Vec3 v) {
		return add(v.x, v.y, v.z);
	}
	
	@Override
	public String toString() {
		return "Vec3 [" + x + ", " + y + ", " + z + "]";
	}

}
