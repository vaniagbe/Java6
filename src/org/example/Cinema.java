package org.example;import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

public class Cinema {
    public int[][][] cinema;

    public Cinema(int halls, int rows, int seats) {
        cinema = new int[halls][rows][seats];
    }

    public boolean bookSeats(int hallNumber, int row, int[] seats) {
        if (hallNumber >= cinema.length || row >= cinema[hallNumber].length
                || Arrays.stream(seats).anyMatch(seat -> seat >= cinema[hallNumber][row].length)) {
            System.err.println("Incorrect input");
            return false;
        }
        if (isBooked(hallNumber, row, seats)) {
            System.err.println("One or more of these seats are already booked");
            return false;
        }

        Arrays.stream(seats).forEach(seat -> cinema[hallNumber][row][seat] = 1);
        return true;
    }

    public boolean cancelBooking(int hallNumber, int row, int[] seats) {
        if (hallNumber >= cinema.length || row >= cinema[hallNumber].length
                || Arrays.stream(seats).anyMatch(seat -> seat >= cinema[hallNumber][row].length)) {
            System.err.println("Incorrect input");
            return false;
        }

        Arrays.stream(seats).forEach(seat -> cinema[hallNumber][row][seat] = 0);
        return true;
    }

    public boolean checkAvailability(int screen, int numSeats) {
        if (screen >= cinema.length || numSeats > cinema[screen][0].length) {
            System.err.println("Incorrect input");
            return false;
        }

        for (int row = 0; row < cinema[screen].length; row++) {
            first: for (int firstPtr = 0; firstPtr <= cinema[screen][row].length - numSeats; firstPtr++) {
                for (int secondPtr = 0; secondPtr < numSeats; secondPtr++) {
                    if (cinema[screen][row][firstPtr + secondPtr] == 1) {
                        firstPtr += secondPtr;
                        continue first;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void printSeatingArrangement(int hallNumber) {
        final String RESET = "\u001B[0m";
        final String BLACK_BG = "\u001B[40m\u001B[32m";
        final String YELLOW_BG = "\u001B[43m\u001B[31m";

        String header = buildHeader(hallNumber);
        StringBuilder output = new StringBuilder(header);
        for (int row = 0; row < cinema[hallNumber].length; row++) {
            output.append((row + 1 >= 10) ? " " : "  ").append(String.format("%d |%s", row + 1, BLACK_BG));
            for (var seat : cinema[hallNumber][row]) {
                output.append(seat == 0 ? String.format("\t%s0", BLACK_BG)
                        : String.format("%s\t1", YELLOW_BG));
            }
            output.append(String.format("\t%s| %d  \n", RESET, row + 1));
        }
        output.append(header);
        System.out.println(output);
    }


    private boolean isBooked(int hallNumber, int row, int[] seats) {
        return Arrays.stream(seats)
                .anyMatch(seat -> cinema[hallNumber][row][seat] == 1);
    }

    private String buildHeader(int hallNumber) {
        StringBuilder header = new StringBuilder("\t");
        for (int seat = 0; seat < cinema[hallNumber][0].length; seat++) {
            if ((seat + 1) / 10 == 0) {
                header.append(String.format("\t%d", seat + 1));
            } else {
                header.append(String.format("  %d", seat + 1));
            }
        }
        header.append("\t\t\n");
        return header.toString();
    }

    public Optional<Integer>[][] buildArrayOfCosts(int hallNumber) {
        Optional<Integer>[][] result = new Optional[cinema[hallNumber].length][cinema[hallNumber][0].length];
        for (int row = 0; row < Math.ceil(cinema[hallNumber].length / 2.0); row++) {
            for (int seat = 0, cost = row; seat < Math.ceil(result[row].length / 2.0); seat++, cost++) {
                result[row][seat]
                        = result[row][result[row].length - 1 - seat]
                        = result[result.length - 1 - row][seat]
                        = result[result.length - 1 - row][result[row].length - 1 - seat]
                        = Optional.of(cost);
            }
        }
        return result;
    }
}