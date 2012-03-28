package uk.co.gundry.david.androidsimple3d;

import javax.microedition.khronos.opengles.GL10;

/**
 * Details about a theoretical camera, including position, orientation and velocity
 */
public class Camera {

	/**
	 * Field of vision in the y direction.
	 */
	private float fovy = 45f;
	/**
	 * Field of vision in the x direction, relative to fovy
	 */
	private float aspect;
	
	private float xpos = 0;
	private float ypos = 0;
	private float zpos = 0;
	private float xrot = 0;
	private float yrot = 0;
	
	float moveSpeed = 0.02f;
	float lookSpeed = 0.1f;

	private float forwardVel;
	private float strafeVel;
	
	/**
	 * Move camera (or, more accurately, scene around camera)
	 * @param gl
	 */
	void update(GL10 gl)
	{			
		float fv = forwardVel * moveSpeed;
		float sv = strafeVel * moveSpeed;
		
	    float yrotrad = (yrot / 180 * 3.141592654f);
		xpos += fv*(Math.sin(yrotrad));
		zpos -= fv*(Math.cos(yrotrad));
		
		yrotrad += (3.141592654f/2);
		xpos -= sv*(Math.sin(yrotrad));
		zpos += sv*(Math.cos(yrotrad));
		
	    gl.glRotatef(xrot,1.0f,0.0f,0.0f); 
	    gl.glRotatef(yrot,0.0f,1.0f,0.0f);
	    gl.glTranslatef(-xpos,-ypos,-zpos);
	}


	/**
	 * Set the camera's position coordinates to those specified
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPos(float x, float y, float z)
	{
		this.xpos = x;
		this.ypos = y;
		this.zpos = z;
	}
	
	public float getXpos() {
		return xpos;
	}

	public void setXpos(float xpos) {
		this.xpos = xpos;
	}

	public float getYpos() {
		return ypos;
	}

	public void setYpos(float ypos) {
		this.ypos = ypos;
	}

	public float getZpos() {
		return zpos;
	}

	public void setZpos(float zpos) {
		this.zpos = zpos;
	}

	/**
	 * Rotate the camera around the x axis.
	 * This will be multiplied by the camera's look speed.
	 * @param xrot
	 */
	public void rotX(float xrot)
	{
		this.xrot += xrot*lookSpeed;
	}
	
	/**
	 * Rotate the camera around the y axis.
	 * This will be multiplied by the camera's look speed.
	 * @param yrot
	 */
	public void rotY(float yrot)
	{
		this.yrot += yrot*lookSpeed;
	}
	
	public float getXrot() {
		return xrot;
	}

	public void setXrot(float xrot) {
		this.xrot = xrot;		
	}

	public float getYrot() {
		return yrot;
	}

	public void setYrot(float yrot) {
		this.yrot = yrot;
	}
	
	public void setVel(float forwardVel, float strafeVel)
	{
		this.forwardVel = forwardVel;
		this.strafeVel = strafeVel;
	}
	
	public float getForwardVel() {
		return forwardVel;
	}

	public void setForwardVel(float forwardVel) {
		this.forwardVel = forwardVel;
	}

	public float getStrafeVel() {
		return strafeVel;
	}

	public void setStrafeVel(float strafeVel) {
		this.strafeVel = strafeVel;
	}


	public float getFovy() {
		return fovy;
	}


	public void setFovy(float fovy) {
		this.fovy = fovy;
	}


	public float getAspect() {
		return aspect;
	}


	public void setAspect(float aspect) {
		this.aspect = aspect;
	}

}
