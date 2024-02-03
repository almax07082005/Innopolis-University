// SPDX-License-Identifier: BUSL-1.1
pragma solidity ^0.8.13;

contract TicTacToe {
    /* 
        This exercise assumes you know how to manipulate nested array.
        1. This contract checks if TicTacToe board is winning or not.
        2. Write your code in `isWinning` function that returns true if a board is winning
           or false if not.
        3. Board contains 1's and 0's elements and it is also a 3x3 nested array.
    */

    function isWinning(uint8[3][3] memory board) public pure returns (bool) {
        // your code here

        uint256[3] memory rowSums;
        uint256[3] memory columnSums;
        uint256[2] memory diagonalSums;

        for (uint256 i = 0; i < board.length; i++) {
            for (uint256 j = 0; j < board[i].length; j++) {
                rowSums[i] += board[i][j];
            }
        }
        for (uint256 i = 0; i < board.length; i++) {
            for (uint256 j = 0; j < board[i].length; j++) {
                columnSums[i] += board[j][i];
            }
        }
        for (uint256 i = 0; i < board.length; i++) {
            diagonalSums[0] += board[i][i];
            diagonalSums[1] += board[i][board.length - i - 1];
        }

        for (uint256 i = 0; i < 3; i++) {
            if (rowSums[i] == 3 || rowSums[i] == 0 || columnSums[i] == 3 || columnSums[i] == 0) return true;
            if (i < 2 && (diagonalSums[i] == 3 || diagonalSums[i] == 0)) return true;
        }
        return false;
    }
}
