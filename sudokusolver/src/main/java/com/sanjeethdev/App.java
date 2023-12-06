package com.sanjeethdev;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.json.JSONArray;
import org.json.JSONObject;

public class App 
{
    private static final int GRID_SIZE = 9;
    public static void main( String[] args )
    {
        HttpRequest getRequest;
        try {
            getRequest = HttpRequest.newBuilder()
            .uri(new URI("https://sudoku-api.vercel.app/api/dosuku"))
            .GET()
            .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
            
            JSONObject body = new JSONObject(getResponse.body());
            JSONObject newboard = body.getJSONObject("newboard");
            JSONArray grids = newboard.getJSONArray("grids");
            JSONArray board = grids.getJSONObject(0).getJSONArray("value");
                
            Integer[][] arrayBoard = new Integer[9][9];

            for (int i = 0; i < board.length(); i++) {
                for (int j = 0; j < board.length(); j++) {
                    arrayBoard[i][j] = (Integer) board.getJSONArray(i).get(j);
                }            
            }

            printBoard(arrayBoard);

            if (solveBoard(arrayBoard)) {
                System.out.println("\nSolved Successfully!");
            } else {
                System.out.println("Unsolvable Board X");
            }

            printBoard(arrayBoard);
                                
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }

    private static void printBoard(Integer[][] board) {
        System.out.println("+-----------------------+");
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("+-----------------------+");
            }
            for (int column = 0; column < GRID_SIZE; column++) {
                if (column % 3 == 0 && column != 0) {
                    System.out.print("| ");
                } else if (column == 0) {
                    System.out.print("| ");
                }
                
                if (board[row][column] == 0) {
                    System.out.print("_" + " ");
                } else {
                    System.out.print(board[row][column] + " ");    
                }
                
                if (column % 8 == 0 && column != 0) {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
        System.out.println("+-----------------------+");
    }

    private static boolean isNumberInRow(Integer[][] board, int number, int row) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInColumn(Integer[][] board, int number, int column) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][column] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInBox(Integer[][] board, int number, int row, int column) {
        int localBoxRow = row - row % 3;
        int localBoxColumn = column - column % 3;

        for (int i = localBoxRow; i < localBoxRow + 3; i++) {
            for (int j = localBoxColumn; j < localBoxColumn; j++) {
                if (board[i][j] == number) {
                    return true;
                    
                }
            }
        }
        return false;
    }

    private static boolean isValidPlacement(Integer[][] board, int number, int row, int column) {
        return !isNumberInRow(board, number, row) &&
        !isNumberInColumn(board, number, column) &&
        !isNumberInBox(board, number, row, column);
    }

    private static boolean solveBoard(Integer[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                if (board[row][column] == 0) {
                    for (int numberToTry = 1; numberToTry <= GRID_SIZE; numberToTry++) {
                        if (isValidPlacement(board, numberToTry, row, column)) {
                            board[row][column] = numberToTry;
                            if (solveBoard(board)) {
                                return true;
                            } else {
                                board[row][column] = 0;
                            }
                        }
                    }
                    return false;
                }
            }            
        }
        return true;
    }
}
