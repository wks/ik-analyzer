/**
 * 
 */
package org.wltea.analyzer.test;

import org.junit.Test;
import org.wltea.analyzer.cfg.Configuration;

/**
 *
 */
public class CfgTester {
	
	@Test
	public void testCfgLoading(){
		System.out.println(Configuration.getExtDictionarys().size());
		System.out.println(Configuration.getExtStopWordDictionarys().size());
	}

}
