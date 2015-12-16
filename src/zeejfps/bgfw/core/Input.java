package zeejfps.bgfw.core;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Arrays;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import zeejfps.bgfw.math.Vec2;

public class Input {

	private static final int MAX_KEYS = 512;
	private static final int MAX_MOUSE_BUTTONS = 10;
	
	private static Input instance;
	
	private final GLFWKeyCallback keyCallback;
	private final GLFWMouseButtonCallback mouseButtonCallback;
	private final GLFWCursorPosCallback cursorPosCallback;
	
	private Vec2 mousePos;
	private final boolean[] mouseButtonDown = new boolean[MAX_MOUSE_BUTTONS];
	private final boolean[] mouseButtonUp = new boolean[MAX_MOUSE_BUTTONS];
	private final boolean[] mouseButton = new boolean[MAX_MOUSE_BUTTONS];
	private final boolean[] keysDown = new boolean[MAX_KEYS];
	private final boolean[] keysUp = new boolean[MAX_KEYS];
	private final boolean[] keys = new boolean[MAX_KEYS];
	
	private Input() {
		mousePos = new Vec2();
		keyCallback = new KeyCallback();
		mouseButtonCallback = new MouseButtonCallback();
		cursorPosCallback = new CursorPosCallback();
	};
	
	public static Vec2 getMousePosition() {
		return instance.mousePos;
	}
	
	public static boolean getKeyDown(int key) {
		return instance.keysDown[key];
	}
	
	public static boolean getKeyUp(int key) {
		return instance.keysUp[key];
	}
	
	public static boolean getKey(int key) {
		return instance.keys[key];
	}
	
	public static boolean getMouseButtonDown(int button) {
		return instance.mouseButtonDown[button];
	}
	
	public static boolean getMouseButtonUp(int button) {
		return instance.mouseButtonUp[button];
	}
	
	public static boolean getMouseButton(int button) {
		return instance.mouseButton[button];
	}
	
	protected static void create(long windowID) {
		if (instance == null) {
			instance = new Input();
	        glfwSetKeyCallback(windowID, instance.keyCallback);
	        glfwSetCursorPosCallback(windowID, instance.cursorPosCallback);
	        glfwSetMouseButtonCallback(windowID, instance.mouseButtonCallback);
	        //glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		}
	}
	
	protected static void poll() {
		Arrays.fill(instance.mouseButtonUp, false);
		Arrays.fill(instance.mouseButtonDown, false);
		Arrays.fill(instance.keysDown, false);
		Arrays.fill(instance.keysUp, false);
		glfwPollEvents();
	}
	
	protected static Input getInstance() {
		return instance;
	}
	
	protected static void destroy() {
		instance.keyCallback.release();
		instance.mouseButtonCallback.release();
		instance.cursorPosCallback.release();
	}
	
	private class KeyCallback extends GLFWKeyCallback {

		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			switch (action) {
				case GLFW_RELEASE:
					keysUp[key] = true;
					keysDown[key] = false;
					keys[key] = false;
					break;
				
				case GLFW_PRESS:
					
					keysUp[key] = false;
					keysDown[key] = true;
					keys[key] = true;
					break;
			}
		}
		
	}
	
	private class MouseButtonCallback extends GLFWMouseButtonCallback {

		@Override
		public void invoke(long window, int button, int action, int mods) {
			switch (action) {
			case GLFW_RELEASE:
				mouseButtonUp[button] = true;
				mouseButtonDown[button] = false;
				mouseButton[button] = false;
				break;
			
			case GLFW_PRESS:
				mouseButtonUp[button] = false;
				mouseButtonDown[button] = true;
				mouseButton[button] = true;
				break;
			}
		}
		
	}
	
	private class CursorPosCallback extends GLFWCursorPosCallback {

		@Override
		public void invoke(long window, double xpos, double ypos) {
			mousePos = new Vec2((float)xpos, (float)ypos);
		}
		
	}
	
}
