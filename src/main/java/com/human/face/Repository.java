package com.human.face;

import com.human.face.Dto.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Repository
public class Repository {
    JdbcTemplate jdbcTemplate;

    public Repository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getNewExperimentNum() {
        String sql = "insert into experimenter(name) value ('minho');";
        jdbcTemplate.update(sql);
        String getSql = "select id from experimenter order by id DESC LIMIT 1;";
        List<Integer> id = jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return Integer.valueOf(rs.getInt("id"));
        });

        return id.get(0);
    }

    public void createMoveTable(int experimenter_id) {
        String tableName = experimenter_id + "_user_moves";
        String createSql = "create table " + tableName + " (" +
                "move_id int auto_increment primary key," +
                "type varchar(20), seq int, ray_x float, ray_y float, ray_z float, " +
                "ray_reached boolean, ray_selected boolean, time_ms long);";

        jdbcTemplate.update(createSql);
    }

    public void saveTarget(int experimentId, String type, TargetRequest targetRequest) {
        String saveSql = "insert into target_info (experimenter_id, type, seq, time_ms, position_x, position_y, position_z) values (?, ?, ?, ?, ?, ?, ?);";
        float[] position = targetRequest.getPosition();
        jdbcTemplate.update(saveSql, experimentId, type, targetRequest.getSeq(), targetRequest.getTime_ms(), position[0], position[1], position[2]);
    }

    public void saveUserMove(int experimentId, String type, int sequence, UserMoveRequest userMoveRequest) {
        String tableName = experimentId + "_user_moves";
        String saveSql = "insert into " + tableName + "(type, seq, ray_x, ray_y, ray_z, ray_reached, ray_selected, time_ms) values (?, ?, ?, ?, ?, ?, ?, ?);";
        float[] ray = userMoveRequest.getRay_xyz();
        jdbcTemplate.update(saveSql, type, sequence, ray[0], ray[1], ray[2],
                userMoveRequest.isRay_reached(), userMoveRequest.isRay_selected(), userMoveRequest.getTime_ms());
    }

    public void saveTargetDone(int experimentId, String type, int sequence, UserMoveRequest userMoveRequest) {
        String sql = "insert into user_done(experimenter_id, type, seq, ray_x, ray_y, ray_z, time_ms) values (?, ?, ?, ?, ?, ?, ?);";
        float[] ray = userMoveRequest.getRay_xyz();
        jdbcTemplate.update(sql, experimentId, type, sequence, ray[0], ray[1], ray[2], userMoveRequest.getTime_ms());
    }


    public List<UserPosition> getUserPosition(int experimentId) {
        String getSql = "select type, seq, position_x, position_y, position_z, time_ms from target_info where" +
                " experimenter_id = ? order by info_id asc;";
        return jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return new UserPosition(rs.getString("type"), rs.getInt("seq"),
                    rs.getFloat("position_x"), rs.getFloat("position_y"), rs.getFloat("position_z"),
                    rs.getLong("time_ms"));
        }, experimentId);
    }

    public List<UserMove> getUserMoves(int experimentId, String type, int seq) {
        String tableName = experimentId + "_user_moves";
        String getSql = "select ray_x, ray_y, ray_z, ray_reached, ray_selected, time_ms from " + tableName + " " +
                "where type = ? and seq = ?;";
        return jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return new UserMove(rs.getFloat("ray_x"), rs.getFloat("ray_y"), rs.getFloat("ray_z"),
                    rs.getBoolean("ray_reached"), rs.getBoolean("ray_selected"), rs.getLong("time_ms"));
        }, type, seq);
    }

    public List<UserMove> getUserDoneMoves(int experimentId, String type, int seq) {
        String getSql = "select ray_x, ray_y, ray_z, time_ms from user_done where experimenter_id = ? and type = ? and seq = ?;";
        return jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return new UserMove(rs.getFloat("ray_x"), rs.getFloat("ray_y"), rs.getFloat("ray_z"),
                    true, true, rs.getLong("time_ms"));
        }, experimentId, type, seq);
    }
}
