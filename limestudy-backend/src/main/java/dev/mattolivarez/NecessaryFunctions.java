/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Contains sigmoid (logistic) function
*/

package dev.mattolivarez;

public class NecessaryFunctions
{
    public static Double logisticFunction(Integer input)
    {
        Double e = Math.exp(-1*input);
        Double calc = 1 / (1 + e);
        return calc;
    }
}
