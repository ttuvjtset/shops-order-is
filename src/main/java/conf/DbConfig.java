package conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import util.FileUtil;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = {"dao"})
public class DbConfig {
    @Autowired
    public Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.hsqldb.jdbcDriver");
        ds.setUrl(env.getProperty("db.url"));

        new JdbcTemplate(ds)
                .update(FileUtil.readFileFromClasspath("schema.sql"));
//        new JdbcTemplate(ds)
//                .update(FileUtil.readFileFromClasspath("data.sql"));

        return ds;
    }

    @Bean
    public JdbcTemplate getTemplate() {
        return new JdbcTemplate(dataSource());
    }

}