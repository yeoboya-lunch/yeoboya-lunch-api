package com.yeoboya.lunch.config.security.repository;

import com.yeoboya.lunch.config.security.domain.TokenIgnoreUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TokenIgnoreUrlRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<TokenIgnoreUrl> mapper = (rs, rowNum) -> {
        TokenIgnoreUrl tokenIgnoreUrl = new TokenIgnoreUrl();
        tokenIgnoreUrl.setUrl(rs.getString("url"));
        tokenIgnoreUrl.setIsIgnore(rs.getBoolean("is_ignore"));
        return tokenIgnoreUrl;
    };


    public List<TokenIgnoreUrl> getTokenIgnoreUrls() {
        return jdbcTemplate.query("SELECT url, is_ignore FROM token_ignore_urls", mapper);
    }
}
