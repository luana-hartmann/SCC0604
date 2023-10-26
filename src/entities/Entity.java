package entities;

public abstract class Entity {
    
    /*private means only this class can use the variable*/
    /*protected means only classes that extends this abstract can use this variable*/
    protected float x, y;
    protected int width, height;

    public Entity(float x, float y, int width, int height) {
	this.x = x;
	this.y = y;
        this.width = width;
	this.height = height;
    }
}
