package br.com.dio.ui;

import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.service.BoardQueryService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    private final BoardEntity entity;

    public void execute() {
        try {
            System.out.printf("Bem vindo ao board %s, selecione a operação desejada", entity.getId());
            var option = -1;
            while (option != 9) {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Ver board");
                System.out.println("7 - Ver colunas com cards");
                System.out.println("8 - Ver card");
                System.out.println("9 - Voltar para o menu anterior um card");
                System.out.println("10 - sair");
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> createcard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> BlockCard();
                    case 4 -> unBlockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showClumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para o menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválid, informe uma opção do menu");
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            System.exit(8);
        }
    }


    private void createcard() {
    }

    private void moveCardToNextColumn() {
    }

    private void BlockCard() {
    }

    private void unBlockCard() {
    }

    private void cancelCard() {
    }

    private void showBoard() throws SQLException {
        try(var connection = getConnection()){
            var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.columns().forEach(c ->
                    System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardsAmount())
                );
            });
        }
    }

    private void showClumn() {
    }

    private void showCard() {
    }
}
