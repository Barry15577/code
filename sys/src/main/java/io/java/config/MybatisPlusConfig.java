package io.java.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.java.modules.autoparts.utils.SqlFixUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Map;

/**
 * mybatis-plus configuration
 *
 * @author Jiasi Lu
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * Pagination plugin
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
    
    /**
     * Customize SqlSessionFactory to handle MySQL reserved keywords
     */
    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        
        // Set the mapper.xml file location
        sqlSessionFactory.setMapperLocations(
            new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml"));
        
        // Setting up a custom ObjectWrapperFactory to handle MySQL reserved keywords
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setObjectWrapperFactory(new CustomObjectWrapperFactory());
        sqlSessionFactory.setConfiguration(configuration);
        
        return sqlSessionFactory;
    }
    
    /**
     * Custom ObjectWrapperFactory class for handling MySQL reserved keywords
     */
    private static class CustomObjectWrapperFactory implements ObjectWrapperFactory {
        @Override
        public boolean hasWrapperFor(Object object) {
            return object instanceof Map;
        }

        @Override
        public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
            return new CustomMapWrapper(metaObject, (Map<String, Object>) object);
        }
    }
    
    /**
     * MapWrapper class defined to handle MySQL reserved keywords when setting properties
     */
    private static class CustomMapWrapper extends MapWrapper {
        public CustomMapWrapper(MetaObject metaObject, Map<String, Object> map) {
            super(metaObject, map);
        }

        @Override
        public String findProperty(String name, boolean useCamelCaseMapping) {
            // If the property name is a reserved keyword, add backticks.
            if (SqlFixUtil.isReservedKeyword(name)) {
                return SqlFixUtil.getQuotedColumnName(name);
            }
            return super.findProperty(name, useCamelCaseMapping);
        }
    }
}
