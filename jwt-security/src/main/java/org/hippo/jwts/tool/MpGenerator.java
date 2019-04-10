package org.hippo.jwts.tool;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MpGenerator {

  /**
   * <p>
   * MySQL 生成演示
   * </p>
   */
  public static void main(String[] args) {
    AutoGenerator mpg = new AutoGenerator();
    // 全局配置
    GlobalConfig gc = new GlobalConfig();
    gc.setOutputDir("/Users/dujf/Downloads/code");
    gc.setFileOverride(true);
    gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
    gc.setEnableCache(false);// XML 二级缓存
    gc.setBaseResultMap(true);// XML ResultMap
    gc.setBaseColumnList(false);// XML columList
    gc.setAuthor("dujf");

    // 自定义文件命名，注意 %s 会自动填充表实体属性！
    gc.setMapperName("%sMapper");
    gc.setXmlName("%sMapper");
    gc.setServiceName("I%sService");
    gc.setServiceImplName("%sServiceImpl");
    gc.setControllerName("%sController");
    mpg.setGlobalConfig(gc);

    // 数据源配置
    DataSourceConfig dsc = new DataSourceConfig();
    dsc.setDbType(DbType.MYSQL);

    dsc.setDriverName("com.mysql.jdbc.Driver");
    dsc.setUsername("root");
    dsc.setPassword("123456");
    dsc.setUrl("jdbc:mysql://127.0.0.1:3306/shiro_manage?autoReconnect=true&useUnicode=true&characterEncoding=utf8&useSSL=false");
    mpg.setDataSource(dsc);

    // 策略配置
    StrategyConfig strategy = new StrategyConfig();
    strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
    mpg.setStrategy(strategy);

    // 包配置
    PackageConfig pc = new PackageConfig();
    pc.setParent("com.github.security");
    pc.setController("controller");
    pc.setServiceImpl("service");
    pc.setService("service");
    pc.setEntity("po");
    pc.setMapper("mapper");
    mpg.setPackageInfo(pc);


    // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
    InjectionConfig cfg = new InjectionConfig() {
      @Override
      public void initMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
        this.setMap(map);
      }
    };

    // 自定义 xxList.jsp 生成
    List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
    // 调整 xml 生成目录演示
    focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
      @Override
      public String outputFile(TableInfo tableInfo) {
        return "/Users/dujf/Downloads/code/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
      }
    });
    cfg.setFileOutConfigList(focList);
    mpg.setCfg(cfg);

    // 关闭默认 xml 生成，调整生成 至 根目录
    TemplateConfig tc = new TemplateConfig();
    tc.setXml(null);
    mpg.setTemplate(tc);
    // 执行生成
    mpg.execute();

  }

}