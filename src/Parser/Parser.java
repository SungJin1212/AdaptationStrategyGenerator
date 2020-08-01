package Parser;

import XMLParseDataType.State;
import XMLParseDataType.Transition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    public static ArrayList<String> getParameterInformation(String url) {
        ArrayList<String> parameterList = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(url);

            Element rootElement = document.getDocumentElement();
            NodeList instanceNodes = rootElement.getElementsByTagName("INSTANCE");

            for (int i=0; i<instanceNodes.getLength(); i++) {
                Element e = (Element) instanceNodes.item(i);

                NodeList nList = e.getElementsByTagName("ATTRIBUTE");

                if(nList.item(0).getParentNode().getAttributes().getNamedItem("class").getNodeValue().equals("Configuration")) {
                    if(!nList.item(2).getTextContent().equals("")) {
                        String parsedParameter = nList.item(2).getTextContent();
                        String[] parameters = parsedParameter.split(",");
                        parameterList.addAll(Arrays.asList(parameters));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parameterList;
    }

    public static ArrayList<String> getLocalVariableInformation(String url) {
        ArrayList<String> localVariables = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(url);

            Element rootElement = document.getDocumentElement();
            NodeList instanceNodes = rootElement.getElementsByTagName("INSTANCE");

            for (int i=0; i<instanceNodes.getLength(); i++) {
                Element e = (Element) instanceNodes.item(i);

                NodeList nList = e.getElementsByTagName("ATTRIBUTE");

                if(nList.item(0).getParentNode().getAttributes().getNamedItem("class").getNodeValue().equals("Configuration")) {
                    if(!nList.item(3).getTextContent().equals("")) {
                        String parsedLocalVariables = nList.item(3).getTextContent();
                        localVariables.addAll(Arrays.asList(parsedLocalVariables.split(";")));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return localVariables;
    }

    public static String getActionCode(String url) {
        String actionCode = "";

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(url);

            Element rootElement = document.getDocumentElement();
            NodeList instanceNodes = rootElement.getElementsByTagName("INSTANCE");

            for (int i=0; i<instanceNodes.getLength(); i++) {
                Element e = (Element) instanceNodes.item(i);

                NodeList nList = e.getElementsByTagName("ATTRIBUTE");

                if(nList.item(0).getParentNode().getAttributes().getNamedItem("class").getNodeValue().equals("ActionDescription")) {
                    if(!nList.item(2).getTextContent().equals("")) {
                        actionCode = nList.item(2).getTextContent();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return actionCode;
    }



    public static ArrayList<State> getStateInformation(String url) {
        ArrayList<State> StateList = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(url);

            Element rootElement = document.getDocumentElement();
            NodeList instanceNodes = rootElement.getElementsByTagName("INSTANCE");

            for (int i=0; i<instanceNodes.getLength(); i++) {
                State s = new State();
                Element e = (Element) instanceNodes.item(i);

                NodeList nList = e.getElementsByTagName("ATTRIBUTE");

                if(nList.item(0).getParentNode().getAttributes().getNamedItem("class").getNodeValue().equals("State")) {
                    s.setStateName(nList.item(0).getParentNode().getAttributes().getNamedItem("name").getNodeValue());
                    s.setInitialState( nList.item(6).getTextContent());
                    StateList.add(s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return StateList;
    }

    public static ArrayList<Transition> getTransitionInformation(String url) {
        ArrayList <Transition> TransitionList = new ArrayList<>();
        ArrayList <Transition> From = new ArrayList<>();
        ArrayList <Transition> To = new ArrayList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(url);

            Element rootElement = document.getDocumentElement();
            NodeList transitions = rootElement.getElementsByTagName("CONNECTOR");


            for (int i=0; i<transitions.getLength(); i++) {
                Transition t = new Transition();
                Element e = (Element) transitions.item(i);

                NodeList nList = e.getElementsByTagName("ATTRIBUTE");

                String curFrom = e.getElementsByTagName("FROM").item(0).getAttributes().getNamedItem("instance").getNodeValue();
                String curTo = e.getElementsByTagName("TO").item(0).getAttributes().getNamedItem("instance").getNodeValue();
                String curGuard = nList.item(1).getTextContent();
                String curAction = nList.item(2).getTextContent();
                String curTrigger = nList.item(3).getTextContent();
                String curProbability = nList.item(4).getTextContent();


                if (curAction.equals("")) {
                    curAction = "NoAction";
                }


                if (curTo.contains("associationState")) {
                    To.add(new Transition(curFrom,curTo,curGuard,curProbability,curAction,curTrigger));
                }
                else if (curFrom.contains("associationState")) {
                    From.add(new Transition(curFrom,curTo,curGuard,curProbability,curAction,curTrigger));
                }
                else {
                    t.setFrom(curFrom);
                    t.setTo(curTo);
                    t.setGuard(curGuard);
                    t.setProbability(curProbability);
                    t.setAction(curAction);
                    t.setTrigger(curTrigger);
                    TransitionList.add(t);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Transition from : From) {
            for(Transition to : To) {
                if(from.getTo().equals(to.getFrom())) {
                    TransitionList.add(new Transition(to.getFrom(),from.getTo(),from.getGuard(),from.getProbability(),from.getAction(),from.getTrigger()));
                }
            }
        }

        return TransitionList;
    }

}
