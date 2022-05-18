package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
    private Graph m_graph;
    private int m_nbMarked;

    protected List<Node> m_nodes;
    protected int m_origin;
    protected int m_size;
    protected Label[] m_labels;

    public DijkstraAlgorithm(ShortestPathData data)
    {
        super(data);

        m_graph = getInputData().getGraph();
        m_nodes = m_graph.getNodes();
        m_size = m_nodes.size();
        m_labels = new Label[m_size];
        m_origin = getInputData().getOrigin().getId();
        m_nbMarked = 0;
    }

    private void markLabel(Label label)
    {
        label.marked();
        m_nbMarked++;
    }

    protected void initLabels()
    {
        for (int i = 0; i < m_size; i++)
        {
            m_labels[i] = new Label(m_nodes.get(i));

            if (i == m_origin)
            {
                m_labels[i].setCost(0);
            }
            else
            {
                m_labels[i].setCost(Float.POSITIVE_INFINITY);
            }
        }
    }

    @Override
    protected ShortestPathSolution doRun()
    {
        final Calendar start = Calendar.getInstance();

        final ShortestPathData data = getInputData();

        initLabels();

        BinaryHeap<Label> queue = new BinaryHeap<Label>();
        ShortestPathSolution solution = null;

        queue.insert(m_labels[m_origin]);

        while (m_nbMarked < m_size)
        {
            if (queue.isEmpty())
            {
                m_nbMarked = m_size;
            }
            else
            {
                Label minLabel = queue.deleteMin();
                Node minNode = m_nodes.get(minLabel.getID());
                double minLabelCost = minLabel.getCost();

                minLabel.marked();

                m_nbMarked++;

                for (Arc arc : minNode.getSuccessors())
                {
                    if (data.isAllowed(arc))
                    {
                        Node childNode = arc.getDestination();

                        int childNodeID = childNode.getId();

                        double childCost = m_labels[childNodeID].getCost();
                        double newCost = minLabelCost + data.getCost(arc);

                        if (Double.isInfinite(childCost) && Double.isFinite(newCost))
                        {
                            notifyNodeReached(arc.getDestination());
                        }

                        if (!m_labels[childNodeID].isMarked())
                        {
                            if (childCost != Double.POSITIVE_INFINITY)
                            {
                                if (newCost < childCost)
                                {
                                    queue.remove(m_labels[childNodeID]);

                                    m_labels[childNodeID].setCost(newCost);
                                    m_labels[childNodeID].setParent(arc);

                                    queue.insert(m_labels[childNodeID]);
                                }
                            }
                            else
                            {
                                if (m_labels[childNodeID] == m_labels[data.getDestination().getId()])
                                {
                                    m_nbMarked = m_size;
                                }

                                m_labels[childNodeID].setCost(newCost);
                                m_labels[childNodeID].setParent(arc);

                                queue.insert(m_labels[childNodeID]);
                            }
                        }
                    }
                }
            }
        }

        double size = 0;

        // Destination has no predecessor, the solution is infeasible...
        if (m_labels[data.getDestination().getId()].getParent() == null)
        {
            solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        }
        else
        {
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = m_labels[data.getDestination().getId()].getParent();

            while (arc != null)
            {
                size += arc.getMinimumTravelTime();
                arcs.add(arc);
                arc = m_labels[arc.getOrigin().getId()].getParent();
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, new Path(m_graph, arcs));
        }

        final Calendar end = Calendar.getInstance();

        System.out.println("Time : " + (float)(end.getTime().getTime() - start.getTime().getTime()) / 1000.0);

        return solution;
    }
}
