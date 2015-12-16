package zeejfps.bgfw.core;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.File;
import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

public abstract class Game {

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
	
	protected abstract void onUpdate();
	
	protected abstract void onRender(float interpolation);
	
	protected abstract void onDispose();
	
	private void initContext() {	
		
		System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
		System.out.println(System.getProperty("org.lwjgl.librarypath"));
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		
		if ( glfwInit() != GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        
        windowID = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
            windowID,
            (GLFWvidmode.width(vidmode) - width) / 2,
            (GLFWvidmode.height(vidmode) - height) / 2
        );
 
        glfwMakeContextCurrent(windowID);
        glfwShowWindow(windowID);
        GLContext.createFromCurrent();

		debugTimer = System.currentTimeMillis();
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
	
	private void render(float interpolation) {
		onRender(interpolation);
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
	
	private static final double UPDATES_PER_SECOND = 30;
	private static final double UPDATES = 1000.0 / UPDATES_PER_SECOND;
	private static final int MAX_FRAMES_SKIPED = 5;

	private void mainLoop() {

		try  {
			Input.create(windowID);
			double t = System.currentTimeMillis();
			int loops;
			float interpolation;
			while (running && glfwWindowShouldClose(windowID) == GL_FALSE) {
			
				loops = 0;	
				while (System.currentTimeMillis() > t && loops < MAX_FRAMES_SKIPED) {
					Input.poll();
					update();
					
					t += UPDATES;
					loops++;
				}
				interpolation = (float)((System.currentTimeMillis() + UPDATES - t) / UPDATES);
				render(interpolation);
			}
			onDispose();
			glfwDestroyWindow(windowID);
			Input.destroy();
			
		} finally {
			glfwTerminate();
            errorCallback.release();
		}
		
	}
	
}
