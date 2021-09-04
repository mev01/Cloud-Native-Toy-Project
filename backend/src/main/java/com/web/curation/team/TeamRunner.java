package com.web.curation.team;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;

//@Component
@Slf4j
public class TeamRunner implements ApplicationRunner {

    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            log.info(connection.getMetaData().getURL());
        }
        jdbcTemplate.execute("insert into team(leader_id, sport_id, name, introduction, leader, member_count,img_path)" +
                "values(1, 1, '피지컬갤러리','빡빡이아저씨야','홍길동', 1,'test')");
    }
}
