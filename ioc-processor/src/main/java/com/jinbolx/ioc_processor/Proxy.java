package com.jinbolx.ioc_processor;

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
    public Map<Integer, VariableElement> idMap = new HashMap<>();
    public Map<String,List<Integer>> onclickMap=new HashMap<>();
    Proxy(Elements elements, TypeElement typeElement) {
        this.typeElement = typeElement;
        packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();
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
        builder.append("\n\n    @Override")
                .append("\n     public void inject(final ")
                .append(getTypeFullName())
                .append(" host,Object object) {");
        generateBindViewField(builder);
        generateOnclickField(builder);
        builder.append("\n   }");
    }

    private void generateBindViewField(StringBuilder builder) {

        for (Integer id :
                idMap.keySet()) {
            VariableElement variableElement=idMap.get(id);
            builder.append("\n       if(object instanceof android.app.Activity) {\n")
                    .append("           host.")
                    .append(variableElement.getSimpleName().toString())
                    .append(" = (")
                    .append(variableElement.asType().toString())
                    .append(")((android.app.Activity) object)\n")
                    .append("                   .findViewById(")
                    .append(id)
                    .append(");\n")
                    .append("       } else {\n")
                    .append("           host.")
                    .append(variableElement.getSimpleName().toString())
                    .append(" = (")
                    .append(variableElement.asType().toString())
                    .append(")((android.view.View) object)\n")
                    .append("                   .findViewById(")
                    .append(id)
                    .append(");\n")
                    .append("        }");
        }

    }
// host.textView.setOnClickListener(new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            host.onclick(v);
//        }
//    });
    private void generateOnclickField(StringBuilder builder) {
        for (String name :
                onclickMap.keySet()) {
            List<Integer> list=onclickMap.get(name);
            for (int i = 0; i < list.size(); i++) {
                VariableElement variableElement=idMap.get(list.get(i));
                if (variableElement!=null){
                    builder.append("\n      host.")
                            .append(variableElement.getSimpleName().toString())
                            .append(".setOnClickListener(new android.view.View.OnClickListener() {\n")
                            .append("        @Override")
                            .append("\n           public void onClick(android.view.View v) {")
                            .append("\n               host.")
                            .append(name)
                            .append("(v);\n")
                            .append("           }\n")
                            .append("       });");

                }else {
                    try {
                        throw new Exception("view not bind");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }

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
