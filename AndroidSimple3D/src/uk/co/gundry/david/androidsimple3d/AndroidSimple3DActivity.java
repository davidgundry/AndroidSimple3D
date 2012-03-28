package uk.co.gundry.david.androidsimple3d;

import java.io.IOException;

import uk.co.gundry.david.androidsimple3d.mesh.GroundPlane;
import uk.co.gundry.david.androidsimple3d.mesh.Square;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class AndroidSimple3DActivity extends Activity {
	
	/**
	 * The renderer that will render the game.
	 */
	GameRenderer renderer;
	/**
	 * The last known X coordinate for two pointers
	 */
	float[] lastX  = new float[2];
	/**
	 * The last known Y coordinate for two pointers
	 */
	float[]	lastY = new float[2];
	/**
	 * Whether each pointer started on the movement joystick on the HUD.
	 */
	boolean[] startedOnJoystick = new boolean[2];
	/**
	 * Where the movement joystick should be on the screen.
	 */
	Point moveHub = new Point(42,198);
	/**
	 * Radius of the movement joystick
	 */
	int moveRadius = 32;

	/** 
	 * Called when the activity is first created.
	 * Does initial setup, and then starts the game.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setup();
		startGame();
	}
	
	/**
	 * Called from onCreate(). Sets up things that need to be set up
	 * at the start.
	 */
	private void setup()
	{
		// Android setup
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Create a OpenGL view.
		GLSurfaceView view = new GLSurfaceView(this);

		// Renderer setup
		renderer = new GameRenderer();
		view.setRenderer(renderer);
		setContentView(view);
	}
	
	/**
	 * Loads meshes into world, positions camera, setup HUD, etc.
	 */
	private void startGame()
	{
		// Setup HUD items
		Square joystick = new Square(moveRadius*2,moveRadius*2);
		joystick.x = 10;
		joystick.y = 10;

		try {
			// Create a GroundPlane.
			GroundPlane plane = new GroundPlane(BitmapFactory.decodeStream(getAssets().open("heightMap.png")),BitmapFactory.decodeStream(getAssets().open("textureMap.png")));
			Bitmap ground = BitmapFactory.decodeStream(getAssets().open("ground.png")).copy(Bitmap.Config.RGB_565, false);
			plane.setBitmap(ground);
			renderer.getScene().add(plane);
			renderer.getCamera().setPos(plane.getWidth()/2,2,plane.getDepth()/2);
			
			Bitmap joy = BitmapFactory.decodeStream(getAssets().open("joy.png"));
			joystick.setBitmap(joy);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Add HUD items to the hud
		renderer.getHud().add(joystick);
	}
	
	/**
	 * When the user touches the screen
	 * Hopefully multi-touch works...
	 */
	@Override
	public boolean onTouchEvent(MotionEvent me)
	{
		for(int i=0;i<2;i++)
		{
			int pointerId = me.getPointerId(i);	
			
			float x = me.getX(pointerId);
			float y = me.getY(pointerId);
			
			if ((me.getActionMasked() == MotionEvent.ACTION_DOWN) ||  (me.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN))
			{
				lastX[i] = x;
				lastY[i] = y;
				
				if ((x<moveHub.x + moveRadius) && (y>moveHub.y - moveRadius) && (x>moveHub.x - moveRadius) && (y<moveHub.y + moveRadius))
					startedOnJoystick[i] = true;
			}
			
			if (startedOnJoystick[i])
			{
				renderer.getCamera().setVel(Math.min((moveHub.y - y),moveRadius), Math.min((moveHub.x - x),moveRadius));
				if (me.getActionMasked() == MotionEvent.ACTION_UP)
				{ // stop moving if the finger is released from the joystick
					renderer.getCamera().setVel(0, 0);
					startedOnJoystick[i] = false;
				}
			}
			else if (!startedOnJoystick[i])
			{
				float xDiff = lastX[i] - x;
				float yDiff = lastY[i] - y;
				renderer.getCamera().rotX(yDiff);//look down/up
				renderer.getCamera().rotY(xDiff);//look left/right
			}

			lastX[i] = x;
			lastY[i] = y;
		}
		return super.onTouchEvent(me);
	}
}