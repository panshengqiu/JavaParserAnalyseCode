package cn.edu.bjut.utils;


import cn.edu.bjut.config.ParserJavaSrcConfig;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodCallExtractor {

    public static void main(String[] args) throws IOException {
        // 项目源码路径
        String projectSrcPath1 = "D:\\HuaweiMoveData\\Users\\24312\\Desktop\\panshengqiu_document\\course_materials\\大四上\\毕业设计\\辅修\\源代码\\daytrader-ee7\\src\\main\\java";

        String projectSrcPath2 = "D:\\HuaweiMoveData\\Users\\24312\\Desktop\\panshengqiu_document\\course_materials\\大四上\\毕业设计\\辅修\\源代码\\jPetStore\\src\\main\\java";

        ParserJavaSrcConfig parserJavaSrcConfig1 = new ParserJavaSrcConfig(projectSrcPath1);
        List<ParseResult<CompilationUnit>> compilationUnits1 = parserJavaSrcConfig1.getCompilationUnits();

        ParserJavaSrcConfig parserJavaSrcConfig2 = new ParserJavaSrcConfig(projectSrcPath2);
        List<ParseResult<CompilationUnit>> compilationUnits2 = parserJavaSrcConfig2.getCompilationUnits();

        extractMethodCalls(compilationUnits1);
        System.out.println("--------------------------------------------------------------");
        extractMethodCalls(compilationUnits2);
    }

    // 提取类方法间的调用关系
    public static void extractMethodCalls(List<ParseResult<CompilationUnit>> compilationUnits) {
        for (ParseResult<CompilationUnit> result : compilationUnits) {
            if (result.isSuccessful()) {
                CompilationUnit cu = result.getResult().get();
                extractMethodCallsFromCompilationUnit(cu);
            } else {
                System.out.println("Failed to parse file");
            }
        }
    }

    private static void extractMethodCallsFromCompilationUnit(CompilationUnit cu) {
        // 存储每个类的方法调用关系
        Map<String, Map<String, Integer>> classMethodCalls = new HashMap<>();

        // 获取所有类声明
        List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration clazz : classes) {
            // 获取类名
            String className = clazz.getNameAsString();
            System.out.println("Class: " + className);

            // 获取类中的所有方法
            List<MethodDeclaration> methods = clazz.getMethods();
            for (MethodDeclaration method : methods) {
                String methodName = method.getNameAsString();
                System.out.println("  Method: " + methodName);

                // 存储当前方法调用的所有方法
                Map<String, Integer> methodCalls = new HashMap<>();

                // 查找方法体中的所有方法调用
                method.findAll(MethodCallExpr.class).forEach(call -> {
                    String calledMethod = call.getNameAsString();
                    methodCalls.put(calledMethod, methodCalls.getOrDefault(calledMethod, 0) + 1);
                });

                // 如果当前方法调用了其他方法，打印调用关系
                if (!methodCalls.isEmpty()) {
                    System.out.println("    Calls:");
                    methodCalls.forEach((calledMethod, count) -> {
                        System.out.println("      " + calledMethod + " (called " + count + " times)");
                    });
                }
            }
        }


    }

}

