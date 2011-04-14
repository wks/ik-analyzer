/**
 * 
 */
package org.wltea.analyzer.seg;

import java.util.HashSet;
import java.util.Set;

import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.Context;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.dic.Hit;
import org.wltea.analyzer.help.CharacterHelper;

/**
 * 数量词子分词器，涵盖一下范围
 * 1.阿拉伯数字，阿拉伯数字+中文量词
 * 2.中文数字+中文量词
 * 3.时间,日期
 * 4.罗马数字
 * 5.数学符号 % . / 
 * @author 林良益
 *
 */
public class QuantifierSegmenter implements ISegmenter {

	//阿拉伯数词前缀（货币符号）
//	public static String Arabic_Num_Pre = "-+$￥";//Apre
//	private static Set<Character> ArabicNumPreChars = new HashSet<Character>();
//	static{
//		char[] ca = Arabic_Num_Pre.toCharArray();
//		for(char nChar : ca){
//			ArabicNumPreChars.add(nChar);
//		}
//	}
//	public static final int NC_ANP = 01;	
	//阿拉伯数字0-9
	public static final int NC_ARABIC = 02;
	//阿拉伯数词链接符号
	public static String Arabic_Num_Mid = ",./:Ee";//Amid
	private static Set<Character> ArabicNumMidChars = new HashSet<Character>();
	static{
		char[] ca = Arabic_Num_Mid.toCharArray();
		for(char nChar : ca){
			ArabicNumMidChars.add(nChar);
		}
	}
	public static final int NC_ANM = 03;
//	//阿拉伯数词后缀
//	public static String Arabic_Num_End = "%‰";//Aend
//	public static final int NC_ANE = 04;
	
	//序数词（数词前缀）
	public static String Num_Pre = "第初";//Cpre
	public static final int NC_NP = 11;
	//中文数词
	public static String Chn_Num = "○一二两三四五六七八九十零壹贰叁肆伍陆柒捌玖拾百千万亿拾佰仟萬億兆卅廿";//Cnum
	private static Set<Character> ChnNumberChars = new HashSet<Character>();
	static{
		char[] ca = Chn_Num.toCharArray();
		for(char nChar : ca){
			ChnNumberChars.add(nChar);
		}
	}
	public static final int NC_CHINESE = 12;
	//中文数词连接符
	public static String Chn_Num_Mid = "点";//Cmid
	public static final int NC_CNM = 13;
	
	//约数词（数词结尾）
	public static String Num_End = "几多余半";//Cend
	private static Set<Character> NumEndChars = new HashSet<Character>();
	static{
		char[] ca = Num_End.toCharArray();
		for(char nChar : ca){
			NumEndChars.add(nChar);
		}
	}
	public static final int NC_NE = 14;
	
//	//GB库中的罗马字符(起始、中间、结束)
//	public static String Rome_Num = "ⅠⅡⅢⅣⅤⅥⅧⅨⅩⅪ"; //Rnum
//	private static Set<Character> RomeNumChars = new HashSet<Character>();
//	static{
//		char[] ca = Rome_Num.toCharArray();
//		for(char nChar : ca){
//			RomeNumChars.add(nChar);
//		}
//	}
//	public static final int NC_ROME = 22;

	//非数词字符
	public static final int NaN = -99;
	
	//所有的可能数词
	private static Set<Character> AllNumberChars = new HashSet<Character>(256);
	static{
		char[] ca = null;
		
//		AllNumberChars.addAll(ArabicNumPreChars);

		for(char nChar = '0' ; nChar <='9' ; nChar++ ){
			AllNumberChars.add(nChar);
		}
		
		AllNumberChars.addAll(ArabicNumMidChars);
		
//		ca = Arabic_Num_End.toCharArray();
//		for(char nChar : ca){
//			AllNumberChars.add(nChar);
//		}
		
		ca = Num_Pre.toCharArray();
		for(char nChar : ca){
			AllNumberChars.add(nChar);
		}
		
		AllNumberChars.addAll(ChnNumberChars);
		
		ca = Chn_Num_Mid.toCharArray();
		for(char nChar : ca){
			AllNumberChars.add(nChar);
		}

		AllNumberChars.addAll(NumEndChars);
		
//		AllNumberChars.addAll(RomeNumChars);
		
	}
	
	/*
	 * 词元的开始位置，
	 * 同时作为子分词器状态标识
	 * 当start > -1 时，标识当前的分词器正在处理字符
	 */
	private int nStart;
	/*
	 * 记录词元结束位置
	 * end记录的是在词元中最后一个出现的合理的数词结束
	 */
	private int nEnd;
	/*
	 * 当前数词的状态 
	 */
	private int nStatus;
	/*
	 * 捕获到一个数词
	 */
	private boolean fCaN;	
	
	/*
	 * 量词起始位置
	 */
	private int countStart;
	/*
	 * 量词终止位置
	 */
	private int countEnd;
	

	
	public QuantifierSegmenter(){
		nStart = -1;
		nEnd = -1;
		nStatus = NaN;
		fCaN = false;
		
		countStart = -1;
		countEnd = -1;
	}
	
	/* (non-Javadoc)
	 * @see org.wltea.analyzer.ISegmenter#nextLexeme(org.wltea.analyzer.IKSegmentation.Context)
	 */
	public void nextLexeme(char[] segmentBuff , Context context) {
		fCaN = false;
		//数词处理部分
		processNumber(segmentBuff , context);
		
		//量词处理部分		
		if(countStart == -1){//未开始处理量词
			//当前游标的位置紧挨着数词
			if((fCaN && nStart == -1)
					|| (nEnd != -1 && nEnd == context.getCursor() - 1)//遇到CNM的状态
					){				
				//量词处理
				processCount(segmentBuff , context);
			
			}
		}else{//已开始处理量词
			//量词处理
			processCount(segmentBuff , context);
		}

		//判断是否锁定缓冲区
		if(this.nStart == -1 && this.nEnd == -1 && NaN == this.nStatus
				&& this.countStart == -1 && this.countEnd == -1){
			//对缓冲区解锁
			context.unlockBuffer(this);
		}else{
			context.lockBuffer(this);
		}
	}

	/**
	 * 数词处理
	 * @param segmentBuff
	 * @param context
	 */
	private void processNumber(char[] segmentBuff , Context context){		
		//数词字符识别
		int inputStatus = nIdentify(segmentBuff , context);
		
		if(NaN == nStatus){
			//当前的分词器尚未开始处理字符
			onNaNStatus(inputStatus , context);
			
//		}else if(NC_ANP == nStatus){ 
//			//当前为阿拉伯数字前缀	
//			onANPStatus(inputStatus , context);
			
		}else if(NC_ARABIC == nStatus){
			//当前为阿拉伯数字
			onARABICStatus(inputStatus , context);
			
		}else if(NC_ANM	== nStatus){
			//当前为阿拉伯数字链接符
			onANMStatus(inputStatus , context);
			
//		}else if(NC_ANE == nStatus){
//			//当前为阿拉伯数字结束符
//			onANEStatus(inputStatus , context);
			
		}else if(NC_NP == nStatus){
			//当前为中文数字前缀
			onNPStatus(inputStatus , context);
			
		}else if(NC_CHINESE == nStatus){
			//当前为中文数字
			onCHINESEStatus(inputStatus , context);
			
		}else if(NC_CNM == nStatus){
			//当前为中文数字连接符
			onCNMStatus(inputStatus , context);
			
		}else if(NC_NE == nStatus){
			//当前为中文数字结束符
			onCNEStatus(inputStatus , context);
			
//		}else if(NC_ROME == nStatus){
//			//当前为罗马数字
//			onROMEStatus(inputStatus , context);			
			
		}
		
		//读到缓冲区最后一个字符，还有尚未输出的数词
		if(context.getCursor() == context.getAvailable() - 1){
			if(nStart != -1 && nEnd != -1){
				//输出数词
				outputNumLexeme(context);
			}
			//重置数词状态
			nReset();
		}				
	}
	
	/**
	 * 当前为NaN状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onNaNStatus(int inputStatus ,  Context context){
		if(NaN == inputStatus){
			return;
			
		}else if(NC_NP == inputStatus){//中文数词前缀
			//记录起始位置
			nStart = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;	
			
		}else if(NC_CHINESE == inputStatus){//中文数词
			//记录起始位置
			nStart = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if(NC_NE == inputStatus){//中文数词后缀
			//记录起始位置
			nStart = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
//		}else if(NC_ANP == inputStatus){//阿拉伯数字前缀
//			//记录起始位置
//			nStart = context.getCursor();
//			//记录当前的字符状态
//			nStatus = inputStatus;
			
		}else if(NC_ARABIC == inputStatus){//阿拉伯数字
			//记录起始位置
			nStart = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
//		}else if(NC_ROME == inputStatus){//罗马数字
//			//记录起始位置
//			nStart = context.getCursor();
//			//记录当前的字符状态
//			nStatus = inputStatus;
//			//记录可能的结束位置
//			nEnd = context.getCursor();	
		
		}else{
			//对NC_ANM ，NC_ANE和NC_CNM 不做处理
		}
	}
	
	
	/**
	 * 当前为ANP状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
//	private void onANPStatus(int inputStatus ,  Context context){
//		if(NC_ARABIC == inputStatus){//阿拉伯数字
//			//记录当前的字符状态
//			nStatus = inputStatus;
//			//记录可能的结束位置
//			nEnd = context.getCursor();
//			
//		}else{
//			//输出可能的数词
//			outputNumLexeme(context);
//			//重置数词状态
//			nReset();
//			//进入初始态进行处理
//			onNaNStatus(inputStatus , context);
//			
//		}
//	}
	
	
	/**
	 * 当前为ARABIC状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onARABICStatus(int inputStatus ,  Context context){
		if(NC_ARABIC == inputStatus){//阿拉伯数字
			//保持状态不变
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if(NC_ANM == inputStatus){//阿拉伯数字连接符
			//记录当前的字符状态
			nStatus = inputStatus;
			
//		}else if(NC_ANE == inputStatus){//阿拉伯数字后缀
//			//记录当前的字符状态
//			nStatus = inputStatus;
//			//记录可能的结束位置
//			nEnd = context.getCursor();
//			//输出数词
//			outputNumLexeme(context);
//			//重置数词状态
//			nReset();
		}else if(NC_CHINESE == inputStatus){//中文数字
			//记录可能的结束位置
			nEnd = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			
		}else if(NC_NE == inputStatus){//约数词
			//记录可能的结束位置
			nEnd = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			
		}else{
			//输出数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);	
			
		}
		
	}
	
	/**
	 * 当前为ANM状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onANMStatus(int inputStatus ,  Context context){
		if (NC_ARABIC == inputStatus){//阿拉伯数字
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
//		}else if (NC_ANP == inputStatus){//阿拉伯数字前缀
//			//记录当前的字符状态
//			nStatus = inputStatus;
			
		}else{
			//输出可能存在的数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);
			
		}		
	}

	
	/**
	 * 当前为ANE状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
//	private void onANEStatus(int inputStatus ,  Context context){
//		//输出可能存在的数词
//		outputNumLexeme(context);
//		//重置数词状态
//		nReset();
//		//进入初始态进行处理
//		onNaNStatus(inputStatus , context);
//				
//	}	
	
	
	/**
	 *  当前为NP状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onNPStatus(int inputStatus ,  Context context){
		if(NC_CHINESE == inputStatus){//中文数字
			//记录可能的结束位置
			nEnd = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;

			
		}else if(NC_ARABIC == inputStatus){//阿拉伯数字
			//记录可能的结束位置
			nEnd = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			
//		}else if(NC_ROME == inputStatus){//罗马数字
//			//记录可能的结束位置
//			nEnd = context.getCursor() - 1;
//			//输出可能存在的数词
//			outputNumLexeme(context);
//			//重置数词状态
//			nReset();
//			//进入初始态进行处理
//			onNaNStatus(inputStatus , context);	
			
		}else{
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);
			
		}
	}
	
	/**
	 * 当前为CHINESE状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onCHINESEStatus(int inputStatus ,  Context context){
		if(NC_CHINESE == inputStatus){//中文数字
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if(NC_CNM == inputStatus){//中文数字链接符
			//记录当前的字符状态
			nStatus = inputStatus;
			
		}else if(NC_NE == inputStatus){//中文数字结束符
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else{//其他输入
			//输出可能存在的数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);
			
		}
	}
	
	/**
	 * 当前为CNM状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onCNMStatus(int inputStatus ,  Context context){
		if(NC_CHINESE == inputStatus){//中文数字
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if(NC_NE == inputStatus){//中文数字结束符
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else{//其他输入
			//输出可能存在的数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);
			
		}		
	}
	
	/**
	 * 当前为CNE状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onCNEStatus(int inputStatus ,  Context context){
		//输出可能存在的数词
		outputNumLexeme(context);
		//重置数词状态
		nReset();
		//进入初始态进行处理
		onNaNStatus(inputStatus , context);
				
	}
	
	/**
	 * 当前为ROME状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
//	private void onROMEStatus(int inputStatus ,  Context context){
//		if(NC_ROME == inputStatus){//罗马数字
//			//记录可能的结束位置
//			nEnd = context.getCursor();
//			
//		}else{//其他输入
//			//输出可能存在的数词
//			outputNumLexeme(context);
//			//重置数词状态
//			nReset();
//			//进入初始态进行处理
//			onNaNStatus(inputStatus , context);
//			
//		}
//	}
	
	/**
	 * 添加数词词元到结果集
	 * @param context
	 */
	private void outputNumLexeme(Context context){
		if(nStart > -1 && nEnd > -1){
			//生成已切分的词元
			Lexeme newLexeme = new Lexeme(context.getBuffOffset() ,nStart , nEnd - nStart + 1 , Lexeme.TYPE_NUM );
			context.addLexeme(newLexeme);
			fCaN = true;
		}
	}
	
	/**
	 * 添加量词词元到结果集
	 * @param context
	 */
	private void outputCountLexeme(Context context){
		if(countStart > -1 && countEnd > -1){
			//生成已切分的词元
			Lexeme countLexeme = new Lexeme(context.getBuffOffset() ,countStart , countEnd - countStart + 1 , Lexeme.TYPE_NUMCOUNT);
			context.addLexeme(countLexeme);
		}

	}	
	
	/**
	 * 重置数词的状态
	 */
	private void nReset(){
		this.nStart = -1;
		this.nEnd = -1;
		this.nStatus = NaN;
	}
	
	/**
	 * 识别数字字符类型
	 * @param input
	 * @return
	 */
	private int nIdentify(char[] segmentBuff , Context context){
		
		//读取当前位置的char	
		char input = segmentBuff[context.getCursor()];
		
		int type = NaN;
		if(!AllNumberChars.contains(input)){
			return type;
		}
		
		if(CharacterHelper.isArabicNumber(input)){
			type = NC_ARABIC;
			 
		}else if(ChnNumberChars.contains(input)){
			type = NC_CHINESE;
			
		}else if(Num_Pre.indexOf(input) >= 0){
			type = NC_NP;
			
		}else if(Chn_Num_Mid.indexOf(input) >= 0){
			type = NC_CNM;
			
		}else if(NumEndChars.contains(input)){
			type = NC_NE;
			
//		}else if(ArabicNumPreChars.contains(input)){
//			type = NC_ANP;
			
		}else if(ArabicNumMidChars.contains(input)){
			type = NC_ANM;
			
//		}else if(Arabic_Num_End.indexOf(input) >= 0){
//			type = NC_ANE;
//			
//		}else if(RomeNumChars.contains(input)){
//			type = NC_ROME;

		}
		return type;
	}

	/**
	 * 处理中文量词
	 * @param segmentBuff
	 * @param context
	 */
	private void processCount(char[] segmentBuff , Context context){
		Hit hit = null;

		if(countStart == -1){
			hit = Dictionary.matchInQuantifierDict(segmentBuff , context.getCursor() , 1);
		}else{
			hit = Dictionary.matchInQuantifierDict(segmentBuff , countStart , context.getCursor() - countStart + 1);
		}
		
		if(hit != null){
			if(hit.isPrefix()){
				if(countStart == -1){
					//设置量词的开始
					countStart = context.getCursor();
				}
			}
			
			if(hit.isMatch()){
				if(countStart == -1){
					countStart = context.getCursor();
				}
				//设置量词可能的结束
				countEnd = context.getCursor();
				//输出可能存在的量词
				outputCountLexeme(context);
			}
			
			if(hit.isUnmatch()){
				if(countStart != -1){
					//重置量词状态
					countStart = -1;
					countEnd = -1;
				}
			}
		}
		
		//读到缓冲区最后一个字符，还有尚未输出的量词
		if(context.getCursor() == context.getAvailable() - 1){
			//重置量词状态
			countStart = -1;
			countEnd = -1;
		}
	}

	public void reset() {
		nStart = -1;
		nEnd = -1;
		nStatus = NaN;
		fCaN = false;
		
		countStart = -1;
		countEnd = -1;
	}

}
