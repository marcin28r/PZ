package com.library.backend.reflection;

import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@NoArgsConstructor
public class JsonRequestGenerator {


    public void generate(Class<?> clas) {
        if (clas.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class)) {

            StringBuilder mapping = new StringBuilder();
            if(clas.isAnnotationPresent(org.springframework.web.bind.annotation.RequestMapping.class)){
                for(String s: clas.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class).value()){
                    mapping.append(s);
                }
            }

            File folder = new File("JsonEndpoints");
            if (!folder.exists()) {
                folder.mkdir();
            }

            File file = new File(folder, clas.getSimpleName() + ".txt");


            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(clas.getSimpleName() + "\n");
                for(Method m: clas.getDeclaredMethods()){
                    writer.write(getMappingMethod(m) +" : " + mapping + getMappingExtendPath(m) + "\n");
                    writer.write("{\n");
                    if(m.getParameters().length == 0){
                        writer.write("none");
                    }else {
                        for(Parameter p: m.getParameters()) {
                            writer.write(this.extractParametersToJson(p));
                        }
                    }
                    writer.write("\n}\n");
                }

                System.out.println("Plik " + file.getAbsolutePath() + " został utworzony.");
            } catch (IOException e) {
                System.err.println("Błąd podczas zapisu do pliku: " + e.getMessage());
            }
        }else {
            System.out.println(clas.getName() + " nie jest kontrolerem!");
        }
    }

    private String extractParametersToJson(Parameter p) {
        String res = "";
        if(p.isAnnotationPresent(org.springframework.web.bind.annotation.RequestHeader.class)){
            res += this.JsonRequestHeader(p);
        }
        else if(p.isAnnotationPresent(org.springframework.web.bind.annotation.RequestBody.class)){
            res += "";
        }
        else {
            res = "{\n";
        }
        return res + "\n";
    }

    private String JsonRequestHeader(Parameter p) {
        return "\"" + p.getDeclaredAnnotation(RequestHeader.class).value() + "\" : "
//               this.JsonValueInType(p.getDeclaredAnnotation(RequestHeader.class).annotationType(),
                + this.JsonNameInType(p.getName(), p.getType());
    }

    private String JsonNameInType(String name, Class<?> type) {
        switch (type.getSimpleName()){
            case "String":
                return "\""+ name +"\"";
            default:
                return "default";
        }
    }


    public Optional<Annotation> extractMapping(Method method){
            List<Class<? extends Annotation>> expectedAnnotations = Arrays.asList(
                    PostMapping.class,
                    DeleteMapping.class,
                    PutMapping.class,
                    GetMapping.class);

            Optional<Annotation> found = Arrays.stream(method.getDeclaredAnnotations())
                    .filter(annotation -> expectedAnnotations.contains(annotation.annotationType()))
                    .findFirst();
            return found;
    }

    public String getMappingMethod(Method method){
        Optional<Annotation> found = extractMapping(method);
        return found.map(annotation ->
                annotation.annotationType().getSimpleName().replaceAll("Mapping$", "").toUpperCase(Locale.ROOT)
        ).orElse("Nieprzewidziany Mapping");
    }

    public String getMappingExtendPath(Method method) {
        Optional<Annotation> found = extractMapping(method);
        return found.map(annotation -> {
            try {
                String[] paths = (String[]) annotation.annotationType().getDeclaredMethod("path").invoke(annotation);

                if (paths.length == 0) {
                    paths = (String[]) annotation.annotationType().getDeclaredMethod("value").invoke(annotation);
                }

                return String.join("/", paths);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }).orElse("Brak");
    }

}
