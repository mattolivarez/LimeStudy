package dev.mattolivarez;

public class NecessaryFunctions
{
    public Double logisticFunction(Integer input)
    {
        Double e = Math.exp(input);
        Double calc = 1 / (1 + e);
        return calc;
    }
}
