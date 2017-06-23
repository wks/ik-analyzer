/**
 *
 */
package org.wltea.analyzer.solr;

import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.wltea.analyzer.lucene.IKTokenizer;

/**
 * 实现Solr1.4分词器接口
 * 基于IKTokenizer的实现
 *
 * @author 林良益、李良杰
 */
public final class IKTokenizerFactory extends TokenizerFactory {

    public IKTokenizerFactory(Map<String, String> args) {
        super(args);
    }

    private boolean isMaxWordLength = false;

    /**
     * IK分词器Solr TokenizerFactory接口实现类
     * 默认最细粒度切分算法
     */

	/*
	 * (non-Javadoc)
	 * @see org.apache.solr.analysis.BaseTokenizerFactory#init(java.util.Map)
	 */
    public void init(Map<String, String> args) {
        String _arg = args.get("isMaxWordLength");
        isMaxWordLength = Boolean.parseBoolean(_arg);
    }

    public void setMaxWordLength(boolean isMaxWordLength) {
        this.isMaxWordLength = isMaxWordLength;
    }

    public boolean isMaxWordLength() {
        return isMaxWordLength;
    }

    @Override
    public Tokenizer create(AttributeFactory factory) {
        return new IKTokenizer(factory);
    }

}
