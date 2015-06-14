/**
 * 
 */
package org.wltea.analyzer.test;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKTokenizer;

/**
 * @author 林良益
 *
 */
public class IKTokenerTest {

	@Test
	public void testLucene5Tokenizer() throws IOException {
		String t = "IK分词器Lucene Analyzer接口实现类 民生银行";
		IKTokenizer tokenizer = new IKTokenizer(false);
		tokenizer.setReader(new StringReader(t));
		tokenizer.reset();
		try {
			while (tokenizer.incrementToken()) {
				CharTermAttribute termAtt = tokenizer
						.getAttribute(CharTermAttribute.class);
				System.out.println(termAtt);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
