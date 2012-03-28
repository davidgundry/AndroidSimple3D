package uk.co.gundry.david.androidsimple3d.mesh;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * A horizontal plane defined by a height map, where each square segment is
 * textured according to a texture map.
 */
public class GroundPlane extends Mesh {
	
	/**
	 * The width of the plane
	 */
	private final int width;
	/**
	 * The depth of the plane
	 */
	private final int depth;
	/**
	 * The width of each segment
	 */
	private static final short sWidth = 10;
	/**
	 * The depth of each segment
	 */
	private static final short sDepth = 10;
	/**
	 * The number of textures high the texture atlas is
	 */
	private static final float atlasWide = 2;
	/**
	 * The number of textures wide the texture atlas is
	 */
	private static final float atlasHigh = 2;

    /**
     * Create a horizontal plane which is divided up into segments
     * 
     * @param heightMap a bitmap where each pixel represents the y of one vector
     * @param textureMap a bitmap where each pixel corresponds to one square face
     */
    public GroundPlane(Bitmap heightMap, Bitmap textureMap) 
    {		
    	int widthSegments = heightMap.getWidth()-1;
    	int depthSegments = heightMap.getHeight()-1;
		
		width = widthSegments * sWidth;
		depth = depthSegments * sDepth;
    	
		float[] vertices = new float[(widthSegments ) * (depthSegments) * 18];
		short[] indices = new short[vertices.length];
		float[] textureCoordinates = new float[(widthSegments) * (depthSegments ) * 12];

		int currentVertex = 0;
		int currentTex = 0;
		
		// This code loops through every square segment and sets the vertices for the two triangles
		// inside it, and sets the texture coordinates
		for (int z=depthSegments; z>0; z--)
			for (int x=0; x<widthSegments; x++) 	
			{
				// First Triangle
				float[] hsv = new float[3];
				Color.RGBToHSV(Color.red(heightMap.getPixel(x, z)), Color.green(heightMap.getPixel(x, z)), Color.blue(heightMap.getPixel(x, z)), hsv);
				
				vertices[currentVertex] = x * sWidth;
				vertices[currentVertex + 1] = 1-(hsv[2]);
				vertices[currentVertex + 2] = z * sDepth;
	
				float[] hsvx1 = new float[3];
				Color.RGBToHSV(Color.red(heightMap.getPixel(x+1, z)), Color.green(heightMap.getPixel(x+1, z)), Color.blue(heightMap.getPixel(x+1, z)), hsvx1);
				
				vertices[currentVertex + 3] = (x+1) * sWidth;
				vertices[currentVertex + 4] = 1-(hsvx1[2]);
				vertices[currentVertex + 5] = z * sDepth;
				
				float[] hsvz1 = new float[3];
				Color.RGBToHSV(Color.red(heightMap.getPixel(x, z-1)), Color.green(heightMap.getPixel(x, z-1)), Color.blue(heightMap.getPixel(x, z-1)), hsvz1);
				
				vertices[currentVertex + 6] = x * sWidth;
				vertices[currentVertex + 7] = 1-(hsvz1[2]);
				vertices[currentVertex + 8] = (z-1) * sDepth;
				
				// Second Triangle
				vertices[currentVertex + 9] = (x+1) * sWidth;
				vertices[currentVertex + 10] = 1-(hsvx1[2]);
				vertices[currentVertex + 11] = z * sDepth;
				
				float[] hsvx1z1 = new float[3];
				Color.RGBToHSV(Color.red(heightMap.getPixel(x+1, z-1)), Color.green(heightMap.getPixel(x+1, z-1)), Color.blue(heightMap.getPixel(x+1, z-1)), hsvx1z1);
			
				vertices[currentVertex + 12] = (x+1) * sWidth;
				vertices[currentVertex + 13] = 1-(hsvx1z1[2]);
				vertices[currentVertex + 14] = (z -1) * sDepth;
				
				vertices[currentVertex + 15] = x * sWidth;
				vertices[currentVertex + 16] = 1-(hsvz1[2]);
				vertices[currentVertex + 17] = (z -1) * sDepth;
				
				currentVertex += 18;
		
				// interpret heightMap
				// test code
				int tex = 0;
				if (Color.red(heightMap.getPixel(x, z)) >0)
					tex = 1; 
				if (Color.blue(heightMap.getPixel(x, z)) > 0)
					tex = 2; 
				
				float texx = ((tex%atlasWide)/atlasWide);
				float texy = ((tex/atlasWide)/atlasHigh);
				// First Triangle
				textureCoordinates[currentTex] = texx;
				textureCoordinates[currentTex + 1] = texy;
				
				textureCoordinates[currentTex + 2] = (texx+ ((float)1/atlasWide));
				textureCoordinates[currentTex + 3] = texy;
				
				textureCoordinates[currentTex + 4] = texx;
				textureCoordinates[currentTex + 5] = (texy+ ((float)1/atlasHigh));
				
				// Second Triangle
				textureCoordinates[currentTex + 6] = texx+((float)1/atlasWide);
				textureCoordinates[currentTex + 7] = texy;
				
				textureCoordinates[currentTex + 8] = texx+((float)1/atlasWide);
				textureCoordinates[currentTex + 9] = texy+((float)1/atlasHigh);
				
				textureCoordinates[currentTex + 10] = texx;
				textureCoordinates[currentTex + 11] = texy+((float)1/atlasHigh);
				currentTex += 12;
			}
		
		for (short i=0;i<vertices.length;i++)
			indices[i] = i;
		
		setIndices(indices);
		setVertices(vertices);
		setTextureCoordinates(textureCoordinates);
    }

	public int getWidth() {
		return width;
	}

	public int getDepth() {
		return depth;
	}
}
	