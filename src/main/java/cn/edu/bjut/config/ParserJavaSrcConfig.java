package cn.edu.bjut.config;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class ParserJavaSrcConfig {
    private String projectSrcPath;
    List<ParseResult<CompilationUnit>> compilationUnits;

    public ParserJavaSrcConfig(String projectSrcPath){
        this.projectSrcPath = projectSrcPath;
    }

    public ParserJavaSrcConfig(){
    }

    public void init() throws IOException {
        // 配置 JavaParser 的符号解析器
        // 设置类型解析器
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        combinedTypeSolver.add(new JavaParserTypeSolver(Paths.get(projectSrcPath)));

        // 设置JavaParser解析器
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);

        // 指定Spring Boot项目的源代码目录
        SourceRoot sourceRoot = new SourceRoot(Paths.get(projectSrcPath));
        sourceRoot.getParserConfiguration().setSymbolResolver(symbolSolver);

        // 解析项目中的所有Java文件
        this.compilationUnits = sourceRoot.tryToParse("");
    }

    public String getProjectSrcPath(){
        return this.projectSrcPath;
    }

    public List<ParseResult<CompilationUnit>> getCompilationUnits() throws IOException {
        if(this.compilationUnits == null){
            this.init();
        }
        return this.compilationUnits;
    }



}
