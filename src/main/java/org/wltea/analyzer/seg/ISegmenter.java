/**
 * 
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Context;

/**
 * 子分词器接口
 * @author 林良益
 *
 */
public interface ISegmenter {
	
	/**
	 * 从分析器读取下一个可能分解的词元对象
	 * @param segmentBuff 文本缓冲
	 * @param context 分词算法上下文
	 */
	void nextLexeme(char[] segmentBuff , Context context);
	
	/**
	 * 重置子分析器状态
	 */
	void reset();
}
