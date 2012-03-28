package uk.co.gundry.david.androidsimple3d.mesh;

/**
 * A simple plane mesh for 2D drawing
 */
public class Square extends Mesh {
	/**
	 * Create a square
	 * 
	 * @param width the width of the square, in pixels
	 * @param height the height of the square, in pixels
	 */
	public Square(float width, float height)
	{
		float textureCoordinates[] = { 
			0.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f,
		};

		short[] indices = new short[] { 0, 1, 2, 1, 3, 2 };

		float[] vertices = new float[] { 
			0f, 0f, 0.0f,
			width, 0f, 0.0f,
			0f, height, 0.0f,
			width, height, 0.0f };

		setIndices(indices);
		setVertices(vertices);
		setTextureCoordinates(textureCoordinates);
	}
}
