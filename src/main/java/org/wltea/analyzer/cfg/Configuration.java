package org.wltea.analyzer.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.seg.CJKSegmenter;
import org.wltea.analyzer.seg.ISegmenter;
import org.wltea.analyzer.seg.LetterSegmenter;
import org.wltea.analyzer.seg.QuantifierSegmenter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * IK Analyzer v3.2
 * 简单的配置管理类,单子模式
 * @author 林良益
 *
 */
public class Configuration {

	private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

	/*
	 * 分词器配置文件路径
	 */	
	private static final String FILE_NAME = "/IKAnalyzer.cfg.xml";
	//配置属性——扩展字典
	private static final String EXT_DICT = "ext_dict";
	//配置属性——扩展停止词典
	private static final String EXT_STOP = "ext_stopwords";
	
	private static final Configuration CFG = new Configuration();
	
	private Properties props;
	
	/*
	 * 初始化配置文件
	 */
	private Configuration(){
		
		props = new Properties();
		InputStream input = Configuration.class.getResourceAsStream(FILE_NAME);
		if(input != null){
			try {
				props.loadFromXML(input);
				logger.debug("load config as {}", props.toString());
			} catch (IOException e) {
			    logger.error("load ik config file failed, {}", e);
				e.printStackTrace();
			}
		} else {
		    logger.error("ik config file {} read failed", FILE_NAME);
        }
	}
	
	/**
	 * 获取扩展字典配置路径
	 * @return List<String> 相对类加载器的路径
	 */
	public static List<String> getExtDictionarys(){
		List<String> extDictFiles = new ArrayList<>(2);
		String extDictCfg = CFG.props.getProperty(EXT_DICT);
		if(extDictCfg != null){
			//使用;分割多个扩展字典配置
			loadExtFiles(extDictFiles, extDictCfg);
		}		
		return extDictFiles;		
	}

	private static void loadExtFiles(List<String> extDictFiles, String extDictCfg) {
		String[] filePaths = extDictCfg.split(";");
		for(String filePath : filePaths){
            if(filePath != null && !"".equals(filePath.trim())){
                extDictFiles.add(filePath.trim());
                //System.out.println(filePath.trim());
                logger.debug("load dic file {}", filePath.trim());
            }
        }
	}

	/**
	 * 获取扩展停止词典配置路径
	 * @return List<String> 相对类加载器的路径
	 */
	public static List<String> getExtStopWordDictionarys(){
		List<String> extStopWordDictFiles = new ArrayList<>(2);
		String extStopWordDictCfg = CFG.props.getProperty(EXT_STOP);
		if(extStopWordDictCfg != null){
			//使用;分割多个停止词典配置
			loadExtFiles(extStopWordDictFiles, extStopWordDictCfg);
		}		
		return extStopWordDictFiles;		
	}
		
	
	/**
	 * 初始化子分词器实现
	 * （目前暂时不考虑配置扩展）
	 * @return List<ISegmenter>
	 */
	public static List<ISegmenter> loadSegmenter(){
		//初始化词典单例
//        Dictionary instance = Dictionary.getInstance();

        List<ISegmenter> segmenters = new ArrayList<>(4);
		//处理数量词的子分词器
		segmenters.add(new QuantifierSegmenter());
		//处理中文词的子分词器
		segmenters.add(new CJKSegmenter());
		//处理字母的子分词器
		segmenters.add(new LetterSegmenter()); 
		return segmenters;
	}
}
