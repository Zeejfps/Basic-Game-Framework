package zeejfps.bgfw.math;

public class Vec2 {

	public final float x, y;
	
	public Vec2() {
		this(0, 0);
	}
	
	public Vec2(float x, float y) {
		this.x = x; 
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "Vec2 [" + x + ", " + y + "]";
	}
	
}
