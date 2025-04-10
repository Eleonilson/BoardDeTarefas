package br.com.dio.dto;

import br.com.dio.persistence.entity.BoardColumnKindEnum;

public record BoardcolumnDTO(Long id,
                             String name,
                             BoardColumnKindEnum kind,
                             int cardsAmount) {
}
