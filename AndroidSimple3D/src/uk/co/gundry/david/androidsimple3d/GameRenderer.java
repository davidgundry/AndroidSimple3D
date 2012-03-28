package uk.co.gundry.david.androidsimple3d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import uk.co.gundry.david.androidsimple3d.mesh.Group;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

/**
 * An OpenGL renderer, which can render a group of objects in a 3D scene,
 * and render a 2D HUD.
 */
public class GameRenderer implements Renderer {
	
	/**
	 * A Group containing meshes in the world
	 */
	private final Group scene;
	/**
	 * A Group containing meshes in the head-up display
	 */
	private final Group hud;
	/**
	 * The camera details that will be used to render the scene
	 */
	private final Camera camera;
	/**
	 * Width of the screen in pixels
	 */
	private int width;
	/**
	 * Height of the screen in pixels
	 */
	private int height;
	
	/**
	 * Create a new GameRenderer with an empty scene and an empty hud.
	 */
	public GameRenderer()
	{
		scene = new Group();
		hud = new Group();	
		camera = new Camera();
	}	
	
	/**
	 * When the surface is created, set some properties about the renderer.
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		// Set background colour
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		
		// Depth setup.
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
	    gl.glViewport(0, 0, width, height);
	    gl.glDisable(GL10.GL_DITHER);
	    gl.glEnable(GL10.GL_CULL_FACE);
	}

	/** 
	 * Draw everything on screen
	 */
	public void onDrawFrame(GL10 gl)	
	{	
		// Clear to background colour
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

	    gl.glLoadIdentity();
	    onSurfacePerspective(gl);
	    camera.update(gl);
		scene.draw(gl);
	    onSurfaceOrtho(gl);
	    
	    gl.glEnable(GL10.GL_BLEND);    
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

	    // Enable alpha bits for good semi-transparency
	    gl.glEnable(GL10.GL_ALPHA_BITS);
	    hud.draw(gl);
	    gl.glDisable(GL10.GL_ALPHA_BITS);
	}
	
	/**
	 * When the surface changes, update the width and height of the renderer
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		this.width = width;
		this.height = height;
		camera.setAspect((float) width/ (float) height);
		
	    gl.glViewport(0, 0, width, height);
	    gl.glDisable(GL10.GL_DITHER);
	    gl.glEnable(GL10.GL_DEPTH_TEST);
	    gl.glEnable(GL10.GL_CULL_FACE);
	}

	/**
	 * Set orthographic mode for 2D drawing
	 * @param gl
	 */
	public void onSurfaceOrtho(GL10 gl) {
	    gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
	    gl.glOrthof(0.0f, width, 0.0f, height, -0.1f, 0.1f);
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	}

	/**
	 * Set perspective mode for 3D drawing
	 * @param gl
	 */
	public void onSurfacePerspective(GL10 gl) {
	    gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
	    GLU.gluPerspective(gl, camera.getFovy(), camera.getAspect(), 0.1f, 1000.0f);
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	}
	
	/**
	 * Returns a group containing all 3D objects in the world
	 */
	public Group getScene(){
		return scene;
	}

	/**
	 * Returns a group containing all 2D objects to be drawn as a head-up display
	 */
	public Group getHud() {
		return hud;
	}

	/**
	 * Returns the camera
	 * @return
	 */
	public Camera getCamera() {
		return camera;
	}
}
