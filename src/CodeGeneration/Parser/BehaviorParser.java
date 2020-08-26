package CodeGeneration.Parser;

import CodeGeneration.XMLParseDataType.ActionDescription;
import CodeGeneration.XMLParseDataType.State;
import CodeGeneration.XMLParseDataType.Transition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.Arrays;

public class BehaviorParser {

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


    public static String getType(String url) {
        String type = "";

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
                    if(!nList.item(4).getTextContent().equals("")) {
                        type = nList.item(4).getTextContent();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return type;
    }

    public static ArrayList<ActionDescription> getActionCode(String url) {
        ArrayList<ActionDescription> actionDescriptions = new ArrayList<>(0);
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(url);

            Element rootElement = document.getDocumentElement();
            NodeList instanceNodes = rootElement.getElementsByTagName("INSTANCE");

            for (int i=0; i<instanceNodes.getLength(); i++) {
                ActionDescription actionDescription = new ActionDescription();
                Element e = (Element) instanceNodes.item(i);

                NodeList nList = e.getElementsByTagName("ATTRIBUTE");

                if(nList.item(0).getParentNode().getAttributes().getNamedItem("class").getNodeValue().equals("ActionDescription")) {
                    if(!nList.item(2).getTextContent().equals("")) {
                        actionDescription.setEffect(nList.item(2).getTextContent());
                        actionDescription.setActionName(nList.item(3).getTextContent());
                        actionDescriptions.add(actionDescription);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return actionDescriptions;
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
                    s.setInitialState( nList.item(6).getTextContent()); // Initial State
                    s.setTime(nList.item(7).getTextContent()); // Time
                    s.setAtomic(nList.item(8).getTextContent()); // Atomic
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
        ArrayList <Transition> FromAssociation = new ArrayList<>();
        ArrayList <Transition> ToAssociation = new ArrayList<>();

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
                    ToAssociation.add(new Transition(curFrom,curTo,curGuard,curProbability,curAction,curTrigger));
                }
                else if (curFrom.contains("associationState")) {
                    FromAssociation.add(new Transition(curFrom,curTo,curGuard,curProbability,curAction,curTrigger));
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

        for(Transition to : ToAssociation) {
            for(Transition from : FromAssociation) {
                if(to.getTo().equals(from.getFrom())) {
                    TransitionList.add(new Transition(to.getFrom(),from.getTo(),to.getGuard(),to.getProbability(),to.getAction(),to.getTrigger()));
                    //TransitionList.add(new Transition(to.getFrom(),from.getTo(),from.getGuard(),from.getProbability(),from.getAction(),from.getTrigger()));
                }
            }
        }

        return TransitionList;
    }

}
