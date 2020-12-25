package com.heroslender.exceptions.inventory;

public class InvalidInventorySizeException extends IllegalArgumentException {

    public InvalidInventorySizeException(int used) {
        super(String.format("%d não é múltiplo de 9. O tamanho dos inventários tem de ser múltiplo de 9!", used));
    }
}
