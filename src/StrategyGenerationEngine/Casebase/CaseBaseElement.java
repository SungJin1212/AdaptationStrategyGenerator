package StrategyGenerationEngine.Casebase;

import Model.AbstactClass.Rule.Configuration;
import StrategyGenerationEngine.Element.CaseBaseValue;
import StrategyGenerationEngine.Element.StrategyElement;

import java.util.ArrayList;

public class CaseBaseElement {
    private Configuration configuration;
    private ArrayList <CaseBaseValue> caseBaseValues;

    public CaseBaseElement(Configuration configuration) {
        this.configuration = configuration;
        this.caseBaseValues = new ArrayList<>(0);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public ArrayList<CaseBaseValue> getCaseBaseValues() {
        return caseBaseValues;
    }

    public void setCaseBaseValues(ArrayList<CaseBaseValue> caseBaseValues) {
        this.caseBaseValues = caseBaseValues;
    }

    public void addCaseBaseValue(CaseBaseValue caseBaseValue) {
        this.caseBaseValues.add(caseBaseValue);
    }




}
