package uk.co.gundry.david.androidsimple3d.mesh;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

/**
 * A group is a collection of meshes. Any operation performed on the group should be performed on
 * every mesh in the group.
 */
public class Group extends Mesh {
	private final Vector<Mesh> meshes = new Vector<Mesh>();

	/**
	 * Call draw() on every mesh the group contains
	 */
	@Override
	public void draw(GL10 gl) {
		for (Mesh mesh: meshes)
			mesh.draw(gl);
	}
	
	/**
	 * Add a mesh to the group
	 * @param mesh
	 */
	public boolean add(Mesh mesh) {
		return meshes.add(mesh);
	}

	/**
	 * Get the mesh at the specified index in the group
	 * @param index
	 * @return the element found
	 */
	public Mesh get(int index) {
		return meshes.get(index);
	}

	/**
	 * Removes the mesh at the specified index from the group
	 * @param index
	 * @return the element that was removed
	 */
	public Mesh remove(int index) {
		return meshes.remove(index);
	}

	/**
	 * Removes the first instance of the provided mesh
	 * @param mesh - the mesh to remove
	 * @return true if a mesh was deleted
	 */
	public boolean remove(Mesh mesh) {
		return meshes.remove(mesh);
	}

	/**
	 * @return number of meshes in group
	 */
	public int size() {
		return meshes.size();
	}

}
