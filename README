IKAnalyzer是一个开源的，基于java语言开发的轻量级的中文分词工具包。从2006年12月推出1.0版开始，IKAnalyzer已经推出了3个大版本。最初，它是以开源项目Luence为应用主体的，结合词典分词和文法分析算法的中文分词组件。新版本的 IKAnalyzer3.0则发展为面向Java的公用分词组件，独立于Lucene项目，同时提供了对Lucene的默认优化实现。 

IKAnalyzer3.0特性:

采用了特有的“正向迭代最细粒度切分算法“，支持细粒度和最大词长两种切分模式；具有83万字/秒（1600KB/S）的高速处理能力。

采用了多子处理器分析模式，支持：英文字母、数字、中文词汇等分词处理，兼容韩文、日文字符

优化的词典存储，更小的内存占用。支持用户词典扩展定义

针对Lucene全文检索优化的查询分析器IKQueryParser(作者吐血推荐)；引入简单搜索表达式，采用歧义分析算法优化查询关键字的搜索排列组合，能极大的提高Lucene检索的命中率。

IKAnalyzer的作者为林良益（linliangyi2007@gmail.com），项目网站为http://code.google.com/p/ik-analyzer/。

Maven工程由王坤山创建（wks1986@gmail.com）。创建的目的是为了方便用于其他Maven工程。你可以在 https://github.com/wks/ik-analyzer 网站找到本工程。

Maven用法：

将以下依赖加入工程的pom.xml中的<dependencies>...</dependencies>部分。
    <dependency>
        <groupId>org.wltea.ik-analyzer</groupId>
        <artifactId>ik-analyzer</artifactId>
        <version>3.2.8</version>
	</dependency>

在IK Analyzer加入Maven Central Repository之前，你需要手动安装，安装到本地的repository，或者上传到自己的Maven repository服务器上。

要安装到本地Maven repository，使用如下命令，将自动编译，打包并安装：
mvn install -Dmaven.test.skip=true

