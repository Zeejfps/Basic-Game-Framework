package zeejfps.bgfw.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public abstract class Game {

	private static final double UPDATES_PER_SECOND = 30;
	private static final double UPDATES_PER_MILI = 1000.0 / UPDATES_PER_SECOND;
	private static final int MAX_FRAMES_SKIPED = 5;
	
	private final String 	  title;
	private final int 		  width;
	private final int 		  height;

	private long 			  windowID;
	private boolean 	      running;
	private GLFWErrorCallback errorCallback;
	
	private int 	frames;
	private int 	updates;
	private long 	debugTimer;
	private boolean debug;
	
	public Game(String title, int width, int height) {		
		this.title = title;
		this.width = width;
		this.height = height;

		frames = 0;
		updates = 0;
		debug = false;
		running = false;
	
		initContext();
	}
	
	protected abstract void onFixedUpdate();
	
	protected abstract void onUpdate();
	
	protected abstract void onRender();
	
	protected abstract void onExit();
	
	private void initContext() {		
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		
		if ( glfwInit() != GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);

        windowID = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
            windowID,   
            (vidmode.width() - width) / 2,
            (vidmode.height() - height) / 2
        );
 
        glfwMakeContextCurrent(windowID);
        glfwShowWindow(windowID);
        GL.createCapabilities();
		debugTimer = System.currentTimeMillis();
	}
	
	private void fixedUpdate() {
		onFixedUpdate();
	}
	
	private void update() {
		onUpdate();
		updates++;
		if (debug && System.currentTimeMillis() - debugTimer >= 1000) {
			System.out.println("FPS: " + frames + ", UPS: " + updates);
			updates = 0;
			frames = 0;
			debugTimer = System.currentTimeMillis();
		}
	}
	
	private void render() {
		onRender();
		frames++;
		glfwSwapBuffers(windowID);
	}
	
	public final void launch() {
		running = true;
		mainLoop();
	}
	
	public final void quit() {
		running = false;
	}
	
	public void showDebug(boolean show) {
		debug = show;
	}

	private void mainLoop() {
		try  {
			Input.create(windowID);
			double t = System.currentTimeMillis();
			int loops;
			//float interpolation;
			while (running && glfwWindowShouldClose(windowID) == GL_FALSE) {
			
				loops = 0;	
				while (System.currentTimeMillis() > t && loops < MAX_FRAMES_SKIPED) {
					Input.poll();
					fixedUpdate();
					
					t += UPDATES_PER_MILI;
					loops++;
				}
				//interpolation = (float)((System.currentTimeMillis() + UPDATES_PER_MILI - t) / UPDATES_PER_MILI);
				update();
				render();
			}
			onExit();
			glfwDestroyWindow(windowID);
			Input.destroy();	
		} finally {
			glfwTerminate();
            errorCallback.release();
		}
	}
}
