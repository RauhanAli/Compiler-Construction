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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class validator {
  
     static int checking;
     static int opCount=0;
  static String checking_operator="";
 static String[][] key={{"int","char","decimal","bool"} //Keywords dictionary 
               ,{"String"}
       ,{"for"}
       ,{"till"}
       ,{"do"}
       ,{"return"}
       ,{"if"}
       ,{"shift"}
       ,{"class"}
       ,{"main"}
       ,{"break"}
       ,{"default"}
       ,{"case"}
       ,{"continue"}
        ,{"public","secure","protected"} ,
         {"abst"},
         {"fin"},
         {"void"},
         {"extends"},
         {"super","this"},
         {"new"},
         {"true","false"},
         {"static"},
         {"else"},
         {"interface"}
       
    
         
       };
         
  public validator() {
    }
    Matcher matcher;
    
    public boolean Validate_IsKeyword(String word){
        //System.out.println("c:");
       // System.out.println("Keyl"+key.length);

        for(int row = 0; row < key.length; row++) {
            for (int j = 0; j <key[row].length ; j++) {
               if(word.equals(key[row][j])){
               
                   checking=row;
                  // System.out.println("key::::"+checking);
               return true;
               }
               
            }
        }
        return false;
 
    }
    public boolean Validate_Punctuator(String temp){
           
       
            switch (temp.charAt(0)) {
                case '(':
                    //temp = "(";
                    return true;
                   
                case ')':
                    //temp = ")";
                    return true;
                case '[':
                    //temp = "[";
                     return true;
                case ']':
                   // temp = "]";
                     return true;
                 case '{':
                  
                     return true;
                case '}':
                   
                     return true;
                case ',':
                //temp=",";
                 return true;
                case ':':
                 return true;
                case ';':
                 return true;
                
              
               
                    
                default:
                     return false;
            }
            
        }
    
  
        
    public boolean Validate_Operator(String str){
    		
		if (str.equals("=")) {
                     checking_operator = "Assignment Operator";
                     return true;
		} else if(str.equals("!")){
                    checking_operator = "Not";
                     return true;
                }
                else if (str.equals("+") || str.equals("-")) {
		checking_operator = "PM";
		return true;
                } else if ( str.equals("*") || str.equals("/") || str.equals("%")) {
		checking_operator = "MDM";
		return true;
                }
                else if (str.equals("==") || str.equals(">") || str.equals("<")
				|| str.equals(">=") || str.equals("<=") 
                       || str.equals("!=") ) {
			checking_operator = "Relational Operator";
		return true;
                } 
                else if (str.equals("*=") || str.equals("+=")||str.equals("/=")) {
			checking_operator = "Compound Assignment";
		             return true;
                }
                else if (str.equals("&") || str.equals("|")) {
			checking_operator = "Logical Operator";
		             return true;
                } else if (str.equals("++") || str.equals("--")) {
			checking_operator = "IncDec";
		return true;
                }
                else if (str.equals("<<") || str.equals(">>")) {
			checking_operator = "Shift Operator";
		return true;
                }
             

                
                else 
                    checking_operator = "";
                        return false;
		
    }
  
    
    public boolean Validate_Identifire(String word){
     
        matcher=Identifier.matcher(word);
        //System.out.println("identifier");
        return matcher.matches();
     
    }
 public boolean Validate_Float(String word){
matcher=Floating.matcher(word);
     //System.out.println("float");
      
return matcher.matches();
 }

 
 public boolean Validate_SimpleFloat(String word){
matcher=simpleFloat.matcher(word);
    // System.out.println("simple float");
      
return matcher.matches();
 }
 public boolean Validate_Int(String word){              
 matcher=Unsigned.matcher(word);
        return matcher.matches();
   }
  public boolean Validate_Sign(String word){              
 matcher=Signed.matcher(word);
        return matcher.matches();
   }
 public boolean Validate_Char(String word){
  matcher=Character.matcher(word);
        return matcher.matches();   
  }
 public boolean Validate_String(String word){
   matcher=String.matcher(word);
  
         
        return matcher.matches();
   }
//----- ---------------RE for character -------------------// 
           Pattern Character = Pattern.compile("(\'(\\\\(\\\\|\"|\'|r|b|t|n|o))\')|(\'.\')");
//----- ---------------RE for single unsigned integer -------------------// 
           Pattern Unsigned = Pattern.compile("\\d+");
//----- ---------------RE for single signed integer -------------------// 
           Pattern Signed = Pattern.compile("[+-]\\d+");
//----- ---------------RE for String -------------------// 
           Pattern String = Pattern.compile("\"((\\\\[\'\"\\\\])|(\\\\[bfnrt0])|([\\!#-&\\(-/0-9:-@A-Z\\[\\]-`ac-eg-mo-qsu-z\\{-~])|([bnfrt0])|(\\s))*\"");
//----- ---------------RE of floating point number -------------------// 
           Pattern Floating= Pattern.compile("[+-]?\\d*\\.\\d*([Ee]([+-]?\\d+))?");
//--------------RE for simple float 
           Pattern simpleFloat=Pattern.compile("[-+]?[0-9]*\\.?[0-9]*");
//--------------------RE for identifier------------------//
         Pattern Identifier = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]{0,30}");
          
           
   
    
}

