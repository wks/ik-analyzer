/**
 * 
 */
package org.wltea.analyzer.lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;


/**
 * IK Analyzer v3.2
 * Lucene3.0 Tokenizer适配器类
 * 它封装了IKSegmentation实现
 * 
 * @author 林良益
 *
 */
public final class IKTokenizer extends Tokenizer {
	
	//IK分词器实现
	private IKSegmentation _IKImplement;
	//词元文本属性
	private TermAttribute termAtt;
	//词元位移属性
	private OffsetAttribute offsetAtt;
	//记录最后一个词元的结束位置
	private int finalOffset;

	/**
	 * Lucene Tokenizer适配器类构造函数
	 * @param in
	 * @param isMaxWordLength 当为true时，分词器进行最大词长切分；当为false是，采用最细粒度切分
	 */
	public IKTokenizer(Reader in , boolean isMaxWordLength) {
	    super(in);
	    offsetAtt = addAttribute(OffsetAttribute.class);
	    termAtt = addAttribute(TermAttribute.class);
		_IKImplement = new IKSegmentation(in , isMaxWordLength);
	}	
	
	@Override
	public final boolean incrementToken() throws IOException {
		//清除所有的词元属性
		clearAttributes();
		Lexeme nextLexeme = _IKImplement.next();
		if(nextLexeme != null){
			//将Lexeme转成Attributes
			//设置词元文本
			termAtt.setTermBuffer(nextLexeme.getLexemeText());
			//设置词元长度
			termAtt.setTermLength(nextLexeme.getLength());
			//设置词元位移
			offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
			//记录分词的最后位置
			finalOffset = nextLexeme.getEndPosition();
			//返会true告知还有下个词元
			return true;
		}
		//返会false告知词元输出完毕
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.lucene.analysis.Tokenizer#reset(java.io.Reader)
	 */
	public void reset(Reader input) throws IOException {
		super.reset(input);
		_IKImplement.reset(input);
	}	
	
	@Override
	public final void end() {
	    // set final offset 
		offsetAtt.setOffset(finalOffset, finalOffset);
	}
	
}
