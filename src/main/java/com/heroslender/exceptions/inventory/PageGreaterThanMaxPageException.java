package com.heroslender.exceptions.inventory;

public class PageGreaterThanMaxPageException extends IndexOutOfBoundsException {

    public PageGreaterThanMaxPageException(int page, int maxPage) {
        super(String.format("Página \"%d\" é maior que a maior página (%d)", page, maxPage));
    }
}
