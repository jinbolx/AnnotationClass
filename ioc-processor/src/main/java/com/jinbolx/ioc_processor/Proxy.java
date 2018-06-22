package com.jinbolx.ioc_processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class Proxy {

    private String packageName;
    private TypeElement typeElement;
    private static final String SUFFIX = "ViewInjector";
    private static final String SYMBOL = "$$";
    public Map<VariableElement, Integer> idMap = new HashMap<>();
    public List<Integer> list=new ArrayList<>();

    Proxy(Elements elements, TypeElement typeElement) {
        this.typeElement = typeElement;
        packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public List<Integer> getList() {
        return list;
    }

    public String generateClass(StringBuilder builder) {
        builder.append("//Generate Code by BindView. Do not modify it!")
                .append("\n package ")
                .append(packageName)
                .append(";\n\n")
                .append("import com.jinbolx.ioc.*;\n\n")
                .append("public class ")
                .append(getClassName())
                .append(" implements\n")
                .append("       ViewInjector<")
                .append(getTypeFullName())
                .append("> {");
        generateUsefulMethod(builder);
        builder.append("\n\n}");
        return builder.toString();
    }

    private void generateUsefulMethod(StringBuilder builder) {
        generateBindViewMethod(builder);
        generateOnclickMethod(builder);
    }

    private void generateBindViewMethod(StringBuilder builder) {
        builder.append("\n\n    @Override")
                .append("\n     public void inject(")
                .append(getTypeFullName())
                .append(" host,Object object) {");
        for (VariableElement variableElement :
                idMap.keySet()) {
            int id = idMap.get(variableElement);
            builder.append("\n       if(object instanceof android.app.Activity) {\n")
                    .append("           host.")
                    .append(variableElement.getSimpleName().toString())
                    .append(" = (")
                    .append(variableElement.asType().toString())
                    .append(")((android.app.Activity) object)\n")
                    .append("                   .findViewById(")
                    .append(id)
                    .append(");\n")
                    .append("       } else {")
                    .append("           host.")
                    .append(variableElement.getSimpleName().toString())
                    .append(" = (")
                    .append(variableElement.asType().toString())
                    .append(")((android.view.View) object)\n")
                    .append("                   .findViewById(")
                    .append(id)
                    .append(");\n")
                    .append("       }");
        }
        builder.append("    \n}");
    }

    private void generateOnclickMethod(StringBuilder builder) {

    }

    public String getFileName() {
        return getTypeFullName() + SYMBOL + SUFFIX;
    }

    private String getClassName() {
        return getTypeSimpleName() + SYMBOL + SUFFIX;
    }

    private String getTypeSimpleName() {
        return typeElement.getSimpleName().toString();
    }

    public String getTypeFullName() {
        return typeElement.getQualifiedName().toString();
    }

}
