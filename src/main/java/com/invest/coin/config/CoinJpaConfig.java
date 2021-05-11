package com.invest.coin.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.invest.coin.domain.entity.coin.VloatilityRangeBreakout;
import com.invest.coin.domain.repository.coin.VloatilityRangeBreakoutRepository;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        transactionManagerRef = "coinTransactionManager",
        entityManagerFactoryRef = "coinEntityManager",
        		basePackageClasses = {
                        VloatilityRangeBreakoutRepository.class
                }
)
public class CoinJpaConfig {
	
	@Primary
    @Bean
    @ConfigurationProperties(prefix = "datasource.coin.master")
    public DataSource coinDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean coinEntityManager() {
    	LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    	
    	em.setDataSource(coinDataSource());
        em.setPackagesToScan(VloatilityRangeBreakout.class.getPackage().getName());
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        em.setJpaPropertyMap(properties);
        
        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager coinTransactionManager() {
    	JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(coinEntityManager().getObject());
        return transactionManager;
    }

}
