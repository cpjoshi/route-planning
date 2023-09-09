package osmxml;

import common.RoadUtils;
import graph.IRoadNetwork;
import graph.Node;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class OsmXmlElementsHandler extends DefaultHandler {
    private IRoadNetwork _graph = null;
    private WayContext _currentWay = null;
    public OsmXmlElementsHandler(IRoadNetwork roadNetwork) {
        _graph = roadNetwork;
    }
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) {
        switch (qName) {
            case "node":
                _currentWay = null;
                _graph.addNode(Long.parseLong(attributes.getValue("id")),
                        Float.parseFloat(attributes.getValue("lon")),
                        Float.parseFloat(attributes.getValue("lat")));
                break;
            case "way":
                _currentWay = new WayContext(Long.parseLong((attributes.getValue("id"))));
                break;
            case "nd":
                if(_currentWay != null) {
                    _currentWay.nodesInPath.add(Long.parseLong((attributes.getValue("ref"))));
                }
                break;
            case "tag":
                if(_currentWay != null) {
                    if(attributes.getValue("k").equals("highway")) {
                        _currentWay.isRoad = true;
                        _currentWay.roadType = attributes.getValue("v");
                    }
                }
                break;
            default:
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "way":
                if(_currentWay.isRoad && RoadUtils.isValidRoadType(_currentWay.roadType)) {
                    for(int i=1; i<_currentWay.nodesInPath.size(); i++) {
                        Node nodeFrom = _graph.getOsmNode(_currentWay.nodesInPath.get(i-1));
                        Node nodeTo = _graph.getOsmNode(_currentWay.nodesInPath.get(i));
                        int roadSpeedKmsPerHour = RoadUtils.getRoadSpeed(_currentWay.roadType);
                        double distance = RoadUtils.getDistance(nodeFrom.latitude(),
                                nodeFrom.longitude(),
                                nodeTo.latitude(),
                                nodeTo.longitude());

                        int timeTakenInSeconds = (int) Math.round((distance / (1000 * roadSpeedKmsPerHour)) * 3600);
                        _graph.addEdge(nodeFrom.osmid(), nodeTo.osmid(), timeTakenInSeconds);
                    }
                }
                _currentWay = null;
                break;
        }
    }
}
