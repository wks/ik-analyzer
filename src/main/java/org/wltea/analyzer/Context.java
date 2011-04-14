package org.wltea.analyzer;

import java.util.HashSet;
import java.util.Set;

import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.seg.ISegmenter;

/**
 * 分词器上下文状态
 * @author 林良益
 *
 */
public class Context{
	
	//是否使用最大词长切分（粗粒度）
	private boolean isMaxWordLength = false;	
    //记录Reader内已分析的字串总长度
    //在分多段分析词元时，该变量累计当前的segmentBuff相对于reader的位移
	private int buffOffset;	
	//最近一次读入的,可处理的字串长度
	private int available;
    //最近一次分析的字串长度
    private int lastAnalyzed;	
    //当前缓冲区位置指针
    private int cursor; 
    //字符窜读取缓冲
    private char[] segmentBuff;
    /*
     * 记录正在使用buffer的分词器对象
     * 如果set中存在有分词器对象，则buffer不能进行位移操作（处于locked状态）
     */
    private Set<ISegmenter> buffLocker;
    /*
     * 词元结果集，为每次游标的移动，存储切分出来的词元
     */
	private IKSortedLinkSet lexemeSet;

    
    Context(char[] segmentBuff , boolean isMaxWordLength){
    	this.isMaxWordLength = isMaxWordLength;
    	this.segmentBuff = segmentBuff;
    	this.buffLocker = new HashSet<ISegmenter>(4);
    	this.lexemeSet = new IKSortedLinkSet();
	}
    
    /**
     * 重置上下文
     */
    public void resetContext(){
    	buffLocker.clear();
    	lexemeSet = new IKSortedLinkSet();
    	buffOffset = 0;
    	available = 0;
    	lastAnalyzed = 0;
    	cursor = 0;
    }

	public boolean isMaxWordLength() {
		return isMaxWordLength;
	}

	public void setMaxWordLength(boolean isMaxWordLength) {
		this.isMaxWordLength = isMaxWordLength;
	}
    
	public int getBuffOffset() {
		return buffOffset;
	}


	public void setBuffOffset(int buffOffset) {
		this.buffOffset = buffOffset;
	}

	public int getLastAnalyzed() {
		return lastAnalyzed;
	}


	public void setLastAnalyzed(int lastAnalyzed) {
		this.lastAnalyzed = lastAnalyzed;
	}


	public int getCursor() {
		return cursor;
	}


	public void setCursor(int cursor) {
		this.cursor = cursor;
	}
	
	public void lockBuffer(ISegmenter segmenter){
		this.buffLocker.add(segmenter);
	}
	
	public void unlockBuffer(ISegmenter segmenter){
		this.buffLocker.remove(segmenter);
	}
	
	/**
	 * 只要buffLocker中存在ISegmenter对象
	 * 则buffer被锁定
	 * @return boolean 缓冲去是否被锁定
	 */
	public boolean isBufferLocked(){
		return this.buffLocker.size() > 0;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}
	
	

	/**
	 * 取出分词结果集中的首个词元
	 * @return Lexeme 集合的第一个词元
	 */
	public Lexeme firstLexeme() {
		return this.lexemeSet.pollFirst();
	}
	
	/**
	 * 取出分词结果集中的最后一个词元
	 * @return Lexeme 集合的最后一个词元
	 */
	public Lexeme lastLexeme() {
		return this.lexemeSet.pollLast();
	}
	
	/**
	 * 向分词结果集添加词元
	 * @param lexeme
	 */
	public void addLexeme(Lexeme lexeme){
		if(!Dictionary.isStopWord(segmentBuff , lexeme.getBegin() , lexeme.getLength())){
			this.lexemeSet.addLexeme(lexeme);
		}
	}
	
	/**
	 * 获取分词结果集大小
	 * @return int 分词结果集大小
	 */
	public int getResultSize(){
		return this.lexemeSet.size();
	}
	
	/**
	 * 排除结果集中完全交叠（彼此包含）的词元
	 * 进行最大切分的时候，过滤长度较小的交叠词元
	 */
	public void excludeOverlap(){
		 this.lexemeSet.excludeOverlap();
	}
	
	/**
	 * 
	 * @author linly
	 *
	 */
	private class IKSortedLinkSet{
		//链表头
		private Lexeme head;
		//链表尾
		private Lexeme tail;
		//链表的实际大小
		private int size;
		
		private IKSortedLinkSet(){
			this.size = 0;
		}
		/**
		 * 向链表集合添加词元
		 * @param lexeme
		 */
		private void addLexeme(Lexeme lexeme){
			if(this.size == 0){
				this.head = lexeme;
				this.tail = lexeme;
				this.size++;
				return;
				
			}else{
				if(this.tail.compareTo(lexeme) == 0){//词元与尾部词元相同，不放入集合
					return;
					
				}else if(this.tail.compareTo(lexeme) < 0){//词元接入链表尾部
					this.tail.setNext(lexeme);
					lexeme.setPrev(this.tail);
					this.tail = lexeme;
					this.size++;
					return;
					
				}else if(this.head.compareTo(lexeme) > 0){//词元接入链表头部
					this.head.setPrev(lexeme);
					lexeme.setNext(this.head);
					this.head = lexeme;
					this.size++;
					return;
					
				}else{					
					//从尾部上逆
					Lexeme l = this.tail;
					while(l != null && l.compareTo(lexeme) > 0){
						l = l.getPrev();
					}
					if(l.compareTo(lexeme) == 0){//词元与集合中的词元重复，不放入集合
						return;
						
					}else if(l.compareTo(lexeme) < 0){//词元插入链表中的某个位置
						lexeme.setPrev(l);
						lexeme.setNext(l.getNext());
						l.getNext().setPrev(lexeme);
						l.setNext(lexeme);
						this.size++;
						return;
						
					}
				}
			}
			
		}
		/**
		 * 取出链表集合的第一个元素
		 * @return Lexeme
		 */
		private Lexeme pollFirst(){
			if(this.size == 1){
				Lexeme first = this.head;
				this.head = null;
				this.tail = null;
				this.size--;
				return first;
			}else if(this.size > 1){
				Lexeme first = this.head;
				this.head = first.getNext();
				first.setNext(null);
				this.size --;
				return first;
			}else{
				return null;
			}
		}
		
		/**
		 * 取出链表集合的最后一个元素
		 * @return Lexeme
		 */
		private Lexeme pollLast(){
			if(this.size == 1){
				Lexeme last = this.head;
				this.head = null;
				this.tail = null;
				this.size--;
				return last;
				
			}else if(this.size > 1){
				Lexeme last = this.tail;
				this.tail = last.getPrev();
				last.setPrev(null);
				this.size--;
				return last;
				
			}else{
				return null;
			}
		}
		
		/**
		 * 剔除集合汇总相邻的切完全包含的lexeme
		 * 进行最大切分的时候，过滤长度较小的交叠词元
		 */
		private void excludeOverlap(){
			if(this.size > 1){
				Lexeme one = this.head;
				Lexeme another = one.getNext();
				do{
					if( one.isOverlap(another)
//							&& Lexeme.TYPE_LETTER != one.getLexemeType()
//							&& Lexeme.TYPE_LETTER != another.getLexemeType()
							){
						
						//邻近的两个词元完全交叠
						another = another.getNext();
						//从链表中断开交叠的词元
						one.setNext(another);
						if(another != null){
							another.setPrev(one);
						}
						this.size--;
						
					}else{//词元不完全交叠
						one = another;
						another = another.getNext();
					}
				}while(another != null);
			}
		}
		
		private int size(){
			return this.size;
		}
		
		
	}

}
