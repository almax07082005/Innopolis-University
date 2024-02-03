// SPDX-License-Identifier: BUSL-1.1
pragma solidity ^0.8.13;

contract IsPrime {
    /**
     * The goal of this exercise is to return if "number" is prime or not (true or false)
     */
    function isPrime(uint256 number) public view returns (bool) {
        // your code here

        if (number == 1) return false;

        for (uint256 i = 2; i < number; i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}
