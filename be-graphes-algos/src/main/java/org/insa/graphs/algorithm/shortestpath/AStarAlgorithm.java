package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

import static org.insa.graphs.algorithm.AbstractInputData.Mode.TIME;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data)
    {
        super(data);
    }

    @Override
    protected void initLabels()
    {
        for (int i = 0; i < m_size; i++)
        {
            Node node = m_nodes.get(i);
            Node dst  = getInputData().getDestination();

            double cost = node.getPoint().distanceTo(dst.getPoint());

            if (getInputData().getMode() == TIME)
            {
                double maxSpeed = getInputData().getGraph().getGraphInformation().getMaximumSpeed();
                cost /= maxSpeed;
                cost *= 10;
            }

            m_labels[i] = new LabelStar(node, cost);

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
}
