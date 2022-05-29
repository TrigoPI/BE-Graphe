package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class LabelStar extends Label implements Comparable<Label>
{
    private double m_ACost;

    LabelStar(Node node, double ACost)
    {
        super(node);
        m_ACost = ACost;
    }

    public double getACost() { return m_ACost; }

    @Override
    public double getTotalCost() { return getCost() + m_ACost; }

    public int compareTo(Label o)
    {
        return (getTotalCost() < ((LabelStar)o).getTotalCost())?-1:1;
    }
}
