/**
 * 
 */
package org.wltea.analyzer.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.wltea.analyzer.dic.DictSegment;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.dic.Hit;

/**
 * 主词典统计分析工具类
 * @author 林良益
 * 
 */
public class DictionaryTester extends TestCase {
	
	public void testMainDicEncoding(){
		int count = 0;
        InputStream is = DictionaryTester.class.getResourceAsStream(Dictionary.PATH_DIC_MAIN);
		try {
			
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					theWord = theWord.trim();
                    /*Test Logging*/
                    System.out.println(theWord);
				}
				count++;
			} while (theWord != null && count < 20);
			
		} catch (IOException ioe) {
			System.err.println("主词典库载入异常.");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testMainDictMemoryConsume(){
        InputStream is = DictionaryTester.class.getResourceAsStream(Dictionary.PATH_DIC_MAIN);
        System.out.println(new Date() + " before load dictionary");
        DictSegment _root_ = new DictSegment((char)0);
        try {
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println(new Date() + " loading dictionary");
		try {
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					_root_.fillSegment(theWord.toCharArray());
				}
			} while (theWord != null);
			System.out.println(new Date() + " after load dictionary");

	
		} catch (IOException ioe) {
			System.err.println("主词典库载入异常.");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
        try {
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}	
	
	public void testCountWordHeader(){
		FileOutputStream fos = null;
		Map<String , Integer> wordMap = new HashMap<String ,Integer>();
        InputStream is = DictionaryTester.class.getResourceAsStream(Dictionary.PATH_DIC_MAIN);
        
		try {
			fos = new FileOutputStream("D:/testCountWordHeader.txt");
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					theWord = theWord.trim();
					String key = theWord.substring(0,1);
					Integer c = wordMap.get(key);
					if(c == null){
						wordMap.put(key, new Integer(1));
					}else{
						wordMap.put(key, ++c);
					}
				}
			} while (theWord != null);
			
			int countOnlyOne = 0;
			int countMorethan64 = 0;
			Set<String> it = wordMap.keySet();
			for(String key : it){
				Integer c = wordMap.get(key);
				if(c == 1){
					countOnlyOne ++;
				}
				if(c > 64){
					countMorethan64 ++;
				}
				
				fos.write((key + " : " + c + "\r\n").getBytes());
			}
			fos.write(("Total : " + wordMap.size() + "\r\n").getBytes());
			fos.write(("OnlyOneCount : " + countOnlyOne + "\r\n").getBytes());
			fos.write(("MoreThen64Count : " + countMorethan64 + "\r\n").getBytes());
			fos.flush();
			
		} catch (IOException ioe) {
			System.err.println("主词典库载入异常.");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(fos != null){
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public void testSurNameDicEncoding(){
		int count = 0;
        InputStream is = DictionaryTester.class.getResourceAsStream(Dictionary.PATH_DIC_SURNAME);
		try {
			
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					theWord = theWord.trim();
                    /*Test Logging*/
                    System.out.println(theWord);
				}
				count++;
			} while (theWord != null && count < 20);
			
		} catch (IOException ioe) {
			System.err.println("姓氏典库载入异常.");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testSuffixDicEncoding(){
		int count = 0;
        InputStream is = DictionaryTester.class.getResourceAsStream(Dictionary.PATH_DIC_SUFFIX);
		try {
			
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					theWord = theWord.trim();
                    /*Test Logging*/
                    System.out.println(theWord);
				}
				count++;
			} while (theWord != null && count < 20);
			
		} catch (IOException ioe) {
			System.err.println("后缀典库载入异常.");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testStopDicEncoding(){
		int count = 0;
        //InputStream is = DictionaryTester.class.getResourceAsStream(Dictionary.PATH_DIC_STOP);
        InputStream is = DictionaryTester.class.getResourceAsStream("/mydict.dic");
		try {
			
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					theWord = theWord.trim();
                    /*Test Logging*/
                    System.out.println(theWord);
				}
				count++;
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("停止词典库载入异常.");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
		
	
	public void testDictSegmentSearch(){
        InputStream is = DictionaryTester.class.getResourceAsStream(Dictionary.PATH_DIC_MAIN);
        System.out.println(new Date() + " before load dictionary");

        DictSegment _root_ = new DictSegment((char)0);
        List<String> allWords = new ArrayList<String>();
        
        System.out.println(new Date() + " loading dictionary");
		try {
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					allWords.add(theWord.trim());
					_root_.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			System.out.println(new Date() + " after load dictionary");

	
		} catch (IOException ioe) {
			System.err.println("主词典库载入异常.");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println(new Date() + " begin march");
		long begintime = System.currentTimeMillis();
		Hit hit = null;
		int umCount = 0;
		int mCount = 0;
		for(String word : allWords){
			char[] chars = word.toCharArray();
			hit = _root_.match(chars , 0, chars.length);
			if(hit.isUnmatch()){
				//System.out.println(word);
				umCount++;
			}else{
				mCount++;
				//System.out.println(mCount + " : " + word);
			}
		}
		System.out.println(new Date() + " finish march , cost " + (System.currentTimeMillis() - begintime ) + " millseconds");
		System.out.println("Total words : " + allWords.size() + " Match words : " + mCount + " Unmatch words : " + umCount);
	}
	
	public void testDictionarySearch(){
	     InputStream is = DictionaryTester.class.getResourceAsStream(Dictionary.PATH_DIC_MAIN);
	     List<String> allWords = new ArrayList<String>();
	        
	     try {
				String theWord = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
				do {
					theWord = br.readLine();
					if (theWord != null) {
						allWords.add(theWord.trim());
					}
				} while (theWord != null);
				
			} catch (IOException ioe) {
				ioe.printStackTrace();
				
			}finally{
				try {
					if(is != null){
	                    is.close();
	                    is = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Dictionary.getInstance();
	        try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println(new Date() + " begin march");
			long begintime = System.currentTimeMillis();
			Hit hit = null;
			int umCount = 0;
			int mCount = 0;
			for(String word : allWords){			
				hit = Dictionary.matchInMainDict(word.toCharArray(), 0, word.length());
				if(hit.isUnmatch()){
					System.out.println(word);
					umCount++;
				}else{
					mCount++;
				}
			}
			System.out.println(new Date() + " finish march , cost " + (System.currentTimeMillis() - begintime ) + " millseconds");
			System.out.println("Match words : " + mCount + " Unmatch words : " + umCount);		
	}
	
	/**
	 * 量词排序
	 */
	public void testSortCount(){
		InputStream is = DictionaryTester.class.getResourceAsStream(Dictionary.PATH_DIC_QUANTIFIER);
		TreeSet<String> allWords = new TreeSet<String>();
	        
		try {
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					allWords.add(theWord.trim());
					System.out.println(theWord.trim());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream("E:/quantifier.dic");
//			String w = null;
//			for(w = allWords.pollFirst(); w != null ; w = allWords.pollFirst()){
//				//System.out.println(w);
//				fos.write(w.getBytes("UTF-8"));
//				fos.write("\r\n".getBytes("UTF-8"));
//			}
//			fos.flush();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			try {
//				fos.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
//	public void testEndsWithSurnameDict(){
//		
//		Dictionary.getInsance();
//		List<String> testStr = new ArrayList<String>();
//		testStr.add("林");
//		testStr.add("树林中");
//		testStr.add("红树林");
//		testStr.add("李");
//		testStr.add("行李");
//		testStr.add("东方");
//		testStr.add("闻人");
//		testStr.add("日出东方唯我不败");
//		
//		for(String t : testStr){
//			System.out.println(t);	
//			char[] charArray = t.toCharArray();
//			System.out.println(Dictionary.endsWithSurnameDict(charArray, 0, charArray.length));
//		}
//	}
//	
//	
//	public void testStartsWithSuffixDict(){
//		
//		Dictionary.getInsance();
//		List<String> testStr = new ArrayList<String>();
//		testStr.add("路");
//		testStr.add("山上");
//		testStr.add("斯基");
//		testStr.add("党内");
//		testStr.add("我党");
//		testStr.add("冶山路");
//		
//		for(String t : testStr){
//			System.out.println(t);	
//			char[] charArray = t.toCharArray();
//			System.out.println(Dictionary.startsWithSuffixDict(charArray, 0, charArray.length));
//		}
//	}
	
	public void testSortAndClear(){
		//int count = 0;
        InputStream is = DictionaryTester.class.getResourceAsStream(Dictionary.PATH_DIC_PREP);
        TreeSet<String> ts = new  TreeSet<String>();
		try {
			
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					theWord = theWord.trim();
                    /*Test Logging*/
					ts.add(theWord);
                    System.out.println(theWord);
				}
				//count++;
			} while (theWord != null);
			
			
			
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("E:/preposition.dic");
				String w = null;
				for(w = ts.pollFirst(); w != null ; w = ts.pollFirst()){
					//System.out.println(w);
					fos.write(w.getBytes("UTF-8"));
					fos.write("\r\n".getBytes("UTF-8"));
				}
				fos.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
}
