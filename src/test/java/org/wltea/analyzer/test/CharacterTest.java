/**
 * 
 */
package org.wltea.analyzer.test;

import org.wltea.analyzer.help.CharacterHelper;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 */
public class CharacterTest extends TestCase{

	public void testSBC2DBCChar(){
		char a = '‘';
//		char a = 'Ｏ';
//		char a = '○';
		System.out.println((int)a);
		System.out.println(CharacterHelper.regularize(a));
		System.out.println((int)CharacterHelper.regularize(a));
		
		String sss  = "智灵通乳酸钙冲剂(5g\14袋)-1244466518522.txt";
		System.out.println(sss.replaceAll("[\\\\]", "每"));
	}
	
	public void testCharBlock(){
		
		Character.UnicodeBlock ub = Character.UnicodeBlock.of('년');
		System.out.println(ub.toString());
		
		ub = Character.UnicodeBlock.of('⑧');
		System.out.println(ub.toString());		
		ub = Character.UnicodeBlock.of('①');
		System.out.println(ub.toString());
		ub = Character.UnicodeBlock.of('⑴');
		System.out.println(ub.toString());
		ub = Character.UnicodeBlock.of('⒈');
		System.out.println(ub.toString());
		
	}
}
