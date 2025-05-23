package br.com.dio.persistence.dao;

import br.com.dio.dto.BoardcolumnDTO;
import br.com.dio.persistence.entity.BoardColumnEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.RequiredArgsConstructor;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static br.com.dio.persistence.entity.BoardColumnKindEnum.findByName;

@RequiredArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public BoardColumnEntity insert(final BoardColumnEntity entity) throws SQLException {
        var sql = "INSERT INTO BOARDS_COLUMNS (name, `order`, kind, board_id) VALUES (?, ?, ?, ?);";
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            statement.setString(i ++, entity.getName());
            statement.setInt(i ++, entity.getOrder());
            statement.setString(i ++, entity.getKind().name());
            statement.setLong(i, entity.getBoard().getId());
            statement.executeUpdate();
            if (statement instanceof StatementImpl impl){
                entity.setId(impl.getLastInsertID());
            }
            return entity;
        }
    }

    public List<BoardColumnEntity> findByBoardId(Long id) throws SQLException {
        List<BoardColumnEntity> entities = new ArrayList<>();
        var sql = "SELECT id, name, `order`, kind FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY `order`";
        try(var statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()){
                var entity = new BoardColumnEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setKind(findByName(resultSet.getString("kind")));
                entities.add(entity);
            }
        }
        return entities;
    }

    public List<BoardcolumnDTO> findByBoardIdWithDetails(Long id) throws SQLException {
        List<BoardcolumnDTO> dtos = new ArrayList<>();
        var sql =
                """
                SELECT bc.id,
                       bc.name,
                       bc.kind
                       COUNT(SELECT c.id
                                FROM CARDS c
                               WHERE c.board_columns_id = bc.id) cards_amount
                  FROM BOARDS_COLUMNS bc
                 WHERE board_id = ?
                 ORDER BY `order`
                """;
        try(var statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()){
                var dto = new BoardcolumnDTO(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        findByName(resultSet.getString("kind")),
                        resultSet.getInt("cards_amaunt")
                );
                dtos.add(dto);
            }
        }
        return dtos;
    }
}
