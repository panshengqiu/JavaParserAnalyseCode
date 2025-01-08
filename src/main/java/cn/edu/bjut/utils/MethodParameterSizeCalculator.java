package cn.edu.bjut.utils;

import cn.edu.bjut.config.ParserJavaSrcConfig;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;

import java.io.IOException;
import java.util.List;

public class MethodParameterSizeCalculator {

    public static void main(String[] args) throws IOException {
        // 项目源码路径
        String projectSrcPath1 = "D:\\HuaweiMoveData\\Users\\24312\\Desktop\\panshengqiu_document\\course_materials\\大四上\\毕业设计\\辅修\\源代码\\daytrader-ee7\\src\\main\\java";
        String projectSrcPath2 = "D:\\HuaweiMoveData\\Users\\24312\\Desktop\\panshengqiu_document\\course_materials\\大四上\\毕业设计\\辅修\\源代码\\jPetStore\\src\\main\\java";

        ParserJavaSrcConfig parserJavaSrcConfig1 = new ParserJavaSrcConfig(projectSrcPath1);
        List<ParseResult<CompilationUnit>> compilationUnits1 = parserJavaSrcConfig1.getCompilationUnits();

        ParserJavaSrcConfig parserJavaSrcConfig2 = new ParserJavaSrcConfig(projectSrcPath2);
        List<ParseResult<CompilationUnit>> compilationUnits2 = parserJavaSrcConfig2.getCompilationUnits();

        extractClassMethodParameterSize(compilationUnits1);
        System.out.println("--------------------------------------------------------------");
        extractClassMethodParameterSize(compilationUnits2);
    }

    // 提取并计算每个类方法的参数大小
    public static void extractClassMethodParameterSize(List<ParseResult<CompilationUnit>> compilationUnits){
        for (ParseResult<CompilationUnit> result : compilationUnits) {
            if (result.isSuccessful()) {
                CompilationUnit cu = result.getResult().get();
                // 获取所有类
                List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
                for (ClassOrInterfaceDeclaration classDecl : classes) {
                    System.out.println("Class: " + classDecl.getNameAsString());
                    calculateMethodParameterSize(classDecl);
                }
            } else {
                System.out.println("Failed to parse file.");
            }
        }
    }

    // 计算类的每个方法的参数字节规模
    public static void calculateMethodParameterSize(ClassOrInterfaceDeclaration classDecl) {
        // 获取类中的所有方法
        List<MethodDeclaration> methods = classDecl.getMethods();
        for (MethodDeclaration method : methods) {
            String methodName = method.getNameAsString();
            System.out.println("  Method: " + methodName);

            // 计算方法的参数大小
            long totalSize = 0;
            List<Parameter> parameters = method.getParameters();
            for (Parameter param : parameters) {
                Type paramType = param.getType();
                long paramSize = getSizeOfType(paramType);
                totalSize += paramSize;
                System.out.println("    Parameter: " + param.getNameAsString() + " of type " + paramType + " takes " + paramSize + " bytes");
            }
            System.out.println("    Total size of parameters: " + totalSize + " bytes");
        }
    }

    // 根据参数类型返回大致的字节大小
    private static long getSizeOfType(Type type) {
        switch (type.asString()) {
            case "int":
                return 4;
            case "long":
                return 8;
            case "double":
                return 8;
            case "float":
                return 4;
            case "char":
                return 2;
            case "boolean":
                return 1;
            case "byte":
                return 1;
            case "short":
                return 2;
            case "String":
                return 64; // 假设平均字符串长度为64字节
            case "Object":
                return 128; // 假设对象占用128字节
            default:
                return 128; // 未知类型
        }
    }
}
