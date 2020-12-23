package com.sashutosh.streams.exercise;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

public class Exercise {

    Trader raoul = new Trader("Raoul", "Cambridge");
    Trader mario = new Trader("Mario", "Milan");
    Trader alan = new Trader("Alan", "Cambridge");
    Trader brian = new Trader("Brian", "Cambridge");

    List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
    );

    public static void main(String[] args) {
        Exercise exercise = new Exercise();
        System.out.println("1. Transactions in 2011 sorted by value " + exercise.allTransactionsIn2011Sorted());

        System.out.println("2. All unique cities traders live in  " + exercise.allUniqueCitiesOfTraders());

        System.out.println("3. All traders from cambridge sorted by name  " + exercise.allTradersFromCambridgeSortedByName());

        System.out.println("4. String of all traders sorted by name  " + exercise.allTraderNameStringSorted());

        System.out.println("5. Any trader living in Milan  " + exercise.anyTraderInMilan());

        System.out.println("6. All transactions from the traders living in Cambridge  " + exercise.allTradersFromCambridgeTransactionValue());

        System.out.println("7. Highest value of all the transactions  " + exercise.highestTransactionValue());


    }

    public List<Transaction> allTransactionsIn2011Sorted() {
        return transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(comparing(Transaction::getValue))
                .collect(Collectors.toList());
    }

    public Set<String> allUniqueCitiesOfTraders() {
        return transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getCity)
                .collect(Collectors.toSet());
    }

    public List<Trader> allTradersFromCambridgeSortedByName() {
        return transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .distinct()
                .sorted(comparing(Trader::getName))
                .collect(Collectors.toList());
    }

    public String allTraderNameStringSorted() {
        return transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .distinct()
                .sorted()
                .collect(joining());
    }

    public boolean anyTraderInMilan() {
        return transactions.stream()
                .map(Transaction::getTrader).anyMatch(trader -> trader.getCity().equals("Milan"));
    }

    public int allTradersFromCambridgeTransactionValue() {

        return transactions.stream()
                .filter(t -> t.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .reduce(0, Integer::sum);
    }

    public int highestTransactionValue() {
        return transactions.stream()
                .map(Transaction::getValue)
                .reduce(0, Integer::max);

    }


}
