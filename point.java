package data;
import java.util.ArrayList;


public class point{

	double x,y,z;

	public point(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public point center( point p)
	{
		return new point((this.x + p.ret_x())/2,(this.y + p.ret_y())/2,(this.z + p.ret_z())/2);
	}

	public double ret_x(){return this.x;}
	public double ret_y(){return this.y;}
	public double ret_z(){return this.z;}
	public void print_point()
	{
		System.out.println(this.x +" "+ this.y +" "+ this.z);
	}

	public boolean equal(point p)
	{
		boolean ret = false;
		if(this.x == p.ret_x() && this.y == p.ret_y() && this.z == p.ret_z()) ret = true;
		return ret;
	}
	public static void main(String args[])
	{
		point p1 = new point((double)0,(double)0,(double)0);
		point p2 = new point((double)4,(double)0,(double)0);
		point p3 = new point((double)0,(double)0,(double)0);
		point p4 = new point((double)0,(double)4,(double)0);
		if(p1.equal(p2)){System.out.println("p2 -p1");p2.print_point();}
		if(p1.equal(p3)){System.out.println("p3-p1");p3.print_point();}
		if(p4.equal(p2)){System.out.println("p2-p4");p2.print_point();}


	}
}