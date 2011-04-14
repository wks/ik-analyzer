/**
 * 
 */
package org.wltea.analyzer.cube;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.wltea.analyzer.Lexeme;

/**
 * @author linliangyi
 *
 */
public class HCube {
	
	private HashMap<Integer , List<Lexeme>> dataList;

	private int left = -1;
	
	private int right = -1;
	
	private LinkedList<PathNode> openNodes;
	
	private HashSet<PathNode> closedNodes;
	
	public HCube(){
		dataList = new HashMap<Integer , List<Lexeme>>();
	}
	
	/**
	 * 
	 * @param lexeme
	 * @return
	 */
	public boolean accept(Lexeme lexeme){
		if(left == -1 && right == -1){
			//accept
			
		}
		return false;
	}
	
	
	
	private void initialOpenNodes(){
		
	}
	


	class PathNode{
		
		private PathNode fisrtNode;
		
		private PathNode preNode;
		
		private PathNode nextNode;
		
		private Lexeme current;
		
		public PathNode(Lexeme current){
			this.current = current;
		}
		
	}
}
