/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 *
 */

public class WAVLTree {
	
	public WAVLNode sentinel;
	public WAVLNode root;
	//public WAVLNode null;
	
	public WAVLTree()
	{
		this.root = null;	
		
	}
	

  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
	  if (this.root == null)
	  {
		  return true;
	  }
	  
    return false; 
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)
  {
	  if (this.empty())
	  {
		  return null;
	  }
  		  
	  WAVLNode result = searchRecursive(root, k);
	  if(result != null){
		  return result.Info;
	  }
	  else{
		  return null;
	  }
	  
  }

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {
	  return 42;	// to be replaced by student code
   }

  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   return 42;	// to be replaced by student code
   }

   /**
    * public String min()
    *
    * Returns the i׳�fo of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   if (this.empty()){
		   return null; 
	   }
	   WAVLNode iter = root;
	   while ( iter.Lchild!=null ){
		   iter = iter.Lchild;
	   }
	   return iter.Info;
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   if (this.empty()){
		   return null; 
	   }
	   WAVLNode iter = root;
	   while ( iter.Rchild!=null ){
		   iter = iter.Rchild;
	   }
	   return iter.Info;
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  if (this.empty()){
		   return new int[0]; 
	   }
	  
	   WAVLNode iter = root;
	   
	   
	   
        int[] arr = new int[42]; // to be replaced by student code
        return arr;              // to be replaced by student code
  }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
        String[] arr = new String[42]; // to be replaced by student code
        return arr;                    // to be replaced by student code
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   return 42; // to be replaced by student code
   }

  /**
   * public class WAVLNode
   *
   * If you wish to implement classes other than WAVLTree
   * (for example WAVLNode), do it in this file, not in 
   * another file.
   * This is an example which can be deleted if no such classes are necessary.
 * @return 
   */
   

   private WAVLNode searchRecursive( WAVLNode root, int k){
	   if (root==null){
		   return null; 
	   }
	   else if (root.Key==k ){
		   return root;
	   }
	   else if (root.Key<k){
		   return searchRecursive(root.Lchild, k);
	   }
	   else if (root.Key>k){
		   return searchRecursive(root.Rchild, k);
	   }   
	   return null;
	}
   
   
   
   
   
  public class WAVLNode{
	  
	  private WAVLNode Parent ;
	  private WAVLNode Rchild ;
	  private WAVLNode Lchild ;
	  private int Key ;
	  private String Info;
	  
	  public WAVLNode (WAVLNode parent, WAVLNode rchild,
			  WAVLNode lchild, int key, String info )
	  {
		  this.Parent = parent;
		  this.Rchild = rchild;
		  this.Lchild = lchild;
		  this.Key = key;
		  this.Info = info;
	  }
	  
	  public WAVLNode ()
	  {
		  this.Parent = this;
		  this.Rchild = this;
		  this.Lchild = this;
		  this.Key = Integer.MAX_VALUE;
		  this.Info = "";
	  }
	
	  public WAVLNode getParent() {
		  return Parent;
		}
	
		public void setParent(WAVLNode parent) {
			Parent = parent;
		}
	
		public WAVLNode getRchild() {
			return Rchild;
		}
	
		public void setRchild(WAVLNode rchild) {
			Rchild = rchild;
		}
	
		public WAVLNode getLchild() {
			return Lchild;
		}
	
		public void setLchild(WAVLNode lchild) {
			Lchild = lchild;
		}
	
		public int getKey() {
			return Key;
		}
	
		public void setKey(int key) {
			Key = key;
		}
	
		public String getInfo() {
			return Info;
		}
	
		public void setInfo(String info) {
			Info = info;
		}
	  

		
  }
 

}
  
	