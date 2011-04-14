package org.wltea.analyzer.test;
///**
// * 
// */
//package org.wltea.analyzer.seg;
//
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.Set;
//
//import org.wltea.analyzer.Context;
//import org.wltea.analyzer.Lexeme;
//import org.wltea.analyzer.dic.Dictionary;
//import org.wltea.analyzer.dic.Hit;
//import org.wltea.analyzer.help.CharacterHelper;
//
///**
// * 简易数量词子分词器，涵盖一下范围
// * 1.阿拉伯数字，阿拉伯数字+中文量词
// * 2.中文数字+中文量词
// * @author 林良益
// *
// */
//public class SimpleQuantifierSegmenter  implements ISegmenter{
//	
//	//状态机常量
//	//初始状态
//	public static final int NaN = -99;	
//	//阿拉伯数字0-9
//	public static final int NC_Arabic = 02;
//	//阿拉伯数字连接符
//	public static final int NC_ANM = 03;	
//	//中文数词
//	public static final int NC_Chinese = 12;
//	//量词状态
//	public static final int NC_Quan = 100;
//	
//	//阿拉伯数词链接符号
//	public static char Arabic_Num_Mid = '.';
//	
//	//中文数词集
//	public static String Chn_Num = "○一二两三四五六七八九十零壹贰叁肆伍陆柒捌玖拾百千万亿拾佰仟萬億兆卅廿";//Cnum
//	private static Set<Character> ChnNumberChars = new HashSet<Character>();
//	static{
//		char[] ca = Chn_Num.toCharArray();
//		for(char nChar : ca){
//			ChnNumberChars.add(nChar);
//		}
//	}
//
//	/*
//	 * 当前子分词器的字符状态 
//	 */
//	private int currentStatus;
//	
//	/*
//	 * 分词处理单元对象 的 链表
//	 */
//	private LinkedList<XUnit> units;
//	
//	/*
//	 * 记录最新获得的量词
//	 */
//	private XUnit lastQuantifier;
//	
//	/*
//	 * 量词匹配过程的HIT
//	 */
//	private Hit quanHit;
//	
//	public SimpleQuantifierSegmenter(){
//		currentStatus = NaN;
//	}
//	
//	
//	public void nextLexeme(char[] segmentBuff, Context context) {		
//		//状态机处理
//		runFSM(segmentBuff , context);
//		
////		//在找到数词的情况下，过滤量词
////		if(this.numberHit != null 
////				&& (NaN == nStatus || NC_CHINESE == nStatus)){
////			processCount(segmentBuff , context);
////		}
////		
////		//读到缓冲区最后一个字符，还有尚未输出
////		if(context.getCursor() == context.getAvailable() - 1){
////			if(this.numberHit != null){
////				//输出数词
////				output(context);
////			}
////		}				
//	}
//	
//	public void reset() {
//		//TODO 重置初始状态
//	}
//
//	/**
//	 * 数词处理
//	 * @param segmentBuff
//	 * @param context
//	 */
//	private void runFSM(char[] segmentBuff , Context context){		
//		//识别输入状态
//		int inputStatus = nIdentify(segmentBuff , context);
//		
//		if(this.currentStatus == NaN){
//			this.onNaNStatus(inputStatus, context);
//			
//		}else if(this.currentStatus == NC_Arabic){
//			this.onARABICStatus(inputStatus, context);
//			
//		}else if(this.currentStatus == NC_ANM){
//
//		}else if(this.currentStatus == NC_Chinese){
//			
//		}
//	
//	}
//	
//	/**
//	 * 识别数字字符类型
//	 * @param input
//	 * @return
//	 */
//	private int nIdentify(char[] segmentBuff , Context context){
//		
//		//读取当前位置的char	
//		char input = segmentBuff[context.getCursor()];
//		
//		int type = NaN;
//
//		if(CharacterHelper.isArabicNumber(input)){
//			type = NC_Arabic;
//			 
//		}else if(ChnNumberChars.contains(input)){
//			type = NC_Chinese;
//			
//		}else if(Arabic_Num_Mid == input){
//			type = NC_ANM;
//		}
//		return type;
//	}
//		
//	/**
//	 * 当前为NaN状态时，状态机的处理(状态转换)
//	 * @param inputStatus
//	 * @param context
//	 */
//	private void onNaNStatus(int inputStatus ,  Context context){
//		if(NC_Chinese == inputStatus //中文数词
//				|| NC_Arabic == inputStatus //阿拉伯数字
//				){
//			XUnit unit = new XUnit();
//			//记录unit起始位置
//			unit.start = context.getCursor();
//			//记录unit最新的结束位置
//			unit.end = context.getCursor();
//			//记录unit的字符状态
//			unit.type = inputStatus;
//			//变更FSM的状态标志
//			currentStatus = inputStatus;
//			//将unit放入链表
//			this.units.add(unit);
//		}
//	}
//	
//	/**
//	 * 当前为ARABIC状态时，状态机的处理(状态转换)
//	 * @param inputStatus
//	 * @param context
//	 */
//	private void onARABICStatus(int inputStatus ,  Context context){
//		if(NC_Arabic == inputStatus){
//			//保持状态不变
//			//记录可能的结束位置
//			numberHit.nEnd = context.getCursor();
//			
//		}else if(NC_Chinese == inputStatus){//中文数字
//			//从ARABIC --> NC_CHINESE 状态，可连续
//			//记录可能的结束位置
//			numberHit.nEnd = context.getCursor();
//			//记录当前的字符状态
//			nStatus = inputStatus;
//		}else {
//			//记录当前的字符状态
//			nStatus = NaN;			
//		}
//	}
//
//	/**
//	 * 当前为CHINESE状态时，状态机的处理(状态转换)
//	 * @param inputStatus
//	 * @param context
//	 */
//	private void onCHINESEStatus(int inputStatus ,  Context context){
//		if(NC_Chinese == inputStatus){//中文数字
//			//记录可能的结束位置
//			numberHit.nEnd = context.getCursor();
//			
//		}else if(NC_Arabic == inputStatus){//其他输入
//			//输出的数词
//			output(context);
//			//重置数词状态
//			numberHit = new NumberHit();
//			//记录起始位置
//			numberHit.nStart = context.getCursor();
//			//记录可能的结束位置
//			numberHit.nEnd = context.getCursor();
//			//记录当前的字符状态
//			nStatus = inputStatus;
//		}else{
//			//记录当前的字符状态
//			nStatus = NaN;			
//		}
//	}
//	
//	/**
//	 * 处理中文量词
//	 * @param segmentBuff
//	 * @param context
//	 */
//	private void processCount(char[] segmentBuff , Context context){
//		Hit hit = numberHit.qHit;
//
//		if(hit == null){
//			hit = Dictionary.matchInQuantifierDict(segmentBuff , context.getCursor() , 1);
//		}else{
//			hit = Dictionary.matchInHit(segmentBuff , context.getCursor() , hit);
//		}
//
//		if(hit.isPrefix()){
//			//设置量词的开始
//			numberHit.qStart = hit.getBegin();
//			numberHit.qHit = hit;
//		}
//			
//		if(hit.isMatch()){
//			//设置量词的开始
//			numberHit.qStart = hit.getBegin();
//			//设置量词的结束
//			numberHit.qEnd = hit.getEnd();
//		}
//			
//		if(hit.isUnmatch() && NaN == nStatus){
//			//输出。。。
//			this.output(context);
//		}
//	}	
//	
//	/**
//	 * 添加数词词元到结果集
//	 * @param context
//	 */
//	private void output(Context context){
//		//找到数词和量词
//		if(numberHit.qStart > -1 && numberHit.qEnd > -1){
//			//单独输出数词
//			Lexeme nLexeme = new Lexeme(context.getBuffOffset() , numberHit.nStart , numberHit.qStart - numberHit.nStart, Lexeme.TYPE_NUM );
//			context.addLexeme(nLexeme);
//			//联合输出数量词
//			Lexeme qLexeme = new Lexeme(context.getBuffOffset() , numberHit.nStart , numberHit.qEnd - numberHit.nStart + 1 , Lexeme.TYPE_NUM );
//			context.addLexeme(qLexeme);
//			
//		}else if(numberHit.nStart > -1 && numberHit.nEnd > -1){//只找到数词
//			//单独输出数词
//			Lexeme nLexeme = new Lexeme(context.getBuffOffset() , numberHit.nStart , numberHit.nEnd - numberHit.nStart + 1, Lexeme.TYPE_NUM );
//			context.addLexeme(nLexeme);
//		}
//		numberHit = null;
//	}
//	
//	/**
//	 * 一个正在匹配中的对象
//	 * @author linliangyi
//	 *
//	 */
//	class XUnit{
//		int start = -1;
//		int end = -1;
//		int type = NaN;
//	}
//}
