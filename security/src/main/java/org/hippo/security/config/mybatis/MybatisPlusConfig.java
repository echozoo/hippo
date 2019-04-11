package org.hippo.security.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dujf
 */
@Configuration
@MapperScan("org.hippo")
public class MybatisPlusConfig {

  /**
   * 分页插件，自动识别数据库类型
   * 多租户，请参考官网【插件扩展】
   */
  @Bean
  public PaginationInterceptor paginationInterceptor() {
    return new PaginationInterceptor();
  }

  /**
   * SQL执行效率插件
   * 设置 dev test 环境开启
   */
  @Bean
//  @Profile({ "dev", "test" })
  public PerformanceInterceptor performanceInterceptor() {
    return new PerformanceInterceptor();
  }
}