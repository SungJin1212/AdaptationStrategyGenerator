package StrategyPlanner.CasebasedReasoning;

import SoS.Configuration;

import java.util.ArrayList;

public class CaseBaseElement implements Cloneable {
    private Configuration configuration;
    private ArrayList<CaseBaseValue> caseBaseValues;

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

    public double getAvgFitnessOfCaseBaseElement() {
        double ret = 0.0;

        for (CaseBaseValue caseBaseValue : caseBaseValues) {
            ret += caseBaseValue.getAverageFitness();
        }
        return ret / caseBaseValues.size();
    }
}
