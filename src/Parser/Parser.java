package Parser;

import Data.State;
import Data.Transition;
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
                    String allparameter = nList.item(2).getTextContent();
                    String[] parameters = allparameter.split(",");
                    parameterList.addAll(Arrays.asList(parameters));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parameterList;
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
                    s.setInitialState( nList.item(5).getTextContent());
                    s.setStateName(nList.item(6).getTextContent());
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


                t.setFrom(e.getElementsByTagName("FROM").item(0).getAttributes().getNamedItem("instance").getNodeValue());
                t.setTo(e.getElementsByTagName("TO").item(0).getAttributes().getNamedItem("instance").getNodeValue());
                t.setGuard(nList.item(1).getTextContent());
                t.setProbability(nList.item(2).getTextContent());
                t.setAction(nList.item(3).getTextContent());
                t.setTrigger(nList.item(4).getTextContent());

                if (t.getAction().equals("")) t.setAction("NoAction");

                TransitionList.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return TransitionList;

    }
}
