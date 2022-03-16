package com.softlines.fastpos.security.exceptions;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RankDuplicationException extends Exception {

    public RankDuplicationException() {
    }

    public RankDuplicationException(String message) {
        super(message);
    }
}
