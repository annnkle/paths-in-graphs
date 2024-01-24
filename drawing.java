package paths_in_graphs;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;


import javafx.event.ActionEvent;

import java.math.*;
import java.util.Random;
import java.util.Vector;

public class drawing extends JFrame{
	static int i = 0;	
	static int n = 3; //number of vertices
	static int eu_ha_graph_mode = 0; 
	static int adjacency_matrix[][] = new int[n][n];
	static Vector<vertex> vertices = new Vector<vertex>();
	static Vector<edge> edges = new Vector<edge>();
	// for euler cycle
	static Vector<edge> visited = new Vector<edge>(); 
	
	public static void main(String[] args) {
		generator();
		new drawing();
	}
		
	private static void generator() {
		//create n vertices
		for(i = 0; i < n; i++) {
			vertex v = new vertex();
			v.numer = i;		
			vertices.add(v);
		}
		
		//create edges A--------------B
		int genB = 0;
		int genA = eulohamil_rulez();
		while(genA!=-1) {
			while(!check_for_loops_n_duplicates(genA, genB)) {
				// get random vertex
				genB = randomNumber(0, (n-1));
			}
			edge e = new edge();
			vertex a = vertices.get(genA);
			vertex b = vertices.get(genB);
			e.a = a;
			e.b = b;
			a.degree+=1;
			b.degree+=1;
			a.neighbors.add(b);
			b.neighbors.add(a);
			edges.add(e);
			genA = eulohamil_rulez();
		}
		// populate adjacency matrix - used for finding Hamilton cycle
		int j;
		for(i = 0; i < n; i++) {
			vertex temp = vertices.get(i);
			for(j = 0; j<temp.neighbors.size(); j++) {
				vertex temp2 = temp.neighbors.get(j);
				adjacency_matrix[temp.numer][temp2.numer] = 1;
			}
		}		
	}
	
	
	protected static boolean check_for_loops_n_duplicates(int a, int b) {
		if(a == b) { //edge cannot be a loop
			return false;
		}
		//checking if edge does not exist yet
		for(i = 0;i < edges.size();i++) {
			if(edges.get(i).a.numer == a && edges.get(i).b.numer == b || edges.get(i).a.numer == b && edges.get(i).b.numer == a) {
				return false;
			}
		}
		return true;
	}
			
	
	protected static int eulohamil_rulez() {
		//ensuring a hamiltonian graph per Dirac's theorem
		//per Dirac's theorem all vertices must have degrees of least n/2, but here I used n/2+1 
		//to differentiate from eulerian graph
		int eh = (n/2)+1;
		if(eu_ha_graph_mode == 0) {
			for(i = 0;i < vertices.size();i++) {
				if(vertices.get(i).degree < (int)eh) {
					return i;
				}
			}
		}
		
		//ensuring an eulerian graph - all vertices must have even degrees
		else if(eu_ha_graph_mode == 1) {
			
			int oddDegreeCount = 0;
			
			for(i = 0;i < vertices.size();i++) {
				if(vertices.get(i).degree%2 != 0) {
					oddDegreeCount++;
				}
				if(vertices.get(i).degree<1 || oddDegreeCount > 2) {
					return i;
				}
			}
		}
		return -1;
	}
			
	private static int randomNumber(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max musi byc wiekszy od min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	//drawing the frame
	public drawing(){  
		
		JFrame f= new JFrame("Graph");  
		//Image icon = Toolkit.getDefaultToolkit().getImage("D:\\Grafy\\icon.jpg");    
		//f.setIconImage(icon);    
		f.setSize(800, 780);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel p = new JPanel();
		JPanel p1 = new JPanel();
		
		MyCanvas panelCanvas = new MyCanvas();		   
		p.add(panelCanvas);  

		//toggle graph generator mode (hamiltonian/eulerian)
		JToggleButton euha = new JToggleButton("HAMILTON"); 
		euha.addActionListener(new ActionListener(){  
			public void actionPerformed(java.awt.event.ActionEvent e) {
				 if (euha.isSelected()) {
					 euha.setText("EULER");  
					 eu_ha_graph_mode = 1;
				 }
					else {
					 euha.setText("HAMILTON");  
					 eu_ha_graph_mode = 0;
					}		
			}
			});
		euha.setSize(100, 70);
			
		//LABELS
		JLabel en = new JLabel("n");
		en.setForeground(Color.WHITE);
		JLabel end = new JLabel("EDGE LIST");
		end.setForeground(Color.WHITE);
		

		JTextField txt = new JTextField(10);
		JTextArea ta1 = new JTextArea(5, 10);
		JTextArea ta2 = new JTextArea(5, 10);
		JTextArea ta3 = new JTextArea(12, 10);
		txt.setEditable(true);
		ta1.setEditable(false);
		ta2.setEditable(false);
		ta3.setEditable(false);
		JButton go = new JButton("SET");
		JButton euler = new JButton("EULER CYCLE/PATH");
		JButton hamilton= new JButton("HAMILTON CYCLE");
		JScrollPane sc = new JScrollPane(ta1);
		JScrollPane sc2 = new JScrollPane(ta2);
		    
		//finding euler cycle/path using Fleury's algorithm
		euler.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				euler_cycle();		    		
			}		
				private void euler_cycle() 
				{ 
					if(isEulerianGraph()) {
						Vector<vertex> tempVertices = vertices;
						int u = 0; 
						for (int i = 0; i < tempVertices.size(); i++) 
						{ 
							vertex v = tempVertices.get(i);
							if (v.neighbors.size() % 2 == 1) 
							{ 
								u = i; 
								break; 
							} 
						}      
						eulerCycleToList(u, tempVertices); 
						visited.clear();
					}
					else {
						ta1.append("No Euler cycle/path in graph");
					}
				} 
				  
				private void eulerCycleToList(int u, Vector<vertex> tempVertices) 
				{  
					for (int i = 0; i < tempVertices.get(u).neighbors.size(); i++) 
					{
						int v = tempVertices.get(u).neighbors.get(i).numer; 
						//if edge passes check add to list
						if (edgeCheck(u, v, tempVertices))  
						{ 
							ta1.append(String.valueOf(u) + "-" + String.valueOf(v) + " ");	                  
							edge e = new edge();
							e.a = tempVertices.get(u);
							e.b = tempVertices.get(v);
							visited.add(e);
							// After removing edge delete vertices from neighbor list
							removeFromNeighborList(tempVertices.get(u), v);
							removeFromNeighborList(tempVertices.get(v), u);
							eulerCycleToList(v, tempVertices); 
						} 
					} 
				} 
				  
				
				private boolean edgeCheck(int u, int v, Vector<vertex> tempVertices) 
				{
					if(isEdgeVisited(u, v)) {
						return false;
					}
				  
					if (tempVertices.get(u).neighbors.size() == 1) { 
						return true; 
					} 
					// DFS from vertex u 
					boolean[] isVisited = new boolean[n]; 
					int count1 = DFScounter(u, isVisited, tempVertices);   
					edge e = new edge();
					e.a = tempVertices.get(u);
					e.b = tempVertices.get(v);
					visited.add(e);
					
					// Check if after marking an edge as visited the graph is still complete
					isVisited = new boolean[n]; 
					int count2 = DFScounter(u, isVisited, tempVertices); 
			  
					
					// Remove edge from visited to avoid duplication
					visited.remove(e);
					//
					return (count1 > count2) ? false : true; 
				} 
					
				private int DFScounter(int v, boolean[] isVisited, Vector<vertex> tempVertices) 
				{ 
				 
					isVisited[v] = true; 
					int count = 1; 
					// for every vertex adjacent to v
					for (vertex adj : tempVertices.get(v).neighbors) 
					{ 	
						// if adjacent vertex was not visited and edge of v and adjacent wasn't either, count it
						if (!isVisited[adj.numer] && !isEdgeVisited(v, adj.numer) ) 
						{ 
							count = count + DFScounter(adj.numer, isVisited, tempVertices); 
						} 
					} 
					return count; 
				}
				    
				private boolean isEdgeVisited(int a, int b) {
					// Loop through all visited edges and check if given edge is on the list
					for(edge ed: visited) {
						if((ed.a.numer == a && ed.b.numer == b) || (ed.a.numer == b && ed.b.numer == a)) {
							return true;
						}
					}
					return false;
				}
				
				private void removeFromNeighborList(vertex v,int numerSasiada) {
					for(vertex adj: v.neighbors) {
						if(adj.numer == numerSasiada) {
							v.neighbors.remove(adj);
							break;
						}
					}
				}    
			
		});
		    
		hamilton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				hamilton_cycle(adjacency_matrix);
			}	  			
				boolean isSafe(int v, int adjacency_matrix[][], int path[], int pos) 
				{   
					//check if chcecked v is not adjacent to previous vertex in path
					if (adjacency_matrix[path[pos - 1]][v] == 0) 
						return false; 
					//check if v is already on path
					for (int i = 0; i < pos; i++) 
						if (path[i] == v) 
							return false; 
			  
					return true; 
				} 
			  
			 
				boolean hamiltonToList(int adjacency_matrix[][], int path[], int pos) 
				{ 
					if (pos == n) 
					{ 
						if (adjacency_matrix[path[pos - 1]][path[0]] == 1) 
							//there is a cycle
							return true; 
						else
							//there is no cycle
							return false; 
					} 
					for (int v = 1; v < n; v++) 
					{ 
						if (isSafe(v, adjacency_matrix, path, pos)) 
						{ 
							path[pos] = v; 
							if (hamiltonToList(adjacency_matrix, path, pos + 1) == true) {
								return true; 
							}
							path[pos] = -1; 
						} 
					} 
					return false; 
				} 

				int hamilton_cycle(int adjacency_matrix[][]) 
				{ 
					int[] path = new int[n]; 
					for (int i = 0; i < n; i++) 
						path[i] = -1; 
			  
					path[0] = 0; // starting from vertex 0 
					// recursion
					if (hamiltonToList(adjacency_matrix, path, 1) == false) 
					{ 
						String s = "No Hamilton cycle in graph";
						ta2.append(s);
						return 0; 
					} 
			  
					for(i = 0; i < n; i++) {
						String s = String.valueOf(path[i]) + ", ";
						ta2.append(s);
						
					}

					return 1; 
				} 
			});
		    DefaultListModel<String> l1 = new DefaultListModel<>();  

		    for(i = 0; i < edges.size();i++) {
		    	String s = String.valueOf(edges.get(i).a.numer) + " " + String.valueOf(edges.get(i).b.numer) + "\n";
		    	l1.addElement(s);
		    } 
		    JList<String> list = new JList<>(l1);  
		    JScrollPane sc3 = new JScrollPane(list);
////////////////////////////////////////////////////////////////////////////////////////////////////////    ////////////////////////////////////////////////////    ////////////////////////////////////////////////////    
		
/////////////////////////////////////////////////////////////////////////////////////////////////////////    ////////////////////////////////////////////////////    ////////////////////////////////////////////////////    
		    
		    go.addActionListener(new ActionListener(){  
				public void actionPerformed(java.awt.event.ActionEvent e) {
					n = Integer.valueOf(txt.getText());
					adjacency_matrix = new int [n][n];
					
					vertices.clear();
					edges.clear();
					generator();
					
					
					panelCanvas.repaint();
					ta1.setText(null);
					ta2.setText(null);
					l1.clear();
					list.repaint();
					
					for(i = 0; i < edges.size();i++) {
				    	String s = String.valueOf(edges.get(i).a.numer) + " " + String.valueOf(edges.get(i).b.numer) + "\n";
				    	l1.addElement(s);
				    } 
					
				}
		    	});  
		   
		     
////////////////////////////////////////////////////////////////////////////////////////////////////////    ////////////////////////////////////////////////////    ////////////////////////////////////////////////////    
		    
		    JSeparator s1 = new JSeparator();
		    JSeparator s2 = new JSeparator();
		    JSeparator s3 = new JSeparator();
////////////////////////////////////////////////////////////////////////////////////////////////////////    ////////////////////////////////////////////////////    ////////////////////////////////////////////////////  
		   
		    
		// Highlighting graph edges
		list.addMouseListener(new MouseAdapter(){
			  //@Override
			  public void mouseClicked(MouseEvent e) {
		   
				  String s = (String) list.getSelectedValue();
			   
				  String[] tokens = s.split(" ");

				  int x = Integer.valueOf(tokens[0]);
				  int y = Integer.valueOf(tokens[1].substring(0, tokens[1].length() - 1));
				  
				  System.out.println(String.valueOf(x) + String.valueOf(y));
				  
				  vertex v1 = vertices.get(x);
				  vertex v2 = vertices.get(y);
				  
				  
				  Graphics g = panelCanvas.getGraphics();
				  
				  int delay = 1000;
				  
				  ActionListener drawWhiteLine = new ActionListener() {

						@Override
						public void actionPerformed(java.awt.event.ActionEvent e) {
							Graphics g = panelCanvas.getGraphics();
							g.setColor(Color.WHITE);
							g.drawLine(v1.x+5, v1.y+5, v2.x+5, v2.y+5);
						}
				  };
				  
				  javax.swing.Timer timer = new Timer(1000, drawWhiteLine); 
				  
				  g.setColor(Color.MAGENTA);
				  g.drawLine(v1.x+5, v1.y+5, v2.x+5, v2.y+5);
				  
				  timer.start();	
				  
				
			  }
		});
		  
		p1.add(euha);p1.add(en);p1.add(txt);p1.add(go);p1.add(s1); p1.add(euler); p1.add(sc); p1.add(s2);p1.add(hamilton);p1.add(sc2);p1.add(s3);p1.add(end);p1.add(sc3); 
		p1.setBackground(new java.awt.Color(13, 28, 40));
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));  
		f.getContentPane().setBackground(new java.awt.Color(13, 28, 40));
		f.add(p);f.add(p1);
		f.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		
		f.setVisible(true);  
	}  
	class MyCanvas extends Canvas{  
		public MyCanvas() {  
		setBackground (Color.DARK_GRAY);  
		setSize(550, 700);     
		}  

		//drawing the graph
		public void paint(Graphics g){  
			g.setColor(Color.WHITE);  
			
			//vertices are distributed on two planes
			double evenDistribution = 600/(Math.floor(n/2));
			double oddDistribution = 600/(Math.floor(n/2)-1);
			int yy = 30;
			int yyy = 30;
			for(i = 0;i < n; i++) {
				String s=String.valueOf(i);
				vertex v = vertices.get(i);
				
				if(i%2==0) {
					v.x = 50;
					v.y = yy;
					g.drawString(s,(v.x-14), (v.y+10));	
					yy += evenDistribution;
				}
				else {
					// starting point
					v.x = 450;
					v.y = yyy;
					g.drawString(s,(v.x+12), (v.y+10));	
					yyy += oddDistribution;	
				}
				
				g.fillOval(v.x, v.y, 10, 10);
			}
			for(i =0; i < edges.size(); i++) {
				g.drawLine((edges.get(i).a.x+5), (edges.get(i).a.y+5),(edges.get(i).b.x+5),(edges.get(i).b.y+5));
			}
		}
	}
		
	private boolean isEulerianGraph() {
		// checks whether graph is eulerian
		int oddDegreeCount = 0;
		for(vertex v: vertices) {
			if(v.neighbors.size() % 2 != 0) {
				oddDegreeCount ++;
			}
		}
		if(oddDegreeCount < 3) {
			return true;
		}
		return false;
	}
}