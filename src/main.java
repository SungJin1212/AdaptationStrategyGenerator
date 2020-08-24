import Model.GeneratedCode.Rule.CleaningSoSConfiguration;
        import Model.GeneratedCode.Rule.CleaningSoSEnvironmentCondition;

        import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<CleaningSoSEnvironmentCondition> expectedCleaningSoSEnvironmentConditions = new ArrayList<>(0);
        expectedCleaningSoSEnvironmentConditions.add(new CleaningSoSEnvironmentCondition(10));
        expectedCleaningSoSEnvironmentConditions.add(new CleaningSoSEnvironmentCondition(20));

        ArrayList<CleaningSoSConfiguration> cleaningSoSConfigurations = new ArrayList<>(0);
        cleaningSoSConfigurations.add(new CleaningSoSConfiguration(5, 5, 5, 5));
        //cleaningSoSConfigurations.add(new CleaningSoSConfiguration(10, 10, 10, 10));
        cleaningSoSConfigurations.add(new CleaningSoSConfiguration(15, 15, 15, 15));

        CleaningSoSConfiguration initialCleaningSoSConfiguration = new CleaningSoSConfiguration(8, 8, 8, 8);
        CleaningSoSEnvironmentCondition initialCleaningSoSEnvironmentCondition = new CleaningSoSEnvironmentCondition(10);

        ArrayList<CleaningSoSEnvironmentCondition> runtimeEnvironmentalConditions = new ArrayList<>(0);
        runtimeEnvironmentalConditions.add(new CleaningSoSEnvironmentCondition(10));
        runtimeEnvironmentalConditions.add(new CleaningSoSEnvironmentCondition(20));
        runtimeEnvironmentalConditions.add(new CleaningSoSEnvironmentCondition(30));
        runtimeEnvironmentalConditions.add(new CleaningSoSEnvironmentCondition(40));

        Executor executor = new Executor(expectedCleaningSoSEnvironmentConditions, cleaningSoSConfigurations, initialCleaningSoSConfiguration, initialCleaningSoSEnvironmentCondition, runtimeEnvironmentalConditions);
        executor.run(100,1000);
    }
}
