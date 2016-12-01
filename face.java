package data;


import java.util.ArrayList;

import data.point;

public class face{

	// list ot store the points
	ArrayList points;
	point centroid;

	//contructor to intialize the object
	public face(){	points =  new ArrayList();}

	//returns the number  of points
	public int ret_num(){return this.points.size()-1;}

	//returns the  point of current index..
	public point  ret_point(int index){return (point)this.points.get(index);}

	//
	public void add_point(point p)
	{
		boolean contains= false;
		for(int i=0;i<this.points.size();i++)
		{
			contains= p.equal((point)this.points.get(i));
			if(contains)break;
		}
		if(!contains)points.add(p);
	}

	//
	public point calc_centroid()
	{
		if(this.points.size()>2)
		{
			double x,y,z;
			x=(double)0;
			y=(double)0;
			z=(double)0;
			for(int i=0;i<this.points.size();i++)
			{
				x = x + ((point)this.points.get(i)).ret_x();
				y = y + ((point)this.points.get(i)).ret_y();
				z = z + ((point)this.points.get(i)).ret_z();
			}

			x=x/((double)this.points.size());
			y=y/((double)this.points.size());
			z=z/((double)this.points.size());
			this.centroid = new point(x,y,z);
			return this.centroid;

		}
		else
		{
			System.out.println(this.points.size()+ "  not enuf points in the face...");
			return null;
		}

	}

	public point ret_centroid(){ return this.centroid;}

	// check if point is in face...
	public boolean find_point(point p){return this.points.contains(p);}



	//for doo sabin..
	public point get_face_point(int pos)
	{
		point ret = null;
		if(pos<points.size())
		{
			if(this.centroid == null) this.calc_centroid();
			face f = new face();
			f.add_point((point)this.points.get(pos));
			f.add_point(edje_center(pos));
			f.add_point(this.centroid);
			if(pos==0)f.add_point(edje_center(points.size()-1));
			else f.add_point(edje_center(pos-1));
			ret =  f.calc_centroid();
		}
		return ret;
	}



	// get new face from current face
	public face get_face()
	{
		calc_centroid();
		face f =  new face();
		for(int i = 0; i<this.points.size();i++)
		{
			point p =  this.get_face_point(i);
			f.add_point(p);
		}

		//f.calc_centroid();
		return f;
	}

	public boolean equal(face f)
	{
		boolean ret= false;
		int count=0;
		if(this.points.size() == f.ret_num())
		{
			for(int i=0;i<this.points.size();i++)
			{
				point p = (point)this.points.get(i);
				for(int j=0;j<f.ret_num();j++)
				{
					point q = (point)f.ret_point(j);
					if(p.equal(q))
					{
						count++;
						break;
					}
				}
			}

		}
		if(count == this.points.size())ret = true;
		return ret;
	}
	public void print_face()
	{
		for(int i=0;i<this.points.size();i++)
		{
			((point)points.get(i)).print_point();
		}
	}


	public point edje_center(int i)
	{
		point ret = null;
		if(i<points.size())
		{
			int j;
			if(i==points.size()-1)j=0;
			else j=i+1;


			double x = ( ((point)points.get(i)).ret_x() + ((point)points.get(j)).ret_x() )/(double)2;
			double y = ( ((point)points.get(i)).ret_y() + ((point)points.get(j)).ret_y() )/(double)2;
			double z = ( ((point)points.get(i)).ret_z() + ((point)points.get(j)).ret_z() )/(double)2;
			ret= new point(x,y,z);
		}
			return ret;
	}

	public static void main(String args[])
	{
		point p= new point((double)0,(double)0,(double)0);
		face f = new face();
		f.add_point(p);
		p= new point((double)2,(double)0,(double)0);
		f.add_point(p);
		p= new point((double)2,(double)2,(double)0);
		f.add_point(p);
		p= new point((double)0,(double)2,(double)0);
		f.add_point(p);
		p= new point((double)0,(double)0,(double)0);
		f.add_point(p);
		p= f.calc_centroid();
		//p.print_point();
		//((point)f.get_face_point(4)).print_point();
		f = f.get_face();
		f.print_face();
		f = f.get_face();
		f.print_face();
	}


}
