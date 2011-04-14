/**
 * 
 */
package org.wltea.analyzer;

/**
 * IK Analyzer v3.2
 * 语义单元（词元） * 
 * @author 林良益
 *
 */
public final class Lexeme implements Comparable<Lexeme>{
	//lexemeType常量
	//普通词元
	public static final int TYPE_CJK_NORMAL = 0;
	//姓氏
	public static final int TYPE_CJK_SN = 1;
	//尾缀
	public static final int TYPE_CJK_SF = 2;
	//未知的
	public static final int TYPE_CJK_UNKNOWN = 3;
	//数词
	public static final int TYPE_NUM = 10;
	//量词
	public static final int TYPE_NUMCOUNT = 11;
	//英文
	public static final int TYPE_LETTER = 20;
	
	//词元的起始位移
	private int offset;
    //词元的相对起始位置
    private int begin;
    //词元的长度
    private int length;
    //词元文本
    private String lexemeText;
    //词元类型
    private int lexemeType;
    
    //当前词元的前一个词元
    private Lexeme prev;
    //当前词元的后一个词元
    private Lexeme next;
    
	public Lexeme(int offset , int begin , int length , int lexemeType){
		this.offset = offset;
		this.begin = begin;
		if(length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
		this.lexemeType = lexemeType;
	}
	
    /*
     * 判断词元相等算法
     * 起始位置偏移、起始位置、终止位置相同
     * @see java.lang.Object#equals(Object o)
     */
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		
		if(this == o){
			return true;
		}
		
		if(o instanceof Lexeme){
			Lexeme other = (Lexeme)o;
			if(this.offset == other.getOffset()
					&& this.begin == other.getBegin()
					&& this.length == other.getLength()){
				return true;			
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}
	
    /*
     * 词元哈希编码算法
     * @see java.lang.Object#hashCode()
     */
    public int hashCode(){
    	int absBegin = getBeginPosition();
    	int absEnd = getEndPosition();
    	return  (absBegin * 37) + (absEnd * 31) + ((absBegin * absEnd) % getLength()) * 11;
    }
    
    /*
     * 词元在排序集合中的比较算法
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
	public int compareTo(Lexeme other) {
		//起始位置优先
        if(this.begin < other.getBegin()){
            return -1;
        }else if(this.begin == other.getBegin()){
        	//词元长度优先
        	if(this.length > other.getLength()){
        		return -1;
        	}else if(this.length == other.getLength()){
        		return 0;
        	}else {//this.length < other.getLength()
        		return 1;
        	}
        	
        }else{//this.begin > other.getBegin()
        	return 1;
        }
	}
	
	/**
	 * 判断词元是否彼此包含
	 * @param other
	 * @return boolean true 完全包含 ， false 可能不相交 或者 相交但不包含
	 */
	public boolean isOverlap(Lexeme other){
		if(other != null){
			if(this.getBeginPosition() <= other.getBeginPosition() 
					&& this.getEndPosition() >= other.getEndPosition()){
				return true;
				
			}else if(this.getBeginPosition() >= other.getBeginPosition() 
					&& this.getEndPosition() <= other.getEndPosition()){
				return true;
				
			}else {
				return false;
			}
		}
		return false;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getBegin() {
		return begin;
	}
	/**
	 * 获取词元在文本中的起始位置
	 * @return int
	 */
	public int getBeginPosition(){
		return offset + begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	/**
	 * 获取词元在文本中的结束位置
	 * @return int
	 */
	public int getEndPosition(){
		return offset + begin + length;
	}
	
	/**
	 * 获取词元的字符长度
	 * @return int
	 */
	public int getLength(){
		return this.length;
	}	
	
	public void setLength(int length) {
		if(this.length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
	}
	
	/**
	 * 获取词元的文本内容
	 * @return String
	 */
	public String getLexemeText() {
		if(lexemeText == null){
			return "";
		}
		return lexemeText;
	}

	public void setLexemeText(String lexemeText) {
		if(lexemeText == null){
			this.lexemeText = "";
			this.length = 0;
		}else{
			this.lexemeText = lexemeText;
			this.length = lexemeText.length();
		}
	}

	/**
	 * 获取词元类型
	 * @return int
	 */
	public int getLexemeType() {
		return lexemeType;
	}

	public void setLexemeType(int lexemeType) {
		this.lexemeType = lexemeType;
	}	
	
	public String toString(){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(this.getBeginPosition()).append("-").append(this.getEndPosition());
		strbuf.append(" : ").append(this.lexemeText).append(" : \t");
		switch(lexemeType) {
			case TYPE_CJK_NORMAL : 
				strbuf.append("CJK_NORMAL");
				break;
			case TYPE_CJK_SF :
				strbuf.append("CJK_SUFFIX");
				break;
			case TYPE_CJK_SN :
				strbuf.append("CJK_NAME");
				break;
			case TYPE_CJK_UNKNOWN :
				strbuf.append("UNKNOWN");
				break;
			case TYPE_NUM : 
				strbuf.append("NUMEBER");
				break;
			case TYPE_NUMCOUNT :
				strbuf.append("COUNT");
				break;
			case TYPE_LETTER :
				strbuf.append("LETTER");
				break;

		}
		return strbuf.toString();
	}

	Lexeme getPrev() {
		return prev;
	}

	void setPrev(Lexeme prev) {
		this.prev = prev;
	}

	Lexeme getNext() {
		return next;
	}

	void setNext(Lexeme next) {
		this.next = next;
	}

	
}