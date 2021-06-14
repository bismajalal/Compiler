package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import parser.LexicalAnalyzer;
import java.util.*;


public class Parser {
    String look;
    boolean error=false;
    BufferedReader br;
    PrintWriter writer1;
    PrintWriter writer2;
    PrintWriter writer3;
    int count =0; //to keep track of number of times newTmp is invoked
    String identifierName;
    String dataType;
    static int tab = 0;
    String lexeme=null;
    int n=0;
    int address;
    int vTrue;
    int whileTrue;
    
    void backPatch(int lineNo, int address) throws FileNotFoundException, IOException
    {
        String currDir = System.getProperty("user.dir");
        String p = currDir + "\\tac.txt";
        Path path = Paths.get(p);
        
        List <String> lines;
        lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        System.out.println(lines);
        
        String content = lines.get(lineNo);
        content += address+1 ;
        lines.set(lineNo, content);
        
        PrintWriter writer3 = new PrintWriter(p);
        for(String str: lines)
            writer3.write(str + System.lineSeparator());
        writer3.close();
    }
    
    void emit(String args)
    {
        writer3.append(args);
        writer3.append("\n");
        writer3.flush();
        n++;
    }
    
    String newTemp()
    {
        count++;
        return("temp"+count);
    }
    
    String ExpressionS()
    {
        ++tab;
        for (int i = 0; i < tab; i++) {
            System.out.print("----");
            writer2.append("----");
            writer2.flush();
        }
        System.out.print("Expression\n");
        writer2.append("Expression\n");
        writer2.flush();
       
        --tab;
        String v=Expression();
        return v;
    }
    
    String Expression() {
        String Ev="";
        if(look.equals("ID")||look.equals("NUM")||look.equals("'('"))
        {
            ++tab;
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Term\n");
            writer2.append("Term\n");
            writer2.flush();
            String Tn=Term();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("EPrime\n");
            writer2.append("EPrime\n");
            writer2.flush();
            Ev=EPrime(Tn);
            --tab;
            
        }
        else
        {
            error=true;
            System.out.print("Syntax Error in what seemed like an assignment expression.");
            System.exit(1);
        }
       
        return Ev;
    }

    String Term() {
	String retValue;
        ++tab;
        for (int i = 0; i < tab; i++) {
            System.out.print("----");
            writer2.append("----");
            writer2.flush();
        }
        System.out.print("F\n");
        writer2.append("F\n");
        writer2.flush();
        String Fn=F();
        
        for (int i = 0; i < tab; i++) {
            System.out.print("----");
            writer2.append("----");
            writer2.flush();
        }
        System.out.print("TPrime\n");
        writer2.append("TPrime\n");
        writer2.flush();
        retValue=TPrime(Fn);  
        --tab;
        return retValue;
    }  

    String EPrime(String str) {
        String retValue;
	if (look.equals("'+'")) {
            ++tab;
            match("'+'"); 
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("+\n");
            writer2.append("+\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Term\n");
            writer2.append("Term\n");
            writer2.flush();
            String Tn = Term();
            
            String EprimeI;
            String tmp=newTemp();
            emit(tmp+"="+str+"+"+Tn);
            EprimeI = tmp;
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("EPrime\n");
            writer2.append("EPrime\n");
            writer2.flush();
            retValue=EPrime(EprimeI);
            --tab;
            
	}
	else if (look.equals("'-'")) {
            ++tab;
            match("'-'"); 
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("-\n");
            writer2.append("-\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Term\n");
            writer2.append("Term\n");
            writer2.flush();
            
            String Tn=Term();
            String EprimeI;
            String tmp=newTemp();
            emit(tmp+"="+str+"-"+Tn);
            EprimeI = tmp;
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("EPrime\n");
            writer2.append("EPrime\n");
            writer2.flush();
            retValue=EPrime(EprimeI);
            --tab;
            
	}
	else
            return str;
        return retValue;
    }

    String F() {
        ++tab;
        String retValue="";
	if (look.equals("ID"))
        {
                for (int i = 0; i < tab; i++) {
                    System.out.print("----");
                    writer2.append("----");
                    writer2.flush();
                }
                System.out.print(lexeme+"\n");
                writer2.append(lexeme+"\n");
                writer2.flush();
                StringBuilder lex=new StringBuilder(lexeme);
                if(lex.charAt(0)=='"')
                {
                    lex.deleteCharAt(0);
                    lex.deleteCharAt(lex.length()-1);
                }
                retValue=lex.toString();
                match("ID");
                --tab;
        }
	else if (look.equals("NUM"))
        {
                for (int i = 0; i < tab; i++) {
                    System.out.print("----");
                    writer2.append("----");
                    writer2.flush();
                }
                System.out.print(lexeme+"\n");
                writer2.append(lexeme+"\n");
                writer2.flush();
                StringBuilder lex=new StringBuilder(lexeme);
                if(lex.charAt(0)=='"')
                {
                    lex.deleteCharAt(0);
                    lex.deleteCharAt(lex.length()-1);
                }
                retValue=lex.toString();
                match("NUM");
                --tab;
                
        }
	else if (look.equals("'('")) {
		match("'('");
                for (int i = 0; i < tab; i++) {
                    System.out.print("----");
                    writer2.append("----");
                    writer2.flush();
                }
                System.out.print("(\n");
                writer2.append("(\n");
                writer2.flush();
            
                for (int i = 0; i < tab; i++) {
                    System.out.print("----");
                    writer2.append("----");
                    writer2.flush();
                }
                System.out.print("Expression\n");
                writer2.append("Expression\n");
                writer2.flush();
                retValue=ExpressionS();
               
                match("')'");
                for (int i = 0; i < tab; i++) {
                    System.out.print("----");
                    writer2.append("----");
                    writer2.flush();
                }
                System.out.print(")\n");
                writer2.append(")\n");
                writer2.flush();
                --tab;
                
	}
	else
        {
            error=true;
            System.out.print("Bad token in what seemed like an arithemtic expression...");
            System.exit(1);
            --tab;
            
        }
        return retValue;
    }

    String TPrime(String str){
        String retValue="";
	if (look.equals("'*'")) {
            ++tab;
            match("'*'"); 
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("*\n");
            writer2.append("*\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("F\n");
            writer2.append("F\n");
            writer2.flush();
            
            String Fn=F();
            String TprimeI;
            String tmp=newTemp();
            emit(tmp+"="+str+"*"+Fn);
            TprimeI = tmp;
          
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("TPrime\n");
            writer2.append("TPrime\n");
            writer2.flush();
            retValue=TPrime(TprimeI);
            --tab;
            
	}
	else if (look.equals("'/'")) {
            ++tab;
            match("'/'"); 
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("/\n");
            writer2.append("/\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("F\n");
            writer2.append("F\n");
            writer2.flush();
            String Fn=F();
            String TprimeI;
            String tmp=newTemp();
            emit(tmp+"="+str+"/"+Fn);
            TprimeI = tmp;
          
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("TPrime\n");
            writer2.append("TPrime\n");
            writer2.flush();
            retValue=TPrime(TprimeI);
            --tab;
	}
        else
            return str;
        return retValue;
    }
    
    
    void DeclarationS()
    {
        ++tab;
        for (int i = 0; i < tab; i++) {
            System.out.print("----");
            writer2.append("----");
            writer2.flush();
        }
        System.out.print("Declaration\n");
        writer2.append("Declaration\n");
        writer2.flush();
        Declaration();
        --tab;
    }
    
    
    void Declaration()
    {
        int inc=0;
        if(look.equals("char"))
        {
            ++tab;
            inc=1;
            match("char");dataType="char";
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("char\n");
            writer2.append("char\n");
            writer2.flush();
            
            match("':'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(":\n");
            writer2.append(":\n");
            writer2.flush();
            
            if(look.equals("ID"))
            {
                   StringBuilder id=new StringBuilder(identifierName);
                   id.deleteCharAt(0);id.deleteCharAt(id.length()-1);
                   identifierName=id.toString().trim();
                   writer1.append(identifierName+"\t"+dataType+"\t"+address+"\n");
                   writer1.flush();
                   identifierName=null;
                   address+=inc;
            }
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            match("ID");
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("G\n");
            writer2.append("G\n");
            writer2.flush();
            G(inc);
            --tab;
        }
        else if(look.equals("Integer"))
        {
            ++tab;
            inc=4;
            match("Integer");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Integer\n");
            writer2.append("Integer\n");
            writer2.flush();
            dataType="Integer";
            
            match("':'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(":\n");
            writer2.append(":\n");
            writer2.flush();
            
            if(look.equals("ID"))
            {
                   StringBuilder id=new StringBuilder(identifierName);
                   id.deleteCharAt(0);id.deleteCharAt(id.length()-1);
                   identifierName=id.toString().trim();
                   writer1.append(identifierName+"\t"+dataType+"\t"+address+"\n");
                   writer1.flush();
                   identifierName=null;
                   address+=inc;
            }
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            match("ID");
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("G\n");
            writer2.append("G\n");
            writer2.flush();
            G(inc);
            --tab;
        }
        
    }
    
    void G(int inc)
    {
        ++tab;
        if(look.equals("','"))
        {
            match("','");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(",\n");
            writer2.append(",\n");
            writer2.flush();
            
            if(look.equals("ID"))
            {
                   StringBuilder id=new StringBuilder(identifierName);
                   id.deleteCharAt(0);id.deleteCharAt(id.length()-1);
                   identifierName=id.toString();
                   writer1.append(identifierName+"\t"+dataType+"\t"+address+"\n");
                   writer1.flush();
                   identifierName=null;
                   address+=inc;
                   //dataType=null;
            }
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            match("ID");
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("G\n");
            writer2.append("G\n");
            writer2.flush();
            G(inc);
        }
        else if(look.equals("';'"))
        {    
            match("';'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(";\n");
            writer2.append(";\n");
            writer2.flush();
            dataType=null;
        }
        else 
        {
            error=true;
            System.out.print("Bad token in what seemed like a declaration statement...");
            System.exit(1);
        }
       --tab;
    }
    
    void AssignmentS()
    {
        ++tab;
        for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
        }
        System.out.print("Assignment\n");
        writer2.append("Assignment\n");
        writer2.flush();
        Assignment();
        --tab;
    }
    
    //
    void Assignment()
    {
        ++tab;
        StringBuilder lex;
        if(look.equals("ID"))
        {
           lex=new StringBuilder(lexeme);
                if(lex.charAt(0)=='"')
                {
                    lex.deleteCharAt(0);
                    lex.deleteCharAt(lex.length()-1);
                }
                
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            match("ID");
            
            match("':='");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(":=\n");
            writer2.append(":=\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("B\n");
            writer2.append("B\n");
            writer2.flush();
            emit(lex.toString()+"="+B());
            
            match("';'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(";\n");
            writer2.append(";\n");
            writer2.flush();
        }
        --tab;
    }
    
    
    String B()
    {
        ++tab;
        String retValue;
        if(look.equals("LC"))
        {
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            StringBuilder lex=new StringBuilder(lexeme);
//            if(lex.charAt(0)=='\'')
//            {
//                lex.deleteCharAt(0);
//                lex.deleteCharAt(lex.length()-1);
//            }
            retValue=lex.toString().trim();
            match("LC");
        }
        else if(look.equals("NUM"))
        {
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            retValue=lexeme;
            match("NUM");
        }
        else if(look.equals("ID")||look.equals("'('"))
        {
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Expression\n");
            writer2.append("Expression\n");
            writer2.flush();  
            retValue=ExpressionS();
        }
        else 
        {    
            error=true;
            System.out.print("Bad token in what seemed like assignment...");
            System.exit(1);
            retValue="error";
        }
        --tab;
        return retValue;
    }
    
    
    void ConditionalS() throws IOException
    {
        ++tab;
        for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
        }
        System.out.print("Conditional\n");
        writer2.append("Conditional\n");
        writer2.flush();
        Conditional();
        --tab;
    }
    
    
    void Conditional() throws IOException
    {
        ++tab;
        if(look.equals("if"))
        {
            match("if");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("if\n");
            writer2.append("if\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("V\n");
            writer2.append("V\n");
            writer2.flush();
         
            int ifFalse = V("if ");
            backPatch(vTrue, n);
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(":\n");
            writer2.append(":\n");
            writer2.flush();
            match("':'");
            
            match("'{'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("{\n");
            writer2.append("{\n");
            writer2.flush();
           
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Statement\n");
            writer2.append("Statement\n");
            writer2.flush();
            
            Statement();
            int ifNext = n;
            emit("goto ");
            backPatch(ifFalse, n);
            
            match("'}'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("}\n");
            writer2.append("}\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("CPrime\n");
            writer2.append("CPrime\n");
            writer2.flush();
            Cprime(ifNext, -1);
        }
        --tab;
    }
    
    int V(String keyword)
    {
        ++tab;
        for (int i = 0; i < tab; i++) {
            System.out.print("----");
                writer2.append("----");
                writer2.flush();
        }
        System.out.print("O\n");
        writer2.append("O\n");
        writer2.flush();
        
        vTrue = n;
        
        String output = keyword;
        output += O();
        output += " ";
        
        for (int i = 0; i < tab; i++) {
            //System.out.print("----");
                writer2.append("----");
                writer2.flush();
        }
        //System.out.print(lexeme+"\n");
        writer2.append(lexeme+"\n");
        writer2.flush();
        output += lexeme;
        output += " ";
        match("RO");
        
            
        for (int i = 0; i < tab; i++) {
            //System.out.print("----");
                writer2.append("----");
                writer2.flush();
        }
        //System.out.print("O\n");
        writer2.append("O\n");
        writer2.flush();
        
        output += O();
        output += " goto " ;
        
        
        emit (output);
        int retVal = n;
        emit ("goto ");
        
        --tab;
        return retVal;
    }
    
    
    String O()
    {
        ++tab;
        if(look.equals("ID"))
        {
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            //System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            String retVal = lexeme;
            writer2.flush();
            match("ID");
            
            return retVal;
        }
        else if(look.equals("LC"))
        {
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            //System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            String retVal = lexeme;
            writer2.flush();
            match("LC");
            
            System.out.println("O string LC lex:" + retVal);
            return retVal;
        }
        else if (look.equals("NUM"))
        {
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            //System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            String retVal = lexeme;
            writer2.flush();
            match("NUM");
            
            System.out.println("O string NUM lex:" + retVal);
            return retVal;
        }
        else
        {
            error=true;
            System.out.print("Bad token... at "+look+" in what seemed like a conditional statement");
            System.exit(1);
        }
        --tab;
        return null;
    }
    
    void Cprime(int ifNext, int elifNext) throws IOException
    {
        ++tab;
        if(look.equals("elif"))
        {
            match("elif");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("elif\n");
            writer2.append("elif\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("V\n");
            writer2.append("V\n");
            writer2.flush();
            
            
            int elifFalse = V("elif");
            backPatch(vTrue, n);
            
            match("':'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(":\n");
            writer2.append(":\n");
            writer2.flush();
            
            match("'{'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("{\n");
            writer2.append("{\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Statement\n");
            writer2.append("Statement\n");
            writer2.flush();
            
            Statement();
            elifNext = n;
            emit("goto ");
            backPatch(elifFalse, n);
            
            match("'}'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("}\n");
            writer2.append("}\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("CPrime\n");
            writer2.append("CPrime\n");
            writer2.flush();
            Cprime(ifNext, elifNext);
        }
        else if(look.equals("else"))
        {
            match("else");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("else\n");
            writer2.append("ese\n");
            writer2.flush();
            
            match("'{'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("{\n");
            writer2.append("{\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Statement\n");
            writer2.append("Statement\n");
            writer2.flush();
            
            Statement();
            backPatch(ifNext, n);
            if (elifNext > -1)
                backPatch(elifNext, n);
            
            match("'}'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("}\n");
            writer2.append("}\n");
            writer2.flush();
        }
        --tab;
    }
    
    void FunctionS() throws IOException
    {
        ++tab;
        for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
        }
        System.out.print("Function\n");
        writer2.append("Function\n");
        writer2.flush();
        Function();
        --tab;
    }
    
    void Function() throws IOException
    {
        ++tab;
        if(look.equals("func"))
        {
            match("func");
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Z\n");
            writer2.append("Z\n");
            writer2.flush();
            Z();
            
            match("':'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(":\n");
            writer2.append(":\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            match("ID");
            
            match("'('");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("(\n");
            writer2.append("(\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("X\n");
            writer2.append("X\n");
            writer2.flush();
            X();
            
            match("')'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(")\n");
            writer2.append(")\n");
            writer2.flush();
            
            match("'{'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("{\n");
            writer2.append("{\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Statement\n");
            writer2.append("Statement\n");
            writer2.flush();
            Statement();
            
            if(look.equals("ret"))
            {
                match("ret");
                for (int i = 0; i < tab; i++) {
                    System.out.print("----");
                writer2.append("----");
                writer2.flush();
                }
                System.out.print("ret\n");
                writer2.append("ret\n");
                writer2.flush();
            
                for (int i = 0; i < tab; i++) {
                    System.out.print("----");
                writer2.append("----");
                writer2.flush();
                }
                System.out.print(lexeme+"\n");
                writer2.append(lexeme+"\n");
                writer2.flush();
                match("ID");
                
                match("';'");
                for (int i = 0; i < tab; i++) {
                    System.out.print("----");
                writer2.append("----");
                writer2.flush();
                }
                System.out.print(";\n");
                writer2.append(";\n");
                writer2.flush();
            }
            
            match("'}'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("}\n");
            writer2.append("}\n");
            writer2.flush();
        }
        --tab;
    }
   
    //
    void Z()
    {
        ++tab;
        if(look.equals("Integer"))
        {
            match("Integer");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Integer\n");
            writer2.append("Integer\n");
            writer2.flush();
        }
        else if(look.equals("char"))
        {
            match("char");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("char\n");
            writer2.append("char\n");
            writer2.flush();
        }
        else
        {
            error=true;
            System.out.print("Bad token at "+look+" ...in what seemed like a function header");
            System.exit(1);
        }
        --tab;
    }
    
    
    void X() //Parameter List
    {
        ++tab;
        if(!look.equals("')'"))
        {
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Z\n");
            writer2.append("Z\n");
            writer2.flush();
            Z();
            
            match("':'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(":\n");
            writer2.append(":\n");
            writer2.flush();
                
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            match("ID");
            
            if(look.equals("','"))
            {    
                match("','");
                for (int i = 0; i < tab; i++) {
                    System.out.print("----");
                writer2.append("----");
                writer2.flush();
                }
                System.out.print(",\n");
                writer2.append(",\n");
                writer2.flush();
                
                for (int i = 0; i < tab; i++) {
                    System.out.print("----");
                writer2.append("----");
                writer2.flush();
                }
                System.out.print("X\n");
                writer2.append("X\n");
                writer2.flush();
                X();
            }
        }
        --tab;
    }
    
    //done
    void OutputS()
    {
        ++tab;
        for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
        }
        System.out.print("Output\n");
        writer2.append("Output\n");
        writer2.flush();
        Output();
        --tab;
    }
    
    //done
    void Output()
    {
        ++tab;
        if(look.equals("print"))
        {
            match("print");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("print\n");
            writer2.append("print\n");
            writer2.flush();
                    
            match("'('");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("(\n");
            writer2.append("(\n");
            writer2.flush();
            
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            
            emit ("print(" + lexeme + ")");        
            match("STR");
            
            match("')'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(")\n");
            writer2.append(")\n");
            writer2.flush();
            
            match("';'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(";\n");
            writer2.append(";\n");
            writer2.flush();
        }
        else if(look.equals("println"))
        {
            match("println");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("println\n");
            writer2.append("println\n");
            writer2.flush();
            
            match("'('");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("(\n");
            writer2.append("(\n");
            writer2.flush();
            
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            emit ("println(" + lexeme + ")");
            match("ID");
            
            match("')'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(")\n");
            writer2.append(")\n");
            writer2.flush();
            
            match("';'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(";\n");
            writer2.append(";\n");
            writer2.flush();
        }
        else if(look.equals("write"))
        {
            match("write");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("write\n");
            writer2.append("write\n");
            writer2.flush();
            
            match("'('");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("(\n");
            writer2.append("(\n");
            writer2.flush();
            
            
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            emit ("print(" + lexeme + ")");
            match("ID");
            
            match("')'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(")\n");
            writer2.append(")\n");
            writer2.flush();
            
            match("';'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(";\n");
            writer2.append(";\n");
            writer2.flush();
        }
        --tab;
    }
    
    void LoopS() throws IOException
    {
        ++tab;
        for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
        }
        System.out.print("loop\n");
        writer2.append("loop\n");
        writer2.flush();
        Loop();
        --tab;
    }
    
    
    void Loop() throws IOException
    {
        ++tab;
        if(look.equals("while"))
        {
            match("while");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("while\n");
            writer2.append("while\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("V\n");
            writer2.append("V\n");
            writer2.flush();
            
            int whileStart = n+1;
            int whileFalse = V("while");
            backPatch(vTrue, n);
            
            match("':'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(":\n");
            writer2.append(":\n");
            writer2.flush();
            
            match("'{'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("{\n");
            writer2.append("{\n");
            writer2.flush();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Statement\n");
            writer2.append("Statement\n");
            writer2.flush();
            
            Statement();
            emit("goto " + whileStart);
            backPatch(whileFalse,n);
            
            
            match("'}'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("}\n");
            writer2.append("}\n");
            writer2.flush();
        }
        --tab;
    }
    
    //done
    void InputS()
    {
        ++tab;
        for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
        }
        System.out.print("Input\n");
        writer2.append("Input\n");
        writer2.flush();
        Input();
        --tab;
    }
       
    //done
    void Input()
    {
        ++tab;
        if(look.equals("In"))
        {
            match("In");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("In\n");
            writer2.append("In\n");
            writer2.flush();
            
            match(">>");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(">>\n");
            writer2.append(">>\n");
            writer2.flush();
            
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            emit ("In >>" + lexeme);
            match("ID");
            
            match("';'");
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(";\n");
            writer2.append(";\n");
            writer2.flush();
        }
        --tab;
    }
      
    void CommentS()
    {
        ++tab;
        for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
        }
        System.out.print("Comment\n");
        writer2.append("Comment\n");
        writer2.flush();
        Comment();
        --tab;
    }
    
    void Comment()
    {
        ++tab;
        if(look.equals("COM"))
        {
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print(lexeme+"\n");
            writer2.append(lexeme+"\n");
            writer2.flush();
            match("COM");
        }
        --tab;
    }
    
    //
    void Statement() throws IOException
    {
        ++tab;
        if(look!=null && !look.equals("'}'") && !look.equals("ret"))
        {
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Q\n");
            writer2.append("Q\n");
            writer2.flush();
            Q();
            
            for (int i = 0; i < tab; i++) {
                System.out.print("----");
                writer2.append("----");
                writer2.flush();
            }
            System.out.print("Statement\n");
            writer2.append("Statement\n");
            writer2.flush();
            Statement();
        }    
        --tab;
    }
    
    void Q() throws IOException
    {
        if(look==null)
        {
            System.out.print("Bad token!Last statement is incomplete");
            System.exit(1);
        }
        if(look.equals("Integer")||look.equals("char"))
        {
            DeclarationS();
        }
        else if(look.equals("ID"))
        {
            AssignmentS();
        }
        else if(look.equals("func"))
        {
            FunctionS();
        }
        else if(look.equals("if"))
        {
            ConditionalS();
        }
        else if(look.equals("print")||look.equals("println")||look.equals("write")) 
        {
            OutputS();
        }
        else if(look.equals("while"))
        {
            LoopS();
        }
        else if(look.equals("In"))
        {
            InputS();
        }
        else if(look.equals("COM"))
        {
            CommentS();
        }
        else
        {
            error=true;
            System.out.print("Syntax Error!Unrecognized token..."+look);
            System.exit(1);
        }
    }
    
    void match(String tok) {
        if(look==null)
        {
            error=true;
            System.out.print("EOF ecountered when "+tok+" expected");
            System.exit(1);
        }
	if (look.equals(tok))
        {
            look = nextTok();
        }
	else
        {
            error=true;
            System.out.print("EOF ecountered when "+tok+" expected");
            System.exit(1);
        }
    }
    
    void runParser()
    {
        try {
            br = new BufferedReader(new FileReader("words.txt"));
            writer1 = new PrintWriter(new FileWriter("parser-symboltable.txt",true));
            writer2 = new PrintWriter(new FileWriter("parseTree.txt",true));
            writer3 = new PrintWriter(new FileWriter("tac.txt",true));
            
            look=nextTok();
            System.out.print("Statement\n");
            writer2.append("Statement\n");
            writer2.flush();
            Statement();

        } catch (Exception ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    String nextTok()
    {
        try{
            String str=br.readLine();
            boolean multilineComment=false;
            //Check to make sure the line read isn't an error statement 
            
            if(str!=null){
            //For Multiline comments
            lexeme="";
            while((str.charAt(str.length()-1)!=')'))
            {
                if(multilineComment==false){
                    String tokens[]=str.split(" , ");
                    StringBuffer sb2=new StringBuffer(tokens[1]);
                    lexeme=lexeme+sb2.toString().trim()+"\n";}
                else
                    lexeme=lexeme+str+"\n";
                str=br.readLine();
                multilineComment=true;
            }
            if (multilineComment==true)
            {
                StringBuffer sb2=new StringBuffer(str);
                sb2.deleteCharAt(sb2.length()-1);
                lexeme=lexeme+sb2.toString().trim();
                return("COM");
            }
            //Extracting the token from the line read
            String tokens[]=str.split(" , ");
            StringBuilder sb = new StringBuilder(tokens[0]);
            sb.deleteCharAt(0);
            
            if(sb.toString().trim().equals("ID"))
            {
                StringBuffer sb2=new StringBuffer(tokens[1]);
                sb2.deleteCharAt(sb2.length()-1);
                identifierName=sb2.toString().trim();
                lexeme=sb2.toString().trim();
            }
            else if(sb.toString().trim().equals("COM") || sb.toString().trim().equals("LC") || sb.toString().trim().equals("NUM") || sb.toString().trim().equals("LC") || sb.toString().trim().equals("STR") || sb.toString().trim().equals("RO"))
            {
                StringBuffer sb2=new StringBuffer(tokens[1]);
                sb2.deleteCharAt(sb2.length()-1);
                lexeme=sb2.toString().trim();
       
            }
            return sb.toString().trim(); 
            }
//            else 
//            {
//                if(look.equals("';'"))
//                   System.exit(0);
//                else
//                {   
//                    System.out.print("Bad token!Last statement is incomplete");
//                    System.exit(1);
//                }
//                
//            }
        }
        catch(Exception e) 
        {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    public static void main(String[] args) throws IOException {
      
        // interface that obtains absolute path of a file including its name
        Scanner myObj = new Scanner(System.in); 
        System.out.println("Enter the complete file path terminating with the .go file");
        String filePath = myObj.next(); 
         System.out.println("File Path is: " + filePath);
         
        //checks for .go extension
        String extension = filePath.substring(filePath.length() - 3);
        if(!extension.equals(".go"))
        {
            System.out.println("File path must end in source file name which should have the .go extension");
            System.exit(-1);
        }
        
        //Compilation Phase 1# Lexical Analyzer,generates words.txt
        LexicalAnalyzer lex = new LexicalAnalyzer();
        if(lex.runLex(filePath)==false)
        {
            System.out.println("Lexical Analyzer detected an error so further compilation halted");
            System.exit(1);
        }
        
        //Compilation Phase 2# Parser and translator
        Parser parser= new Parser();
        parser.runParser();
    }
    
}
