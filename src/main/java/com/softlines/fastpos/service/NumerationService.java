package com.softlines.fastpos.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Service
public class NumerationService {

    private List<Character> rounds;
    private int roundCap = 100;
    public NumerationService() {
        rounds = IntStream.rangeClosed('A', 'Z').mapToObj(i-> (char) i).collect(Collectors.toList());
    }

    public NumerationService(int roundCap) {
        this.roundCap = roundCap;
    }

    public  String mask(int number, String format)
    {
        var round = number / roundCap;
        var roundChar = rounds.get(round);
        var maskedValue = number % roundCap;
        return String.format("%c-"+format,roundChar,maskedValue);
    }
}
