package data;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;


import data.point;
import data.face;




public class object{
	int num_of_points,num_of_faces, num_of_edjes;
	ArrayList points;
	int faces[][];
	boolean formed;
	double max_x=(double)0, max_y=(double)0;
	double min_x=(double)0, min_y=(double)0;

	public int ret_num_faces(){return this.num_of_faces;}
	public int ret_num_points(){return this.num_of_points;}
	public int ret_num_edjes(){return this.num_of_edjes;}
	public ArrayList ret_points(){return this.points;}
	public int[][] ret_faces(){return this.faces;}


	public double ret_max_x(){return this.max_x;}
	public double ret_max_y(){return this.max_y;}
	public double ret_min_x(){return this.min_x;}
	public double ret_min_y(){return this.min_y;}


	double get_x(int i)
	{
		double x = ((point)this.points.get(i)).ret_x();
		double z = ((point)this.points.get(i)).ret_z();
		return conv(x,z);

	}

	double get_y(int i)
	{
		double y = -((point)this.points.get(i)).ret_y();
		double z = -((point)this.points.get(i)).ret_z();
		return conv(y,z);
	}

/**********************************************/
	object(int num_of_points,int num_of_faces,int num_of_edjes, ArrayList points, int[][] faces )
	{
		this.num_of_points=num_of_points;
		this.num_of_faces=num_of_faces;
		this.num_of_edjes=num_of_edjes;
		this.points = points;
		this.faces = faces;
		this.formed =false;
		bounding_box();

	}
/**********************************************/
	public object(String Filename)
	{
		try{
				FileInputStream fis = new FileInputStream(Filename);
				InputStreamReader in = new InputStreamReader((InputStream)fis);
				BufferedReader br = new BufferedReader(in);
				String off =(br.readLine()).trim();
				if(off.equals("OFF"))
				{
					String ln=(br.readLine()).trim();
					ArrayList a = split_string(ln);
					this.num_of_points = Integer.parseInt((String)a.get(0));
					this.num_of_faces = Integer.parseInt((String)a.get(1));
					this.num_of_edjes = Integer.parseInt((String)a.get(2));
					this.points =  new ArrayList();
					this.faces = new int[this.num_of_faces][];
					for(int i=0;i<this.num_of_points;i++)
					{
						String pts = (br.readLine()).trim();
						ArrayList ps = split_string(pts);
						float x = Float.parseFloat((String)ps.get(0));
						float y = Float.parseFloat((String)ps.get(1));
						float z = Float.parseFloat((String)ps.get(2));
						point p = new point(x,y,z);
						//p.print_point();
						this.points.add(p);
					}
					for(int i=0;i<this.num_of_faces;i++)
					{
						String fcs= (br.readLine()).trim();
						ArrayList fs = split_string(fcs);
						int np =  Integer.parseInt((String)fs.get(0));
						this.faces[i]= new int[np+1];
						this.faces[i][0]=np;
						//System.out.println(this.faces[i][0]);
						for(int j=1;j<=np;j++)
						{
						 	this.faces[i][j]=Integer.parseInt((String)fs.get(j));
						// 	System.out.print(this.faces[i][j]+ " ");

						}
						//System.out.println();
					}
					this.formed =true;
					//subdivide();
					bounding_box();

				}
				else
				{
					System.out.println("not an off file..");
					this.formed =false;
				}
			}
		catch(Exception e){	e.printStackTrace();}
	}
/**********************************************/
public double conv(double x1,double z1)
{
	double pi = 3.1415926535;
	double  rad  = (float)(pi/(float)4);
	return (double )((double )x1+((double )z1*(double )Math.cos(rad)));

}

void bounding_box()
{
	for(int i=0;i< this.num_of_points;i++)
	{
		double tempx =  conv(((point)this.points.get(i)).ret_x(),((point)this.points.get(i)).ret_z());
		double tempy =  conv(((point)this.points.get(i)).ret_y(),((point)this.points.get(i)).ret_z());
		if(tempx >max_x) max_x =  tempx;
		if(tempy >max_y) max_y =  tempy;
		if(tempx <min_x) min_x =  tempx;
		if(tempy <min_y) min_y =  tempy;
	}
	//System.out.println("\n" + max_x + "\t" + max_y + "\t" + min_x + "\t" + min_y + "\n");

}



/**********************************************/
	private ArrayList split_string(String s)
	{
		ArrayList sub = new ArrayList();
		//System.out.println(s);
		int start_pos =s.indexOf(" ");
		//System.out.println(s);
		//System.out.println(start_pos);
		while(start_pos !=-1)
		{
			String str = s.substring(0,start_pos).trim();
			if(sub.add(str));//System.out.println(str);
			s = (s.substring(start_pos)).trim();
			start_pos =s.indexOf(" ");
			//System.out.println(s);
			//System.out.println(start_pos);
		}
		if(sub.add(s));//System.out.println(s);
		return  sub;
	}

/**********************************************/
	face form_face(int pos)
	{
		face f = new face();
		//System.out.println(pos);
		for(int i=1;i<=this.faces[pos][0];i++)
		{
			f.add_point((point)this.points.get(this.faces[pos][i]));
		}
		f.add_point((point)this.points.get(this.faces[pos][1]));
		return f;
	}
/**********************************************/

	public void edjenumber()
	{
		int num=0;
		int arr[][]= new int[this.num_of_points][this.num_of_points];
		for(int i=0;i<this.num_of_points;i++)
		for(int j=0;j<this.num_of_points;j++)arr[i][j]=0;

		for(int i=0;i<this.num_of_faces;i++)
		{

			for(int j=1;j<this.faces[i][0];j++)
			{

				int pos1=this.faces[i][j];
				int pos2=this.faces[i][j+1];
				//System.out.println(pos1+"  "+pos2);
				arr[pos1][pos2]=1;
				if(arr[pos2][pos1]==0)
				{
					num++;
				}

			}

			int pos2=faces[i][1];
			int pos1=faces[i][this.faces[i][0]];
			//System.out.println(pos1+"  "+pos2);
			arr[pos1][pos2]=1;
			if(arr[pos2][pos1]==0)
			{
				num++;
			}

		}
		this.num_of_edjes =  num;
		System.out.println("edjes: "+num);


	}






	/**********************************************/
	//find face for edje p-q
	int  find_face(int p, int q)
	{
		boolean got = false;
		for(int i=0;i<this.num_of_faces;i++)
		{
			for(int j=1;j<this.faces[i][0];j++)
			{
				if(this.faces[i][j]==p && this.faces[i][j+1]==q)return i;
			}
			if(this.faces[i][this.faces[i][0]]==p && this.faces[i][1]==q)return i;

		}
		return -1;
	}

	int  get_valence(int p)
	{
		int ret =0;
		for(int i=0;i<this.num_of_faces;i++)
		{
			for(int j=1;j<=4;j++)
			{
				if(this.faces[i][j]==p )ret++;
			}
		}
		return ret;
	}


	//find postions for edje pq...
	//returns an array of faces pos and point post...
	int[] find_point_pos(int p, int q)
	{
		int ret[]=new int[2];
		ret[0]=-1;
		ret[1]=-1;
		for(int i=0;i<this.num_of_faces;i++)
		{
			for(int j=1;j<this.faces[i][0];j++)
			{
				if(this.faces[i][j]==p && this.faces[i][j+1]==q)
				{
					//face number
					ret[0]=i;
					//index of points...
					ret[1]=j;
				}
			}
			if(this.faces[i][this.faces[i][0]]==p && this.faces[i][1]==q)
			{
				ret[0]=i;
				ret[1]=this.faces[i][0];
			}
		}
		return ret;

	}

/**********************************************/

	public object cat_clark()
	{

		int[][] edjes =  new int[this.num_of_faces][];
		//int[][] vertex = new int[this.num_of_points][2];
		//edje points index...
		int num = 0,cnt=0;
		//stores the new points generated...
		ArrayList newpoints =  new ArrayList();
		//stores the new face indices....
		int[][] newfaces;
		//get face points and store is array list...
		for(int i=0;i< this.num_of_faces;i++)
		{
			face f = form_face(i);
			point p = f.calc_centroid();
			if(p!=null) newpoints.add(p);
			edjes[i] = new int[this.faces[i][0]+1];
			for(int j=0;j<this.faces[i][0]+1;j++)edjes[i][j]=-1;
		}

		//get edje points...
		for(int i=0;i< this.num_of_faces;i++)
		{
			point p = (point)newpoints.get(i);
			face f = form_face(i);
			for(int j=1;j<this.faces[i][0];j++)
			{
				if(edjes[i][j]==-1)
				{
					int pos1 = this.faces[i][j];
					int pos2 = this.faces[i][j+1];
					//System.out.println(pos1 +"\t"+pos2);

					//get position for opposite edje....
					int pos[]=find_point_pos(pos2,pos1);
					//System.out.println(pos[0] +"\t"+pos[1]);
					point q = (point)newpoints.get(pos[0]);
					point r = f.edje_center(j-1);
					face eface = new face();
					eface.add_point(p);
					eface.add_point(q);
					eface.add_point(r);
					point epoint=eface.calc_centroid();
					newpoints.add(epoint);
					edjes[i][j]=newpoints.indexOf(epoint);
					edjes[pos[0]][pos[1]]=newpoints.indexOf(epoint);
					//System.out.println(newpoints.indexOf(epoint));
				}

			}
			if(edjes[i][this.faces[i][0]]==-1)
			{
				int pos1 = this.faces[i][this.faces[i][0]];
				int pos2 = this.faces[i][1];
				//System.out.println(pos1 +"\t"+pos2);
				int pos[]=find_point_pos(pos2,pos1);
				//System.out.println(pos[0] +"\t"+pos[1]);
				point q = (point)newpoints.get(pos[0]);
				point r = f.edje_center(this.faces[i][0]-1);
				face eface = new face();
				eface.add_point(p);
				eface.add_point(q);
				eface.add_point(r);
				point epoint=eface.calc_centroid();
				newpoints.add(epoint);
				edjes[i][this.faces[i][0]]=newpoints.indexOf(epoint);
				edjes[pos[0]][pos[1]]=newpoints.indexOf(epoint);
				//System.out.println(newpoints.indexOf(epoint));
			}
		}
		/*for(int i=0;i<this.num_of_faces;i++)
		{
			for(int j=1;j<=this.faces[i][0];j++)System.out.print(edjes[i][j]+"   ");
			System.out.println();
		}*/


		//get vertext points...
		for(int p = 0;p<this.num_of_points;p++)
		{

			double fx=(double)0;
			double fy=(double)0;
			double fz=(double)0;
			double ex=(double)0;
			double ey=(double)0;
			double ez=(double)0;

			//get faces....
			cnt=0;
			//System.out.println("Vertex\t" + p + "\n");
			for(int i=0;i< this.num_of_faces;i++)
			{

				for(int j=1;j<this.faces[i][0];j++)
				{
						if (this.faces[i][j]==p)
						{
							cnt ++;
					//		System.out.println("face\t"+ i);
							fx = fx + ((point)newpoints.get(i)).ret_x();
							fy = fy + ((point)newpoints.get(i)).ret_y();
							fz = fz + ((point)newpoints.get(i)).ret_z();
						//	((point)newpoints.get(i)).print_point();
							ex = ex + ((point)points.get(p)).center((point)points.get(this.faces[i][j+1])).ret_x();
							ey = ey + ((point)points.get(p)).center((point)points.get(this.faces[i][j+1])).ret_y();
							ez = ez + ((point)points.get(p)).center((point)points.get(this.faces[i][j+1])).ret_z();
						//	((point)points.get(p)).center((point)points.get(this.faces[i][j+1])).print_point();
							break;
						}
				}
				if (this.faces[i][this.faces[i][0]]==p)
				{
					cnt ++;
					//System.out.println("face\t"+ i);
					fx = fx + ((point)newpoints.get(i)).ret_x();
					fy = fy + ((point)newpoints.get(i)).ret_y();
					fz = fz + ((point)newpoints.get(i)).ret_z();
					//((point)newpoints.get(i)).print_point();
					ex = ex + ((point)points.get(p)).center((point)points.get(this.faces[i][1])).ret_x();
					ey = ey + ((point)points.get(p)).center((point)points.get(this.faces[i][1])).ret_y();
					ez = ez + ((point)points.get(p)).center((point)points.get(this.faces[i][1])).ret_z();
					//((point)points.get(p)).center((point)points.get(this.faces[i][0])).print_point();
				}

			}
			ex = (double)2* ex/(double)cnt;
			ey = (double)2* ey/(double)cnt;
			ez = (double)2* ez/(double)cnt;

			fx = fx/(double)cnt;
			fy = fy/(double)cnt;
			fz = fz/(double)cnt;
			System.out.println(cnt);
			double v_x = (fx + ex + ((cnt -3)*((point)points.get(p)).ret_x()))/cnt;
			double v_y = (fy + ey + ((cnt -3)*((point)points.get(p)).ret_y()))/cnt;
			double v_z = (fz + ez + ((cnt -3)*((point)points.get(p)).ret_z()))/cnt;
			newpoints.add(new point(v_x,v_y,v_z));

		}
		//for new number of faces....
		int fnum=0;
		for(int i=0; i<this.num_of_faces;i++)
		{
			fnum = fnum + this.faces[i][0];
		}
		//System.out.println(fnum);
		newfaces = new int[fnum][5];
		edjenumber();

		int faceindex=0;
		for(int i = 0;i<this.num_of_faces;i++)
		{

			for(int j=1;j<this.faces[i][0];j++)
			//for(int j=1;j<;j++)
			{
				//face point...
				newfaces[faceindex][0]=4;
				newfaces[faceindex][1]=i;
				newfaces[faceindex][2]=edjes[i][j];
				newfaces[faceindex][3]=this.faces[i][j+1]+this.num_of_faces+ this.num_of_edjes;
				newfaces[faceindex][4]=edjes[i][j+1];
				faceindex++;
			}
			newfaces[faceindex][0]=4;
			newfaces[faceindex][1]=i;
			newfaces[faceindex][2]=edjes[i][this.faces[i][0]];
			newfaces[faceindex][3]=this.faces[i][1]+this.num_of_faces+ this.num_of_edjes;
			newfaces[faceindex][4]=edjes[i][1];
			faceindex++;
		}
		object ret = new object(newpoints.size(),fnum,0,newpoints,newfaces);
		return ret;
	}

/**********************************************/

	public void save(String Filename)
	{
		//save into off file....
		if(this.formed)System.out.println("already saved...");
		else
		{
			try{
				FileOutputStream fos = new FileOutputStream (Filename);

				PrintStream pr =new PrintStream((OutputStream)fos);
				edjenumber();
				pr.println("OFF");
				pr.print(this.num_of_points +" " + this.num_of_faces + " " + this.num_of_edjes);
				pr.println();
				for(int i=0;i<this.num_of_points;i++)
				{
					double x = ((point)this.points.get(i)).ret_x();
					double y = ((point)this.points.get(i)).ret_y();
					double z = ((point)this.points.get(i)).ret_z();
					pr.println(x +" " + y + " " + z);
				}
				for(int i=0;i<this.num_of_faces;i++)
				{

					for(int j=0;j<=this.faces[i][0];j++)
					{
						pr.print( this.faces[i][j] +" ");
					}
					pr.println();
				}
				pr.close();
				this.formed =true;
			}

			catch(Exception e){e.printStackTrace();}
		}

	}

/**********************************************/
	public void plot_object(Graphics2D g2)
	{
		//boundingbox();
		double min;
		double trans  = (double)300;
		if((max_x -min_x)>(max_y -min_y)) min =(max_x -min_x);
		else min =(max_y -min_y);
		g2.setPaint(Color.black);
		g2.translate(300,300);
		//g2.rotate(3.1415926535);
		double scale = (double)200/min;
		for(int i=0;i<this.num_of_faces;i++)
		{
			for(int j=1;j<this.faces[i][0];j++)
			{
				//double x1 = trans + get_x(this.faces[i][j])* scale;
				//double y1 = get_y(this.faces[i][j])* scale + trans;
				//double x2 = trans + get_x(this.faces[i][j+1])* scale;
				//double y2 = get_y(this.faces[i][j+1])* scale + trans;
				double x1 = get_x(this.faces[i][j])* scale;
				double y1 = get_y(this.faces[i][j])* scale;
				double x2 = get_x(this.faces[i][j+1])* scale;
				double y2 = get_y(this.faces[i][j+1])* scale;
				Line2D l =  new Line2D.Double(x1,y1,x2,y2);
				g2.draw(l);
			}
			double x1 = get_x(this.faces[i][this.faces[i][0]]) * scale;
			double y1 = get_y(this.faces[i][this.faces[i][0]])* scale ;
			double x2 = get_x(this.faces[i][1])* scale;
			double y2 = get_y(this.faces[i][1])* scale;
			Line2D l =  new Line2D.Double(x1,y1,x2,y2);
			g2.draw(l);

		}

	}
	/**********************************************/


	public static void main(String args[]) throws Exception
	{
		String Filename1 = args[0];
		String s = " 2                  0                1";
		final object o = new object(Filename1);
		ArrayList arr = o.split_string(s);
		//for(int i = 0;i<arr.size();i++)
		//{
		//	System.out.println((String)arr.get(i));
		//}
		object newobject=o.cat_clark();
		newobject.save("cube1.off");


	}
	/**********************************************/
}
