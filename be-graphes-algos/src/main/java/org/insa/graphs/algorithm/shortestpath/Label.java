package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label>
{
    private int m_nodeID;
    private double m_cost;
    private boolean m_marked;
    private Arc m_parent;

    Label(Node node)
    {
        m_nodeID = node.getId();
        m_cost = Double.POSITIVE_INFINITY;
        m_marked = false;
        m_parent = null;
    }

    public void setCost(double cost)
    {
        m_cost = cost;
    }

    public void setParent(Arc parent)
    {
        m_parent = parent;
    }

    public void marked()
    {
        m_marked = true;
    }

    public double getCost()
    {
        return m_cost;
    }

    public int getID()
    {
        return m_nodeID;
    }

    public boolean isMarked()
    {
        return m_marked;
    }

    public double getTotalCost() { return m_cost; }

    public Arc getParent() { return m_parent; }

    public int compareTo(Label o) { return (getTotalCost() < o.getCost())?-1:1; }
}