package com.yeoboya.lunch.config.security.repository;

import com.yeoboya.lunch.config.security.domain.TokenIgnoreUrl;
import com.yeoboya.lunch.config.security.reqeust.TokenIgnoreUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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


    public Optional<TokenIgnoreUrl> findTokenIgnoreUrlByUrl(String url) {
        String sql = "SELECT * FROM token_ignore_urls WHERE url = ?";
        List<TokenIgnoreUrl> results = jdbcTemplate.query(sql, new Object[]{url},
                (rs, rowNum) -> {
                    TokenIgnoreUrl tokenIgnoreUrl = new TokenIgnoreUrl();
                    tokenIgnoreUrl.setId(rs.getLong("token_ignore_id"));
                    tokenIgnoreUrl.setUrl(rs.getString("url"));
                    tokenIgnoreUrl.setIsIgnore(rs.getBoolean("is_ignore"));
                    return tokenIgnoreUrl;
                });

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public int insertOrUpdateTokenIgnoreUrl(TokenIgnoreUrlRequest tokenIgnoreUrlRequest) {
        Optional<TokenIgnoreUrl> result = this.findTokenIgnoreUrlByUrl(tokenIgnoreUrlRequest.getUrl());

        int update;
        if(result.isPresent()){
            // 존재하는 경우에는 UPDATE 문을 수행
            String updateSql = "UPDATE token_ignore_urls SET is_ignore = ? WHERE url = ?";
            update = jdbcTemplate.update(updateSql, tokenIgnoreUrlRequest.isIgnore(), tokenIgnoreUrlRequest.getUrl());
        } else {
            // 존재하지 않는 경우에는 INSERT 문을 수행
            String insertSql = "INSERT INTO token_ignore_urls(url, is_ignore) VALUES (?, ?)";
            update = jdbcTemplate.update(insertSql, tokenIgnoreUrlRequest.getUrl(), tokenIgnoreUrlRequest.isIgnore());
        }

        return update;
    }

}
