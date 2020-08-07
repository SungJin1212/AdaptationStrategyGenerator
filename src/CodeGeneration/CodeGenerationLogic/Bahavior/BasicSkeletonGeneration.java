package CodeGeneration.CodeGenerationLogic.Bahavior;

import CodeGeneration.XMLParseDataType.State;
import CodeGeneration.XMLParseDataType.Transition;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BasicSkeletonGeneration {

    /**
     *
     * @return MethodSpec which contains the Setter of the state machine code
     */
    public static MethodSpec getSetter() {
        CodeBlock setterCode = CodeBlock.builder().addStatement("status = aStatus").build();
        return MethodSpec.methodBuilder("setStatus").addParameter(TypeVariableName.get("Status"), "aStatus").addCode(setterCode).addModifiers(Modifier.PRIVATE).build();
    }

    /**
     *
     * @param initialStateName initial state name
     * @param parameters parsed parameter data
     * @param states
     * @return MethodSpec which contains the Constructor of the state machine code
     */
    public static MethodSpec getConstructor(String initialStateName, ArrayList<String> parameters, ArrayList<State> states) {
        String timeName = "";
        MethodSpec.Builder builder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        CodeBlock constructorCode = CodeBlock.builder().addStatement("setStatus(Status." + initialStateName + ")").build();
        builder.addCode(constructorCode);

        for(String parameter : parameters) {
            builder.addParameter(TypeName.INT, parameter);
            builder.addStatement("this." + parameter + " = " + parameter);
        }
        for(State s : states) {
            if(!s.getTime().equals("1")) {
                timeName = s.getStateName() + "Time";
                builder.addStatement("this." + timeName + " = " + s.getTime());
            }
        }

        return builder.build();
    }

    /**
     *
     * @return MethodSpec which contains the Getter of the state machine code
     */
    public static MethodSpec getGetter() {
        CodeBlock getterCode = CodeBlock.builder().addStatement("return status").build();
        return MethodSpec.methodBuilder("getStatus").addModifiers(Modifier.PUBLIC).returns(TypeVariableName.get("Status")).addCode(getterCode).build();
    }

    public static TypeSpec enumGeneration(ArrayList<State> states) {
        TypeSpec.Builder builder = TypeSpec.enumBuilder("Status").addModifiers(Modifier.PUBLIC);
        for (State s : states) {
            builder.addEnumConstant(s.getStateName());
        }
        return builder.build();
    }

    /**
     *
     * @param states parsed state data
     * @return initial state name
     */
    public static String getInitialStateName(ArrayList<State> states) {
        String Name = "";
        for (State s : states) {
            if(s.getInitialState().equals("1")){
                Name = s.getStateName();
            }
        }
        return Name;
    }

    /**
     *
     * @param transitions: parsed transition data
     * @param parameters: parsed parameter data
     * @return list of fieldSpecs which contain guards, parameters
     */
    public static ArrayList<FieldSpec> getFieldSpec(ArrayList<Transition> transitions, ArrayList<String> parameters) {
        ArrayList<FieldSpec> fields = new ArrayList<>();
        Set <String> guardNames = new HashSet<>(); // to remove duplicate guards ex) guard1 == true, guard1 == false, our goal is to extract the "guard1"
        FieldSpec fieldSpec = FieldSpec.builder(TypeVariableName.get("Status"), "status").addModifiers(Modifier.PRIVATE).build();


        fields.add(fieldSpec);

        for(Transition t : transitions) {
            if(!t.getGuard().equals("")) { // if there is a guard, add guard variables
                guardNames.add(t.getGuard().split(" ")[0]);
            }
        }
        fields.addAll(getGuardField(guardNames));

        /* add local variable */
        for(String s : parameters) {
            fields.add(FieldSpec.builder(TypeName.INT, s).addModifiers(Modifier.PRIVATE).build());
        }

        return fields;
    }

    /**
     *
     * @param guards: list of names of guards
     * @return list of fieldSpecs which contain field information of each guards
     */
    private static ArrayList<FieldSpec> getGuardField(Set<String> guards) {

        ArrayList<FieldSpec> Guards = new ArrayList<>();

        for(String g : guards) {
            Guards.add(FieldSpec.builder(TypeName.BOOLEAN, g).addModifiers(Modifier.PRIVATE).build());
        }

        return Guards;
    }

}
