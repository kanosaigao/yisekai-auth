package com.yisekai.manager.config;


import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.AllArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.LocalCacheScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;


/**
 * mybatis plus配置中心
 */
@Configuration
@AllArgsConstructor
public class MybatisPlusConfiguration implements MetaObjectHandler {



	/**
	 * 单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
	 */
	private static final Long MAX_LIMIT = 1000L;


	/**
	 * 新的分页插件,一缓和二缓遵循mybatis的规则,
	 * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false
	 * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
	 */
	@Bean
	public MybatisPlusInterceptor paginationInterceptor() {

		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

		//分页插件: PaginationInnerInterceptor
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		paginationInnerInterceptor.setMaxLimit(MAX_LIMIT);
		interceptor.addInnerInterceptor(paginationInnerInterceptor);
		return interceptor;
	}
	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return configuration -> {
			configuration.setCallSettersOnNulls(true);
			//关闭一级缓存配置
			configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
			configuration.setLogImpl(org.apache.ibatis.logging.slf4j.Slf4jImpl.class);
		};
	}


	@Override
	public void insertFill(MetaObject metaObject) {
		Date currentTime = new Date();
		this.strictInsertFill(metaObject, "createTime", Date.class, currentTime);
		this.strictInsertFill(metaObject, "updateTime", Date.class, currentTime);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Date currentTime = new Date();
		//即使有值也会更新
		this.setFieldValByName("updateTime", currentTime, metaObject);
	}


}
