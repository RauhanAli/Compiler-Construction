
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complier_complete;

/**
 *
 * @author S.Rauhan Ali
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LexicalAnalyzer extends tokenSet {

   
    static int d = 0;
   static String temp2="";

   
   
    static ArrayList<tokenSet> tokens = new ArrayList<>();
//static String keywordName;
    static int lineNum = 0;
    static boolean multiline = true;

    public static void main(String[] args) throws FileNotFoundException, IOException {
      
        
  
// Open the file
        FileInputStream fstream = new FileInputStream("C:\\Users\\Lenovo\\Documents\\NetBeansProjects\\Complier_complete\\src\\complier_complete\\input.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;
       
//Read File Line By Line
        while ((strLine = br.readLine()) != null) {

            // Print the content on the console
        
          
            
               
            validator v = new validator();
            String temp = "";
            
            while((strLine.length() - 1) == d){
                if(strLine.charAt(0)=='}'){ //when only this occurs in a line.
                  
                tokenSet pun = new tokenSet("}", "}", lineNum); //as } is creating issue
                    tokens.add(pun);
              }
                break;
            }
            
            while ((strLine.length() - 1) != d) {
                
                temp = splitwords(strLine);
                
               
                if (temp.isEmpty()) {
                                      
                    
                } 
                
                else if (temp.contains("$*")) { //Multi-Line-Comment
                    String comment = String.valueOf(strLine.charAt(d));
                    d++;
                    while (!((strLine.charAt(d) == '*') && (strLine.charAt(d + 1) == '$'))) {
                        if (d == (strLine.length() - 1)) { 
                            strLine = br.readLine(); //new line
                                              
                          
                            lineNum++;
                            d = 0;
                        }
                        else {
                            comment += String.valueOf(strLine.charAt(d));
                            d++;
                        }
                    }
                    d = strLine.length() - 1;
                }
                
                else if(temp.equals(".")){  //if temp contains only .
                    tokenSet dot = new tokenSet("dot", ".", lineNum);
                            tokens.add(dot);
                           
                
                }
                
                else if (v.Validate_Float(temp)) {
                    tokenSet id = new tokenSet("float-const", temp, lineNum);
                    tokens.add(id);
                }
                else if (Character.isAlphabetic(temp.charAt(0))|| temp.charAt(0)=='_'){
                    int len=temp.length()-1;
                    if (v.Validate_Identifire(String.valueOf(temp))) {

                        if (v.Validate_IsKeyword(temp)) {
                            
                            
                            tokenSet key = new tokenSet(keywords(v.checking), temp, lineNum);
                            tokens.add(key);
                        } 
                        
                        else if(temp.charAt(len)!='_'){ //if _ not occurs at the end
                            tokenSet id = new tokenSet("ID", temp, lineNum);
                            tokens.add(id);
                        }
                        else{
                          tokenSet id = new tokenSet("inavlid identifier", temp, lineNum);
                            tokens.add(id);
                        }
                       
                    }
                   
                    else{
                      tokenSet id = new tokenSet("invalid identifier", temp, lineNum);
                       tokens.add(id);
                      
                    }

                } 
                else {
                    if (v.Validate_Int(temp)) {
                        tokenSet id = new tokenSet("int-const", temp, lineNum);
                        tokens.add(id);

                    } else {
                        tokenSet id = new tokenSet("invalid token", temp, lineNum);
                        tokens.add(id);
                    }
                }
                
               if(!temp2.isEmpty()){ //for punctuator
                     tokenSet punc = new tokenSet(temp2, temp2, lineNum);
                        tokens.add(punc);
                        temp2="";
               
               }
            }

            lineNum++;    //line count plus 
            d = 0;     //after every new line d again becomes 0
         

       
        }
      
        tokenSet endOfCode = new tokenSet("$", "$", lineNum);   //it represents the end of code
        tokens.add(endOfCode);

        File fout = new File("C:\\Users\\Lenovo\\Documents\\NetBeansProjects\\Complier_complete\\src\\complier_complete\\output.txt");
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
           
              bw.write("(Class Part, Value Part, Line Number)");
              bw.newLine();
              bw.write("<**********************************************>");
              bw.newLine();
        for (int i = 0; i < tokens.size(); i++) {
            bw.write("(" + tokens.get(i).CP + "," + tokens.get(i).VP + "," + (tokens.get(i).line + 1) + ")");
            bw.newLine();
        }
        bw.close();
        
        
       
       //SYNTAX ANALYZER
  
       
       Syntax syn= new Syntax(tokens);
       syn.validate();
         


    }


    public static String splitwords(String st) throws IOException {
        validator val = new validator();
        String temp = "";
        boolean fc = false;
        boolean invalidflt = false;
       


        
        for (int i = d; i <= st.length() - 1; i++) {
     
           
             
           
           
           
            if (fc) {
                if (st.charAt(i) == '.') {    //work as an breaker if again . occurs
                    d = i;
                    return temp;
                }
                if(st.charAt(i)==';'){  // if it occur after float value
                  
                   temp2=";";
                    
                   d=++i; //because i give ; value
                   return temp;
                }
                
                if(st.charAt(i) == ' '){
                  d=++i;
                  return temp;
                }

                if (Character.isAlphabetic(st.charAt(i))) {
                    if (invalidflt) {
                        temp += st.charAt(i);
                    } 
                    else {               //same if 6.abc any letter occurs after . return temp=6.
                        d = i;
                              
                        return temp;
                    }
                } else if (Character.isDigit(st.charAt(i))) {
                    invalidflt = true;
                     
                    temp += st.charAt(i);
                   
                }
                
                else{
                     //return temp;
                   
                     
                }

            } 
            else if (Character.isAlphabetic(st.charAt(i))) {
                temp += st.charAt(i);
           
            } 
            
            else if (st.charAt(i) == '_') {
                temp += st.charAt(i);

            }
            //for space
            else if (st.charAt(i) == ' ') {
                if (!temp.isEmpty()) {
                    i++;
                    d = i;
                    
                    if(d==(st.length()-1)){ //when space occurs just before last character 
                      
                       d=--i;
                    }

                    return temp;
                }

            }
           

            //operator
            else if (val.Validate_Operator(String.valueOf(st.charAt(i)))) {
            
                if (!temp.isEmpty()) {  //this is for breaking
                    d = i;

                    return temp;
                }
                
              

                if (val.Validate_Operator(String.valueOf(st.charAt(i + 1)))) {

                    if (val.Validate_Operator(String.valueOf(st.charAt(i)).concat(String.valueOf(st.charAt(i + 1))))) { //now check combined value like ++

                        tokenSet op = new tokenSet(val.checking_operator, String.valueOf(st.charAt(i)).concat(String.valueOf(st.charAt(i + 1))), lineNum);
                        tokens.add(op);
                         i = i + 1;
                    }
                    else{
                    
                    val.Validate_Operator(String.valueOf(st.charAt(i)));
                    tokenSet op = new tokenSet(val.checking_operator, String.valueOf(st.charAt(i)), lineNum);
                    tokens.add(op);
                    
                    }

                }
               
                /*if(val.Validate_Int(String.valueOf(st.charAt(i + 1)))){
                      temp+=st.charAt(i);
                    while(st.charAt(i+1)=='['||st.charAt(i+1)==' '){
                        i++;
                       temp+=st.charAt(i);
                    }
                    d=i;
                    if(val.Validate_Sign(temp)){
                      tokenSet no= new tokenSet("Sign-Int",temp,lineNum);
                      tokens.add(no);
                    }
                }*/
                
                else {
                    
                    val.Validate_Operator(String.valueOf(st.charAt(i)));
                    tokenSet op = new tokenSet(val.checking_operator, String.valueOf(st.charAt(i)), lineNum);
                    tokens.add(op);
                    //i++;

                }
            } 
            
          
            
       
            
            

             //punctuator
            else if (val.Validate_Punctuator(String.valueOf(st.charAt(i)))) {
              
               
               
              
               
                if (!temp.isEmpty()) {
                  
                     if((st.length() - 1) == i){ //if punc occurs at last (because next time we cant aproach this as our method moves to next line
                    temp2=String.valueOf(st.charAt(i));
                   }
                      
                   /* if ((st.length() - 1) == i) {
                        tokenSet pun = new tokenSet(String.valueOf(st.charAt(i)), String.valueOf(st.charAt(i)), lineNum);
                        tokens.add(pun);
                    } */
                    
                    d=i;
                    return temp;
                }
                
                if (st.charAt(i) == '.') { //if . comes first then we check for int like .67 
                    temp += st.charAt(i);
                    while (val.Validate_Int(String.valueOf(st.charAt(i + 1)))) {
                        i++;
                        temp += st.charAt(i);
                    }
                } 
                else {
                   
                    
                     
                      
                    tokenSet pun = new tokenSet(String.valueOf(st.charAt(i)), String.valueOf(st.charAt(i)), lineNum);
                    tokens.add(pun);
                   
                    //i++;
                    //System.out.println("in Puctuators" + String.valueOf(st.charAt(i)));
                    //d = i;
                    //return temp;
                }

            }
            //Comment Case
            else if (st.charAt(i) == '$') {
                if (st.charAt(i + 1) == '*') {  //it means it is multi line cmnt
                    temp = "$*";
                    i++;
                    d = i;
                    return temp;          
                }
                
                
                i++;
                String comment = String.valueOf(st.charAt(i));
                while (!(i == (st.length() - 1))) {   //single line comment 
                    i++;
                    comment += st.charAt(i);

                }
                
                
            } 
            
          //for float(45.87, 76.98n4, 4h6.867)
            
            else if (val.Validate_Int(String.valueOf(st.charAt(i)))) {
             
             
                temp += st.charAt(i);
                

             

            } 
            else if (st.charAt(i) == '.') {
              
           
                
               
                 if ((val.Validate_Int(temp))) {
                       
                    fc = true;
                    //    if(val.Validate_Int(String.valueOf(st.charAt(i+1)))){
                    temp += st.charAt(i);
                    // }
                } 
                 else if(val.Validate_Float(temp)){
                 
                   d=i;
                   return temp;
                 
                 }
                 else if(temp.equals("super")||temp.equals("this")){ //this . or super .
                      d=i;
                      return temp;
                 }
               
                else if (val.Validate_Identifire(temp)) {
                     
                    tokenSet id = new tokenSet("ID", temp, lineNum);
                    tokens.add(id);
                    if (i < st.length() - 1) {
                        
                        if (Character.isAlphabetic(st.charAt(i + 1))) { //a.b (after .b is also alphabet so . belongs its own class)
                            tokenSet dot = new tokenSet("dot", ".", lineNum);
                            tokens.add(dot);
                            temp = "";
                        } else {   //its means that after . digit exist
                            temp = ".";
                           
                           
                        }
                    }

                } else if (temp.isEmpty()) {
                    temp += st.charAt(i);
                    fc = true;
                } else {
                    tokenSet invalid = new tokenSet("invalid", temp, lineNum);
                    tokens.add(invalid);
                    if (i < st.length() - 1) {
                        if (Character.isAlphabetic(st.charAt(i + 1))) {
                            tokenSet dot = new tokenSet("dot", ".", lineNum);
                            tokens.add(dot);
                            temp = "";
                        } else {
                            temp = ".";
                        }
                    }

                }
                
                
                
            } 
            //Char Case
            else if (st.charAt(i) == '\'') {

                if (!temp.isEmpty()) { //if temp contains already something before first \" so just return it will not be a string constant.

                    d = i;
                    return temp;

                }

                temp += st.charAt(i);

                if (i == (st.length() - 1)) { //when \' is at end of line

                    d = i;

                    tokens.add(new tokenSet("invalid-ch", temp, lineNum));
                    temp = "";
                    break;

                }

                if (st.charAt(i + 1) == '\\') {
                    i++;
                    int num = i + 2;

                    while (i <= num && i != (st.length() - 1)) { //at any momemt i aproach at the end of line loop will end. 

                        temp += st.charAt(i);
                        i++;

                    }

                    if (temp.length() != 4) {  //we add last value of line due to above loop condition. but this is only done in one case when temp length is not
                        //equal to 4.
                        temp += st.charAt(i);
                        i++;
                    }

                    i = --i;               //decreamenting as in above loop value comes after increament i++             
                    d = i;

                    if (val.Validate_Char(temp)) {

                        tokens.add(new tokenSet("char-const", temp, lineNum));
                        temp = "";

                    } else {
                        tokens.add(new tokenSet("invalid-ch", temp, lineNum));
                        temp = "";

                    }

                } else {

                    i++;
                    int num = i + 1;

                    while (i <= num && i != (st.length() - 1)) {

                        temp += st.charAt(i);
                        i++;

                    }

                    if (temp.length() != 3) {
                        temp += st.charAt(i);
                        i++;
                    }

                    i = --i;
                    d = i;

                    if (val.Validate_Char(temp)) {

                        tokens.add(new tokenSet("char-const", temp, lineNum));
                        temp = "";

                    } else {
                        tokens.add(new tokenSet("invalid-ch", temp, lineNum));
                        temp = "";

                    }
                }

            }
//for String checking "val"
            else if (st.charAt(i) == '\"') {
                if(!temp.isEmpty()){ //if temp contains already something before first \" so just return it will not be a string constant.
                  
                    d=i;
                    return temp;
                
                }
               
               temp+=st.charAt(i);
               i++;
               
               while(st.charAt(i)!='\"'&& st.length()-1!=i){  //it will store everything until it find \" or if \" not exist we have 2nd cond
                   temp+=st.charAt(i);
                   /*if(st.charAt(i)=='\\' && st.charAt(i+1)=='\\'){ //break if double back slash occurs
                     
                      
                       i++;
                       break;
                      
                   }*/
                   i++;  
                   
               }
                 temp+=st.charAt(i);  // to add " 
               
               if(val.Validate_String(temp)){          //if the string is valid it return true
                    
                  /*int len=temp.length()-1;
                     if(temp.charAt(len)=='\\'||temp.charAt(len-1)!='\\'){
                          tokens.add(new tokenSet("invalid", temp, lineNum));
                           temp = "";
                         return temp;
                     }*/
                  
                     tokens.add(new tokenSet("string-const", temp, lineNum));
                     temp = "";
                  }
               
               else{
               
                   tokens.add(new tokenSet("invalid", temp, lineNum));
                     temp = "";
               }
             d=i;
        }
    
       /* else if (st.charAt(i) == '\'') {
                if ((i + 3 <= st.length() - 1) || (i + 2 <= st.length() - 1)) {
                    i++;
                    temp += st.charAt(i);
                    if (st.charAt(i) == '\\') {
                        temp += st.charAt(i);

                    }

                } else {
                    temp += st.charAt(i);
                }
            } */
            
            else if (st.charAt(i) == '\n') {
                return temp;
            } else {
                tokenSet id = new tokenSet("invalid", String.valueOf(st.charAt(i)), lineNum);
                tokens.add(id);

            }

        }

        d = st.length() - 1;
   
        return temp;
    }

  
    
    
    static boolean check_reserved_char(char charAt) {
        switch (charAt) {
            case 'r':
                return true;
            case 'b':
                return true;
            case 't':
                return true;
            case 'n':
                return true;
            case '\'':
                return true;
            case '\\':
                return true;

            default:
                return false;
        }
    }

    public static String keywords(int ch) {

        switch (ch) {
            case 0:
                return "DT";

            case 1:
                return "String";

            case 2:
                // keywordName="repeat";
                return "for";

            case 3:
                // keywordName="unless";
                return "till";

            case 4:
                return "do";
            case 5:
                return "return";
            case 6:
         
                return "if";
            case 7:
               
                return "shift";
            case 8:
          
                return "class";
            case 9:
            
                return "main";
            case 10:
          
                return "break";
            case 11:
                return "default";
            case 12:
              
                return "case";
            case 13:
                return "continue";
            case 14:
                return "Access Modifier";
            case 15:
                return "abst";
            case 16:
                return "fin";
            case 17:
                return "void";
            case 18:
                return "extends";
            case 19:
                return "reference";
            case 20:
                return "new";
            case 21:
                return "bool-const";
            case 22:
                return "static";
            case 23:
                return "else";
            case 24:
                return "interface";
            default:
        }
        return null;
    }

    private static boolean check_endofline(int length) {
        if (d != length) {
            return true;
        }
        return false;
    }

}

