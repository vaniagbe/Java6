package org.example;

import org.example.Cinema;

public class Main {
    public static void main(String[] args) {
        Cinema cinema = new Cinema(5, 10, 20);
        cinema.bookSeats(0, 4, new int[]{7, 8, 9, 10});
        cinema.printSeatingArrangement(0);
    }
}