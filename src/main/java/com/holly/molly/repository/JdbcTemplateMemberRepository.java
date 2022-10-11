package com.holly.molly.repository;

import com.holly.molly.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository{//template & method pattern
    private final JdbcTemplate jdbcTemplate;

    @Autowired//생성자가 1개면 @Autowired생략가능
    public JdbcTemplateMemberRepository(DataSource dataSource){//H2
        jdbcTemplate=new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert=new SimpleJdbcInsert(jdbcTemplate);//jdbcTemplate에 넣기 직전 버퍼 개념(데이터인스턴스를 받을 담을)
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("innerId");//넣을 테이블 이름은 member이고 id를 키로 사용해

        Map<String, Object> parameters=new HashMap<>();//파라미터를 담아 jdbcInsert에 전달하기 위함
        parameters.put("name", member.getName());
        parameters.put("password", member.getPassword());
        parameters.put("emailAddress", member.getEmailAddress());

        Number key=jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));//MapSql~은 sql문을 위해 파라미터를 감싸는 담는 버퍼 개념. 생성한 키값을 리턴한다.
        member.setInnerId(key.longValue());//db에서 생성하여 반환한 값으로 id를 세팅, 반환한다.
        return member;
    }

    @Override
    public Optional<Member> findById(Long innerId) {
        List<Member> result=jdbcTemplate.query("select * from member where innerId = ?", memberRowMapper(), innerId);//리스트로 반환
        return result.stream().findAny();//결과리스트중 아무거나 값이 있는걸 찾으면 리턴.
    }

    @Override
    public Optional<Member> findByEmail(String emailAddress) {
        List<Member> result=jdbcTemplate.query("select * from member where emailAddress = ?", memberRowMapper(), emailAddress);//리스트로 반환
        return result.stream().findAny();//결과리스트중 아무거나 값이 있는걸 찾으면 리턴.
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper(){//ResultSet의 결과를 해당 객체에 맞게 세팅하여 전달하는 역활
        return (rs, rowNum)->{//ResultSet, 개수
            Member member=new Member();
            member.setInnerId(rs.getLong("innerId"));
            member.setName(rs.getString("name"));
            member.setPassword(rs.getString("password"));
            member.setEmailAddress(rs.getString("emailAddress"));
            return member;
        };
    }
}
