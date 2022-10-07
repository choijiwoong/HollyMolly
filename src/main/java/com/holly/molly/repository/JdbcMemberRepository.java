package com.holly.molly.repository;

import com.holly.molly.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository{

    private final DataSource dataSource;

    public JdbcMemberRepository(DataSource dataSource){
        this.dataSource=dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql="insert into member(name) values(?)";
        Connection conn=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;

        try{
            conn=getConnection();
            pstmt=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);//parameterIndex를 이용해 값을 넣을 예정

            pstmt.setLong(1, member.getInnerId());//pstmt의 key를 사용하여 db항목들을 매칭
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmailAddress());
            pstmt.setString(4, member.getPassword());
            pstmt.executeUpdate();
            rs=pstmt.getGeneratedKeys();//해당 키를 이용하여 결과를 저장한다.

            if(rs.next()){//읽어들여진다면
                member.setInnerId(rs.getLong(1));
                member.setName(rs.getString(2));
                member.setEmailAddress(rs.getString(3));
                member.setPassword(rs.getString(4));
            } else{
                throw new SQLException("db 조회 실패");
            }
            return member;
        } catch(Exception e){
            throw new IllegalStateException(e);
        } finally{
            close(conn, pstmt, rs);//필수!! 중요
        }
    }

    @Override
    public Optional<Member> findById(Long innerId) {
        String sql="select * from member where innerId = ?";

        Connection conn=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;

        try{
            conn=getConnection();
            pstmt=conn.prepareStatement(sql);
            pstmt.setLong(1, innerId);

            rs=pstmt.executeQuery();

            if(rs.next()){
                Member member=new Member();
                member.setInnerId(rs.getLong(1));
                member.setName(rs.getString(2));
                member.setEmailAddress(rs.getString(3));
                member.setPassword(rs.getString(4));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch(Exception e){
            throw new IllegalStateException(e);
        } finally{
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByEmail(String emailAddress) {
        String sql="select * from member where emailaddress = ?";

        Connection conn=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;

        try{
            conn=getConnection();
            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1, emailAddress);

            rs=pstmt.executeQuery();

            if(rs.next()){
                Member member=new Member();
                member.setInnerId(rs.getLong(1));
                member.setName(rs.getString(2));
                member.setEmailAddress(rs.getString(3));
                member.setPassword(rs.getString(4));
                return Optional.of(member);
            }
            return Optional.empty();
        } catch(Exception e ){
            throw new IllegalStateException(e);
        } finally{
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql="select * from member";

        Connection conn=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;

        try{
            conn=getConnection();
            pstmt=conn.prepareStatement(sql);

            rs=pstmt.executeQuery();

            List<Member> members=new ArrayList<>();
            while(rs.next()){
                Member member=new Member();
                member.setInnerId(rs.getLong(1));
                member.setName(rs.getString(2));
                member.setEmailAddress(rs.getString(3));
                member.setPassword(rs.getString(4));
                members.add(member);
            }

            return members;
        } catch(Exception e){
            throw new IllegalStateException(e);
        } finally{
            close(conn, pstmt, rs);
        }
    }


    private Connection getConnection(){//연결시 DataSourceUtils의 Connection을 이용
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try{
            if(rs!=null){
                rs.close();
            }
        } catch(SQLException e){
            e.printStackTrace();
        }

        try{
            if(pstmt!=null){
                pstmt.close();
            }
        } catch(SQLException e){
            e.printStackTrace();
        }

        try{
            if(conn!=null){
                close(conn);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    private void close(Connection conn) throws SQLException{//해제시 DataSourceUtils의 release를 이용
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}