package uk.co.gundry.david.androidsimple3d.mesh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Mesh {
	protected FloatBuffer verticesBuffer = null;
	protected ShortBuffer indicesBuffer = null;
	protected FloatBuffer textureBuffer = null;

	protected int[] textureId;
	/**
	 * Bitmap that will be used to texture the mesh
	 */
	protected Bitmap bitmap; 
	/**
	 * True if we need to load the texture
	 */
	protected boolean loadTexture = false;

	/**
	 * The number of indicies in the mesh
	 */
	protected int indicesCount;

	// Mesh Translation
	public float x = 0;
	public float y = 0;
	public float z = 0;

	// Mesh Rotation
	public float rx = 0;
	public float ry = 0;
	public float rz = 0;
	
	/**
	 * Renders the mesh.
	 * 
	 * @param gl the OpenGL context to render to.
	 */
	public void draw(GL10 gl)
	{
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		// set vertex array
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);

		// perform translations and rotations applied to the mesh.
		gl.glTranslatef(x, y, z);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
		
		// Texture
		if (loadTexture) {
			loadGLTexture(gl);
			loadTexture = false;
		}
		if (textureBuffer != null) {
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

			// Point to our buffers
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId[0]);
		}
		
		// Draw the mesh
		gl.glDrawElements(GL10.GL_TRIANGLES, indicesCount, GL10.GL_UNSIGNED_SHORT, indicesBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		if (textureBuffer != null)
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glDisable(GL10.GL_CULL_FACE);
	}

	/**
	 * Set the vertices.
	 * 
	 * @param vertices
	 */
	protected void setVertices(float[] vertices)
	{
		// float has 4 bytes
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		verticesBuffer = vbb.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);
	}

	/**
	 * Set the indices.
	 * 
	 * @param indices
	 */
	protected void setIndices(short[] indices)
	{
		// short has 2 bytes.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indicesBuffer = ibb.asShortBuffer();
		indicesBuffer.put(indices);
		indicesBuffer.position(0);
		indicesCount = indices.length;
	}

	/**
	 * Set the texture coordinates.
	 * 
	 * @param textureCoords
	 */
	protected void setTextureCoordinates(float[] textureCoords)
	{
		// float has 4 bytes.
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(textureCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(textureCoords);
		textureBuffer.position(0);
	}
	
	/**
	 * Set the bitmap array to load as textures
	 * 
	 * @param bitmap
	 */
	public void setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
		loadTexture = true;
	}

	/**
	 * Loads textures from array of bitmap.
	 * textureId is set to the length of the bitmap array and filled with the texture names
	 * 
	 * @param gl
	 */
	protected void loadGLTexture(GL10 gl)
	{
		textureId = new int[1];
		gl.glGenTextures(1, textureId, 0);

			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId[0]);
			
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
			
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	}
}
