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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Syntax {

    static int i = 0;
    tokenSet obj = new tokenSet();
    static ArrayList<tokenSet> token = new ArrayList<>();
    static int pubCheck = 0;
    //public String [] temp = new String[5];
    ArrayList<Object []> main_table = new ArrayList<>();
    ArrayList<Object []> class_table = new ArrayList<>();
    ArrayList<Object []> func_table = new ArrayList<>();
    public Object [] temp = new Object[5];
    public Object [] temp_CT = new Object[6];
    public Object [] temp_FT = new Object[3];
    public int scope = 0;
    public int current_scope= 0 ;
    public String func_flag;

    public Syntax(ArrayList<tokenSet> token1) {

        token = token1;
    }

    public boolean FT_redec(Object [] ft){
        for (int i = 0; i<func_table.size(); i++){
            if ((ft[1].equals(func_table.get(i)[1])) && (ft[0].equals(func_table.get(i)[0]))){
                System.out.println("Redecleration Error!");
                print_MT();
                System.exit(1);
            }
        }
        return true;
    }

    public void add_func_table(Object [] ft){
        if (FT_redec(ft)){
            Object [] temp_FT2 = {ft[0],ft[1],ft[2]};
            func_table.add(temp_FT2);
            temp_FT[0]=null;
            temp_FT[1]=null;
            temp_FT[2]=null;
        }
    }
    
    public boolean MT_checkName(Object [] x){
        for (int j = 0; j < main_table.size(); j++) {
            if (x[0].equals(main_table.get(j)[0])) {
                System.out.println("Redeclaration Error "+x[0]+" is already declared! --> Line No: "+(token.get(i).line + 1));
                print_MT();
                System.exit(1);
            }
        }
        return true;
    }

    public boolean check_inherit(Object [] x){
        if (MT_checkName(x)){
            if (x[4]!=null){
                for (int i = 0; i<main_table.size();i++){
                    if (x[4].equals(main_table.get(i)[0])){
                        if(main_table.get(i)[3].toString().equals("final")){
                            System.out.println("Parent class "+x[4]+" is final, can not be inheritted! --> Line No: "+ (token.get(i).line + 3));
                            print_MT();
                            System.exit(1);
                            return false;
                        }
                        else if(x[1].equals("interface") && main_table.get(i)[1].equals("class")){
                            System.out.println("interface can not extend a class! --> Line No: "+ (token.get(i).line + 3));
                            print_MT();
                            System.exit(1);
                            return false;
                        }
                        return true;
                    }
                }
                System.out.println("Parent class "+x[4]+" not found! --> Line No: "+ (token.get(i).line + 3));
                print_MT();
                System.exit(1);
            }
            else{
                return true;
            }
        }
        return false;
    }
    // update_MT(temp)
    public void update_MT(Object [] x){
        if(check_inherit(x)){
                Object [] temp_2 = {x[0],x[1],x[2],x[3],x[4]};
                main_table.add(temp_2);
                temp[0]=null;
                temp[1]=null;
                temp[2]=null;
                temp[3]=null;
                temp[4]=null;
        }
    }


    public void validate() {

        if (start()) {

            if (i == token.size() - 1) {
                System.out.println("Valid Syntax");
            }

        }
        else {

            System.out.println("Semantic Error Occured At Line No: " + (token.get(i).line + 1));
        }
        print_MT();
        print_FT();
    }

    public void print_FT(){
        System.out.println("\n\n");
        System.out.println("FUNCTION TABLE");
        System.out.println("*****************\n");
        System.out.println("TYPE\t\t  NAME\t\t  SCOPE");
        System.out.println("********************************************************\n\n");
        for (int i = 0;i<func_table.size();i++){
            for (int j =0; j<func_table.get(i).length; j++){
                System.out.print(String.format(func_table.get(i)[j]+"\t\t\t   "));
            }
            System.out.println();
        }
    }

    public void print_MT(){
        System.out.println("\n\n");
        System.out.println("SYMBOL TABLE");
        System.out.println("******************\n");
        System.out.println("NAME\t\t\t  TYPE\t\t\t  ACCESS MODIFIER\t\t\t CATEGORY\t\t\t PARENT");
        System.out.println("************************************************************************************************************************************\n\n");
        for (int i = 0;i<main_table.size();i++){
            for (int j =0; j<main_table.get(i).length; j++){
                System.out.print(main_table.get(i)[j]+"\t\t\t       ");
            }
            System.out.println();
        }
    }

    boolean dotill_st() {

        if (token.get(i).CP.equals("do")) {
            i++;

            if (body()) {
                if (till()) {
                    if (token.get(i).CP.equals(";")) {

                        return true;
                    }
                }

            }

        }
        return false;
    }

    boolean break_st() {
        if (token.get(i).CP.equals("break") && token.get(i + 1).CP.equals(";")) {
            i += 2;
            return true;
        }

        return false;
    }

    boolean case_body2() {

        if (case_body()) {

            return case_body2();

        } else if (token.get(i).CP.equals("}") || token.get(i).CP.equals("default")) {

            // i--;  //creating issue as i++ also in c_body
            return true;
        }

        return false;

    }

    boolean case_body() {
        if (break_st()) {

            if (token.get(i).CP.equals("case") || token.get(i).CP.equals("}") || token.get(i).CP.equals("default")) {

                return case_st();
            }

        }
        if (till()) {

            return true;
        }
        if (dotill_st()) {

            return true;
        }
        if (for_st()) {

            return true;
        }
        if (if_else()) {

            return true;
        }
        if (token.get(i).VP.equals("this") && token.get(i + 1).VP.equals(".")) {
            i += 2;

            if (token.get(i).CP.equals("ID")) {

                i++;

                if (Z()) {

                    return true;
                }

            }

        }
        if (token.get(i).VP.equals("super") && token.get(i + 1).VP.equals(".")) {
            i += 2;

            if (token.get(i).CP.equals("ID")) {
                i++;
                if (Z()) {

                    return true;
                }

            }

        }

        if (token.get(i).CP.equals("IncDec")) {
            i++;
            if (token.get(i).CP.equals("ID")) {

                if (Z()) {

                    return true;
                }

            }

        }
        if (token.get(i).CP.equals("static")) {
            i++;

            if (SST4()) {
                return true;
            }

        }
        if (token.get(i).CP.equals("DT") || token.get(i).CP.equals("String")) {
            i++;

            if (SST5()) {

                return true;
            }

        }

        if (token.get(i).VP.equals("public") || token.get(i).VP.equals("secure")|| token.get(i).VP.equals("protected")) {
            i++;

            if (SST2()) {

                return true;
            }

        }

        if (token.get(i).CP.equals("ID")) {

            i++;
      
               
            if (Z()) {
               
                return true;
            } else if (token.get(i).CP.equals("ID")) {

                i++;
                if (init2()) {
                    i++;
                    return true;
                }
            }

        }

        return false;
    }

    boolean constant() {

        if (token.get(i).CP.equals("int-const")) {

            i++;

            return true;

        } else if (token.get(i).CP.equals("char-const")) {
            i++;

            return true;
        } else if (token.get(i).CP.equals("string-const")) {
            i++;

            return true;
        } else if (token.get(i).CP.equals("bool-const")) {
            i++;

            return true;
        } else if (token.get(i).CP.equals("float-const")) {

            i++;

            return true;
        }
        return false;
    }

    boolean case_st() {

        if (token.get(i).CP.equals("case")) {
            i++;
            if (constant()) {

                if (token.get(i).CP.equals(":")) {
                    i++;

                    if (case_body2()) {

                        return case_st();

                    }
                }

            }

        } else if (token.get(i).CP.equals("default") && token.get(i + 1).CP.equals(":")) {
            i += 2;

            if (MST()) {
                i++;
                return true;
            }
            return false;
        }
        if (token.get(i).CP.equals("}")) {

            return true;
        }

        return false;
    }

    boolean shift_st() {
        if (token.get(i).CP.equals("shift")) {

            i++;
            if (token.get(i).CP.equals("(")) {

                i++;
                if (OE()) {

                    if (token.get(i).CP.equals(")")) {
                        i++;

                        if (token.get(i).CP.equals("{")) {
                            i++;
                            if (case_st()) {

                                if (token.get(i).CP.equals("}")) {
                                    i++;
                                    return true;
                                }
                            }

                        }

                    }

                }
            }

        }

        return false;
    }

    boolean return_st() {

        if (token.get(i).CP.equals("return")) {

            i++;

            if (token.get(i).CP.equals(";")) {
                i++;
                return true;
            } else if (OE()) {

                if (token.get(i).CP.equals(";")) {
                    i++;
                    return true;
                }

            } else if (th()) {
                if (ZF()) {
                    if (token.get(i).CP.equals(";")) {
                        i++;
                        return true;
                    }

                }

            }

        }
        return false;
    }

    boolean th() {
        if (token.get(i).VP.equals("this") && token.get(i + 1).VP.equals(".")) {
            i += 2;
            return true;

        } else if (token.get(i).VP.equals("super") && token.get(i + 1).VP.equals(".")) {
            i += 2;
            return true;

        }

        return true;
    }

    boolean C1() {

        if (token.get(i).CP.equals(";")) {
            i++;
            return true;
        } else if (token.get(i).CP.equals("DT")) {

            i++;

            if (dec()) {
                i++;
                return true;
            }

        } else if (th()) {

            if (token.get(i).CP.equals("ID")) {

                i++;

                if (Z()) {

                    return true;
                }
            }

        }

        return false;
    }

    boolean for_Z() {

        if (token.get(i).VP.equals(".")) {
            i++;

            if (token.get(i).CP.equals("ID")) {
                i++;
                return Z();    //recursive working
            }

        } else if (X4()) {    //square brackets occurs [][]

            if (Z1()) {
                return true;
            }

        } else if (token.get(i).CP.equals("IncDec")) {
            i++;
            return true;

        } else if (assign_For()) {  //= ocuurs

            return true;
        } else if (token.get(i).CP.equals("(")) {    //function call

            i++;

            if (arguments()) {

                if (token.get(i).CP.equals(")")) {
                    i++;
                    if (Z2()) {
                        return true;
                    }
                }
            }

        }

        return false;

    }

    boolean assign_For() {

        if (token.get(i).VP.equals("=")) {
            i++;

            if (OE()) {

                return true;
            }

        } else if (token.get(i).CP.equals("Compound Assignment")) {
            i++;
            if (OE()) {

                return true;
            }

        }

        return false;
    }

    boolean for_st() {

        if (token.get(i).CP.equals("for")) {

            i++;
            if (token.get(i).CP.equals("(")) {

                i++;

                if (C1()) {

                    if (token.get(i).CP.equals(";")) { //when ;; occurs

                    } else if (!OE()) { //if OE false then it enters

                        return false;
                    }

                    if (token.get(i).CP.equals(";")) {

                        i++;
                        if (token.get(i).CP.equals("IncDec") && token.get(i + 1).CP.equals("ID")) {

                            i += 2;

                        } else if (th()) {

                            if (token.get(i).CP.equals("ID")) {
                                i++;

                                if (!for_Z()) {  //same as above OE
                                    return false;

                                }

                            }

                        }
                    }
                    if (token.get(i).CP.equals(")")) {

                        i++;

                        if (body()) {

                            return true;
                        }

                    }

                }

            }
        }
        return false;
    }

    boolean oelse() {

        if (token.get(i).CP.equals("else")) {
            i++;
            if (body()) {

                return true;
            }

        }
        return false;
    }

    boolean if_else() {

        if (token.get(i).CP.equals("if")) {
            i++;

            if (token.get(i).CP.equals("(")) {

                i++;
                if (OE()) {

                    if (token.get(i).CP.equals(")")) {

                        i++;
                        if (body()) {

                            if (oelse()) {
                                return true;
                            }
                            return true;
                        }

                    }

                }

            }

        }

        return false;
    }

    boolean inherit() {

        if (token.get(i).CP.equals("extends") && token.get(i + 1).CP.equals("ID")) {
            temp[4]=token.get(i+1).VP;
            i += 2;

        }
        return true;
    }

    boolean ZF1() {

       /* if (token.get(i).VP.equals(".") && token.get(i + 1).CP.equals("ID")) {

            i += 2;
            if (ZF1()) {

                return true;
            }

        } */
       
        if (token.get(i).CP.equals("ID")) {

            i++;
          
            if (token.get(i).VP.equals(".")) {
                i++;
            
               return ZF1();
            }
              else if (token.get(i).CP.equals("(")) {    //function call
                
            i++;
            
            if (arguments()) {
                 
                if (token.get(i).CP.equals(")")) {
                  
                    i++;
                   
                    return ZF1();
                }
            }
         
        }
            else if (X4()) {
              
               return ZF1();

        }  
            
            return true;
        }
        else if(token.get(i).VP.equals(".")){
                  
                i++;
            
               return ZF1();
            
         
        }
        
 
        else if (token.get(i).CP.equals(")") || token.get(i).CP.equals("]") || token.get(i).CP.equals(",") || token.get(i).CP.equals(";")) {   //for exit

            if (!token.get(i - 1).CP.equals("(") && !token.get(i - 1).CP.equals("[") && !token.get(i - 1).CP.equals(",")) { //its for that OE not be null

                return true;
            }

        }
        
        return false;
    }

    boolean ZF() {

        if (token.get(i).CP.equals("ID")) {

            

            if (ZF1()) {
            
                return true;
            }
          
           // return true;
        }
 
        return false;
    }
    
   

    boolean F() {
         
        if (constant()) {
          
            return true;
        } 
        if (ZF()) { //ID scenarios

            return true;
        } 
        if (token.get(i).VP.equals("this") && token.get(i + 1).VP.equals(".")) {
            i += 2;
            if (ZF()) {
                return true;
            }

        } 
        if (token.get(i).VP.equals("super") && token.get(i + 1).VP.equals(".")) {
            i += 2;
            if (ZF()) {

                return true;
            }

        } 
        if (token.get(i).VP.equals("!")) {
            i++;
            if (F()) {                        //issue

                return true;
            }

        }  
        if (token.get(i).CP.equals("(")) {     // (OE) comes in OE
            i++;
            if (OE()) {

                i++;

                if (token.get(i).CP.equals(")")) {
                            i++;
                    return true;
                }

            }

        }
        //if (token.get(i).CP.equals(")")) { //when OE is NULL

         //   return true;
        //}
        
        return false;
    }

    boolean T1() {

        if (token.get(i).CP.equals("MDM")) {

            i++;
           
            return T();

        }
          else if(token.get(i).CP.equals("PM")||token.get(i).CP.equals("Relational Operator")||token.get(i).VP.equals("&")||token.get(i).VP.equals("|")||token.get(i).CP.equals(")")||token.get(i).CP.equals("]")||token.get(i).CP.equals(";")
                    ||token.get(i).CP.equals("}")||token.get(i).CP.equals(",")){
			
			 
			
			return true;	
		}

        return false;
    }

    boolean T() {
        if (F()) {
        
            if (T1()) {
                
                return true;
            }
           
        }

        return false;
    }

    boolean E1() {

        if (token.get(i).CP.equals("PM")) {
            i++;
            return E();

        }
          else if(token.get(i).CP.equals("Relational Operator")||token.get(i).VP.equals("&")||token.get(i).VP.equals("|")||token.get(i).CP.equals(")")||token.get(i).CP.equals("]")||token.get(i).CP.equals(";")
                    ||token.get(i).CP.equals("}")||token.get(i).CP.equals(",")){
			
			 
			
			return true;	
		}

        return false;
    }

    boolean E() {

        if (T()) {
            
            if (E1()) {
                return true;
            }
         
        }

        return false;
    }

    boolean RE1() {

        if (token.get(i).CP.equals("Relational Operator")) {

            i++;
            return RE();
        }
          else if(token.get(i).VP.equals("&")||token.get(i).VP.equals("|")||token.get(i).CP.equals(")")||token.get(i).CP.equals("]")||token.get(i).CP.equals(";")
                    ||token.get(i).CP.equals("}")||token.get(i).CP.equals(",")){
			
			 
			
			return true;	
		}

        return false;
    }
    

    boolean RE() {
        if (E()) {
            if (RE1()) {
                return true;
            }
           
        }

        return false;
    }

    boolean AE1() {

        if (token.get(i).VP.equals("&")) {

            i++;

            if (RE()) {

                if (AE1()) {
                    return true;

                }
             
            }

        }
          else if(token.get(i).VP.equals("|")||token.get(i).CP.equals(")")||token.get(i).CP.equals("]")||token.get(i).CP.equals(";")
                    ||token.get(i).CP.equals("}")||token.get(i).CP.equals(",")){
			
			 
			
			return true;	
		}
        

        return false;
    }

    boolean AE() {

        if (RE()) {
            if (AE1()) {
               return true;
            }
         
        }

        return false;
    }

    boolean OE1() {
        if (token.get(i).VP.equals("|")) {
            i++;

            if (AE()) {

                if (OE1()) {
                    return true;
                }
              
            }
        }
          else if(token.get(i).CP.equals(")")||token.get(i).CP.equals("]")||token.get(i).CP.equals(";")
                    ||token.get(i).CP.equals("}")||token.get(i).CP.equals(",")){
			
			 
			
			return true;	
		}

        return false;
    }

    boolean OE() {

        if (AE()) {
            if (OE1()) {
              return true; 
            }
        }

        return false;
    }

    
    
    //For Assignment
     boolean assign_F(){
 
           if (constant()) {

            return true;
        } 
           
          else if(token.get(i).CP.equals("ID")){
                       
             i++;
              
               if (Z()) { //ID scenarios
                 
                   return true;
                 }
             
                           
             return true;
        } else if (token.get(i).VP.equals("this") && token.get(i + 1).VP.equals(".")&&token.get(i+2).CP.equals("ID")) {
            i += 3;
            if (Z()) {
                return true;
            }

        } else if (token.get(i).VP.equals("super") && token.get(i + 1).VP.equals(".")&&token.get(i).CP.equals("ID")) {
            i += 3;
            if (Z()) {

                return true;
            }

        } else if (token.get(i).VP.equals("!")) {
            i++;
            if (assign_F()) {                        //issue

                return true;
            }

        }  else if (token.get(i).CP.equals("(")) {     // (OE) comes in OE
            i++;
            if (assign_OE()) {

                i++;

                if (token.get(i).CP.equals(")")) {
                            i++;
                    return true;
                }

            }

        }
        //if (token.get(i).CP.equals(")")) { //when OE is NULL

         //   return true;
        //}
             
        return false;
    
    
       
    }
    boolean assign_T1() {
                 
        if (token.get(i).CP.equals("MDM")) {
           
            i++;
          
            return assign_T();

        }
          else if(token.get(i).CP.equals("PM")||token.get(i).CP.equals("Relational Operator")||token.get(i).VP.equals("&")||token.get(i).VP.equals("|")||token.get(i).CP.equals(")")||token.get(i).CP.equals("]")||token.get(i).CP.equals(";")
                    ||token.get(i).CP.equals("}")||token.get(i).CP.equals(",")){
			
			 
			
			return true;	
		}

        return false;
    }

    boolean assign_T() {

        if (assign_F()) {
          
            if (assign_T1()) {
                
                return true;
            }
          
        }
    
        return false;
    }

    boolean assign_E1() {
         
        if (token.get(i).CP.equals("PM")) {
            i++;
            return assign_E();

        }
         else if(token.get(i).CP.equals("Relational Operator")||token.get(i).VP.equals("&")||token.get(i).VP.equals("|")||token.get(i).CP.equals(")")||token.get(i).CP.equals("]")||token.get(i).CP.equals(";")
                    ||token.get(i).CP.equals("}")||token.get(i).CP.equals(",")){
			
			 
			
			return true;	
		}
     
        return false;
    }

    boolean assign_E() {

        if (assign_T()) {
            
            if (assign_E1()) {
                return true;
            }
           
        }

        return false;
    }

    boolean assign_RE1() {

        if (token.get(i).CP.equals("Relational Operator")) {

            i++;
            return assign_RE();
        }
        else if(token.get(i).VP.equals("&")||token.get(i).VP.equals("|")||token.get(i).CP.equals(")")||token.get(i).CP.equals("]")||token.get(i).CP.equals(";")
                    ||token.get(i).CP.equals("}")||token.get(i).CP.equals(",")){
			
			
			
			return true;	
		}

        return false;
    }

    boolean assign_RE() {
        if (assign_E()) {
            if (assign_RE1()) {
                 
               return true;
            }
          
        }
      
        return false;
    }

    boolean assign_AE1() {

        if (token.get(i).VP.equals("&")) {

            i++;

            if (assign_RE()) {

                if (assign_AE1()) {
                     
                    return true;

                }
                
            }
        }
         else if(token.get(i).VP.equals("|")||token.get(i).CP.equals(")")||token.get(i).CP.equals("]")||token.get(i).CP.equals(";")
                    ||token.get(i).CP.equals("}")||token.get(i).CP.equals(",")){
			
			
			
			return true;	
		}
              

        

        return false;
    }

    boolean assign_AE() {
       
        if (assign_RE()) {
              
            if (assign_AE1()) {
                 
              return true;
            }
        
        }
         
        return false;
    }

    boolean assign_OE1() {
        if (token.get(i).VP.equals("|")) {
            i++;

            if (assign_AE()) {

                if (assign_OE1()) {
                    return true;
                }
              
            }
        }
          else if(token.get(i).CP.equals(")")||token.get(i).CP.equals("]")||token.get(i).CP.equals(";")
                    ||token.get(i).CP.equals("}")||token.get(i).CP.equals(",")){
			
			
			
			return true;	
		}
        

        return false;
    }

    boolean assign_OE() {
       
        if (assign_AE()) {
            if (assign_OE1()) {
                   return true;
            }
            
        }
      
        return false;
    }
    
    
    
    boolean body() {

        if (token.get(i).CP.equals("{")) {
            i++;

            if (MST()) {

                i++;

                if (token.get(i).CP.equals("}")) {
                    i++;
                    return true;
                }
            }
        } else if (token.get(i).CP.equals(";")) {
            i++;
            return true;

        } else if (SST()) {

            return true;

        }

        return true;
    }

    boolean till() {

        if (token.get(i).CP.equals("till")) {

            i++;
            if (token.get(i).CP.equals("(")) {
                i++;
            }
            if (OE()) { //OE will be define

                if (token.get(i).CP.equals(")")) {
                    i++;

                }
            }

            if (body()) {

                return true;

            }

        }

        return false;
    }

    boolean assign_Opr() {
        if (token.get(i).VP.equals("=")) {

            i++;
          
            if (assign_OE()) {
                 
                if (token.get(i).CP.equals(";")) {

                    return true;
                }
            }

        } else if (token.get(i).CP.equals("Compound Assignment")) {
            i++;
            
            if (assign_OE()) {

                if (token.get(i).CP.equals(";")) {

                    return true;
                }
            }

        }

      //  i--;  //this is to avoid  } bracket

        return false;
    }

    boolean X4() {
        if (token.get(i).CP.equals("[")) {

            i++;

            if (OE()) {

                if (token.get(i).CP.equals("]")) {
                    i++;

                    if (X4()) {

                        if (X4()) {

                            return true;

                        }

                        return true;
                    }

                    return true;
                }

            }

        }
        return false;
    }

    boolean Z1() {
        if (token.get(i).VP.equals(".")) {
            i++;

            if (token.get(i).CP.equals("ID")) {
                i++;
                return Z();    //recursive working
            }

        } else if (token.get(i).CP.equals("IncDec")) {
            i++;
            if (token.get(i).CP.equals(";")) {
                i++;

                return true;
            }

        } else if (assign_Opr()) {

            i++;

            return true;
        }

        return false;
    }

    boolean arg2() {

        if (token.get(i).CP.equals(",")) {
            i++;
            return arguments();

        }
        return false;
    }

    boolean arguments() {
      
        if (OE()) {
            if (arg2()) {
                return true;
            }
           
        }
       
        if (token.get(i).CP.equals(")")) { //if argument is NULL
          
            return true;
        }
        
        return false;
    }

    boolean Z2() {

        if (token.get(i).VP.equals(".")) {
            i++;

            if (token.get(i).CP.equals("ID")) {
                i++;
                return Z();    //recursive working
            }

        } else if (X4()) {    //square brackets occurs [][]

            if (Z1()) {
                return true;
            }

        } else if (token.get(i).CP.equals(";")) {
            i++;
            return true;

        }

        return false;
    }

    boolean Z() {      //covers all scenario func call , inc-dec , Assignment
     
        if (token.get(i).VP.equals(".")) {
            i++;
              
            if (token.get(i).CP.equals("ID")) {
                i++;
                
                if(token.get(i).CP.equals(";")){
                   return true;
                }
               
                return Z();    //recursive working
            }

        } else if (X4()) {    //square brackets occurs [][]

            if (Z1()) {
                return true;
            }

        } else if (token.get(i).CP.equals("IncDec") && token.get(i + 1).CP.equals(";")) {
          
            i++;
           
            return true;

        } else if (token.get(i).CP.equals("(")) {    //function call

            i++;
             
            if (arguments()) {
             
                if (token.get(i).CP.equals(")")) {
                    i++;
                    if (Z2()) {
                        return true;
                    }
                }
            }

        } else if (assign_Opr()) {  //= ocuurs
           
            if (token.get(i).CP.equals(";")) {
                i++;
              
                return true;
            }
        }
        
        

       // i--; //to avoid } bracket
              
        return false;
    }

    boolean SST5() {

        if (dec()) {
            
            i++;

            return true;

        } else if (arr_1init()) {

            if (token.get(i).CP.equals(";")) {
                i++; //creating issue
                return true;
            }
        }
      
        return false;
    }

    boolean SST4() {
        if (token.get(i).CP.equals("DT")) {
            temp_FT[0]=token.get(i).VP;
            i++;
            if (SST5()) {
                return true;
            }

        } else if (token.get(i).CP.equals("ID")) {
            temp_FT[0]=token.get(i).VP;
            i++;
            if (token.get(i).CP.equals("ID")) {
                temp_FT[1]=token.get(i).VP;
                temp_FT[2]=scope;
                if(func_flag.equals("func"))
                {
                    add_func_table(temp_FT);
                }
                i++;
                if (init2()) {

                    i++;
                    return true;
                }
            }
        }

        return false;
    }

    boolean SST2() {
        if (token.get(i).CP.equals("static")) {
            i++;
            if (SST4()) {
                return true;
            }

        } else if (SST4()) {
            return true;
        }
        return false;
    }

    boolean SST() {

        if (till()) {

            return true;
        }
        if (dotill_st()) {

            return true;
        }
        if (shift_st()) {

            return true;
        }
        if (break_st()) {

            return true;
        }
        if (return_st()) {

            return true;
        }
        if (for_st()) {

            return true;
        }
        if (if_else()) {

            return true;
        }
        if (token.get(i).VP.equals("this") && token.get(i + 1).VP.equals(".")) {
            i += 2;
          
            if (token.get(i).CP.equals("ID")) {

                i++;
                  
                if (Z()) {

                    return true;
                }

            }

        }
        if (token.get(i).VP.equals("super") && token.get(i + 1).VP.equals(".")) {
            i += 2;

            if (token.get(i).CP.equals("ID")) {
                i++;
                if (Z()) {

                    return true;
                }

            }

        }

        if (token.get(i).CP.equals("IncDec")) {
            i++;
            if (token.get(i).CP.equals("ID")) {
                  i++;
                if (Z()) {
                    
                    return true;
                }

            }

        }
        if (token.get(i).CP.equals("static")) {
            i++;

            if (SST4()) {
                return true;
            }

        }
         if (token.get(i).CP.equals("ID")) {
                   
            i++;
          
            if (token.get(i).CP.equals("ID")) {

                i++;
                if (init2()) {
                    i++;
                    return true;
                }
            } else if (Z()) {
                 
                return true;
            }

        }
        if (token.get(i).CP.equals("DT") || token.get(i).CP.equals("String")) {
            i++;

            if (SST5()) {

                return true;
            }
           
        }

        if (token.get(i).VP.equals("public") || token.get(i).VP.equals("secure")|| token.get(i).VP.equals("protected")) {
            i++;

            if (SST2()) {

                return true;
            }
    
        }

       
     
        return false;
    }

    boolean MST() {

        if (SST()) {

            return MST();
            //return SST();
        } 
        if (token.get(i).CP.equals("}")) {

            i--;  //creating issue as i++ also in c_body
            return true;
        }

        return false;
    }

    boolean main() {

        if (token.get(i).CP.equals("main") && token.get(i + 1).CP.equals("(") && token.get(i + 2).CP.equals(")")) {

            i += 3;

            if (token.get(i).CP.equals("{")) {
                i += 1;

                if (MST()) {

                    i++;
                    return true;
                }
            }
        }

        return false;
    }

    boolean list() {

        if (token.get(i).CP.equals(";")) {
          
            return true;
        } else if (token.get(i).CP.equals(",") && token.get(i + 1).CP.equals("ID")) {

            i += 2;

            if (token.get(i).VP.equals("=")) {
                return init();
            } else if (token.get(i).CP.equals(",")) {
                return list();
            }

        }

        return false;
    }

    boolean init() {
   
        if (token.get(i).VP.equals("=")) {

            i++;
           
            if (assign_OE()) {
               
                if (token.get(i).CP.equals("}")) {

                    i--;  //to avoid } bracket
                }
                if ((token.get(i - 1).CP.equals("int-const") || token.get(i - 1).CP.equals("bool-const") || token.get(i - 1).CP.equals("char-const")
                        || token.get(i - 1).CP.equals("string-const") || token.get(i - 1).CP.equals("char-const"))
                        && token.get(i).VP.equals("=")) {     //to resove this issue int a,b=7=8; 

                    return false;
                }
                if (token.get(i).VP.equals("=")) { //float a; b=x=x; 

                    return init();

                }

                if (list()) {

                    return true;
                }

            }

        }
      
        return false;
    }

    boolean dec() {
         
        if (token.get(i).CP.equals("ID")) {
            temp_FT[0]=token.get(i-1).VP;
            temp_FT[1]=token.get(i).VP;
            temp_FT[2]=scope;
            if(func_flag.equals("func")){add_func_table(temp_FT);}
            i++;

            if (init()) {

                return true;
            } else if (list()) {
               
                return true;
            }
        }
  
        return false;
    }

    boolean arr() {

        if (token.get(i).CP.equals("[") && token.get(i + 1).CP.equals("]")) {
            i += 2;
            if (token.get(i).CP.equals("[") && token.get(i + 1).CP.equals("]")) {
                i += 2;
                if (token.get(i).CP.equals("[") && token.get(i + 1).CP.equals("]")) {
                    i += 2;
                    return true;
                }
                return true;
            }
            return true;
        }

        return false;
    }

    boolean param2() {
        if (token.get(i).CP.equals(",")) {

            i++;

            if (param()) {

                return true;
            }

        } else if (token.get(i).CP.equals("ID")) { //DT ID
            temp_FT[1]=token.get(i).VP;
            temp_FT[2]=scope;
            if(func_flag.equals("func")){
                add_func_table(temp_FT);
            }
            i++;

            if (token.get(i).CP.equals(",")) {
                if (param2()) {

                    return true;
                }
            } else if (token.get(i).CP.equals(")")) {

                return true;
            }
        } else if (token.get(i).CP.equals("[") && token.get(i + 1).CP.equals("]")) {  //DT[]

            i += 2;

            if (token.get(i).CP.equals("ID")) {
                temp_FT[1]=token.get(i).VP;
                temp_FT[2]=scope;
                if(func_flag.equals("func")) {
                    add_func_table(temp_FT);
                }
                return param2();
            } else {

                return param2();
            }

        } else if (token.get(i).CP.equals(")")) { //for exit

            return true;
        }
        i--;
        return false;
    }

    boolean param() {

        if (token.get(i).CP.equals("DT") || token.get(i).CP.equals("String")) {
            temp_FT[0]=token.get(i).VP;
            i++;
            if (param2()) {
                return true;
            }

        } else if (token.get(i).CP.equals("ID") && token.get(i + 1).CP.equals("ID")) {
            temp_FT[0]=token.get(i).VP;
            temp_FT[1]=token.get(i+1).VP;
            temp_FT[2]=scope;
            if(func_flag.equals("func")) {
                add_func_table(temp_FT);
            }
            i += 2;

            if (param2()) {
                return true;
            }

        } else if (token.get(i).CP.equals(")")) { //if param is NULL

            return true;
        }

        return false;
    }

    boolean fn_body() {

        if (token.get(i).CP.equals("ID") && token.get(i + 1).CP.equals("(")) {
            i += 2;
            func_flag="func";
            scope++;
            current_scope++;
            if (param()) {

                if (token.get(i).CP.equals(")") && token.get(i + 1).CP.equals("{")) {
                    i += 2;
                    if (MST()) {
                        i++;

                    }

                    if (token.get(i).CP.equals("}")) {
                        current_scope--;
                        func_flag="class";
                        return true;
                    }

                }
            }

        } else if (token.get(i).CP.equals("(")) {
            scope++;
            current_scope++;
            i++;
            if (param()) {

                if (token.get(i).CP.equals(")") && token.get(i + 1).CP.equals("{")) {
                    i += 2;
                    if (MST()) {
                        i++;

                    }

                    if (token.get(i).CP.equals("}")) {
                        current_scope--;
                        return true;
                    }

                }
            }

        }

        return false;
    }

    boolean f_ret_type() {

        if (token.get(i).CP.equals("ID")) {
            i++;
            if (arr()) {

                return true;
            }

        } else if (token.get(i).CP.equals("DT")) {

            i++;
            return true;
        } else if (token.get(i).CP.equals("String")) {
            i++;
            return true;
        } else if (token.get(i).CP.equals("void")) {
            i++;
            return true;
        }

        return false;
    }

    boolean abst_meth() {

        if (f_ret_type()) {

            if (token.get(i).CP.equals("ID") && token.get(i + 1).CP.equals("(")) {
                i += 2;

                if (param()) {

                    if (token.get(i).CP.equals(")") && token.get(i + 1).CP.equals(";")) {
                        i += 1; //creating issue
                        return true;
                    }
                }

            }

        }

        return false;
    }

    boolean c_body1() {

        if (class1()) {

            return true;
        } else if (abst_meth()) {

            return true;
        }

        return false;
    }

    boolean c_body2() {

        if (class1()) {

            return true;
        } else if (f_ret_type()) {
            if (fn_body()) {

                return true;
            }

        }

        return false;
    }

    boolean c_body3() {

        if (token.get(i).CP.equals("ID")) {

            if (fn_body()) {

                return true;

            } else if (dec()) {
                return true;
            }

        } else if (arr_1init()) {

            if (token.get(i).CP.equals(";")) {
                return true;
            }
        }

        return false;
    }

    boolean init2() {

        if (token.get(i).VP.equals("=") && token.get(i + 1).CP.equals("new") && token.get(i + 2).CP.equals("ID")) {

            i += 3;

            /* if(Y()){  //will design later
               
             }*/
            if (token.get(i).CP.equals("(")) {

                i++;

                if (!arguments()) {
                    return false;
                }
                if (token.get(i).CP.equals(")") && token.get(i + 1).CP.equals(";")) {

                    i++;

                    return true;
                }

            }

        }
        return false;
    }

    boolean c_body4() {

        if (token.get(i).CP.equals("ID")) {
            i++;

            if (init2()) {

                return true;
            }
            i--;  //this is because fn_body start with ID 
            if (fn_body()) {

                return true;
            }

        } else if (arr()) {

            if (fn_body()) {
                return true;
            }

        } else if (fn_body()) {

            return true;
        }

        return false;
    }

    boolean c_body9() {

        if (token.get(i).CP.equals("abst")) {

            i++;
            if (c_body1()) {

                return true;
            }
        } else if (token.get(i).CP.equals("fin")) {
            i++;
            if (c_body2()) {
                return true;
            }

        } else if (token.get(i).CP.equals("ID")) {
            i++;

            if (c_body4()) {
                return true;
            }

        } else if (token.get(i).CP.equals("DT") || token.get(i).CP.equals("String")) {
            i++;
            if (c_body3()) {
                return true;
            }

        } else if (token.get(i).CP.equals("void")) {
            i++;

            if (pubCheck == 1) {

                if (main()) {

                    return true;
                }
            }

            if (fn_body()) {
                return true;
            }

        }

        return false;
    }

    boolean c_body10() {
        if (token.get(i).CP.equals("abst")) {

            i++;
            if (c_body1()) {

                return true;
            }
        } else if (token.get(i).CP.equals("fin")) {
            i++;
            if (c_body2()) {
                return true;
            }

        } else if (token.get(i).CP.equals("ID")) {

            i++;

            if (c_body4()) {
                return true;
            }

        } else if (token.get(i).CP.equals("DT") || token.get(i).CP.equals("String")) {
            i++;
            if (c_body3()) {
                return true;
            } else if (arr_1init()) {

                return true;
            }

        } else if (token.get(i).CP.equals("void")) {
            i++;

            if (fn_body()) {
                return true;
            }

        } /*else if (token.get(i).CP.equals("class")) {

            if (class1()) {
                return true;
            }

        }*/
        return false;
    }

    boolean c_body() {

        if (token.get(i).VP.equals("secure")) {
            i++;
            temp_CT[4]="secure";

            if (token.get(i).VP.equals("static")) {
                temp_CT[5]="static";
                i++;
                if (c_body9()) {

                    return true;
                }

            } else if (c_body10()) {

                return true;
            }

        }
        if (token.get(i).VP.equals("protected")) {
            i++;
            temp_CT[4]="protected";

            if (token.get(i).VP.equals("static")) {
                temp_CT[5]="static";
                i++;
                if (c_body9()) {

                    return true;
                }

            } else if (c_body10()) {

                return true;
            }

        }

        if (token.get(i).VP.equals("public")) {
            temp_CT[4]="public";
            i++;

            if (token.get(i).VP.equals("static")) {
                temp_CT[5]="static";
                if (token.get(i + 1).CP.equals("void") && token.get(i + 2).CP.equals("main")) {
                    temp_CT[3]="void";
                    temp_CT[2]="main";
                    pubCheck = 1;  //this is to add main
                }
                i++;

                if (c_body9()) {

                    return true;
                }

            } else if (c_body10()) {

                return true;
            }

        }
        if (token.get(i).CP.equals("static")) {
            i++;
            temp_CT[5]="static";
            temp_CT[4]="default";
            if (c_body9()) {

                return true;
            }

        }

        if (token.get(i).CP.equals("abst")) {
            temp_CT[5]="abstract";
            temp_CT[4]="default";
            i++;
            if (c_body1()) {

                return true;
            }
        }
        if (token.get(i).CP.equals("fin")) {
            temp_CT[5]="final";
            temp_CT[4]="default";
            i++;
            if (c_body2()) {
                return true;
            }

        }
        if (token.get(i).CP.equals("ID")) {

            i++;

            if (c_body4()) {
                return true;
            }

        }
        if (token.get(i).CP.equals("DT") || token.get(i).CP.equals("String")) {
            i++;
            if (c_body3()) {
                return true;
            }

        }

        if (token.get(i).CP.equals("void")) {
            i++;

            if (fn_body()) {
                return true;
            }

        }

        if (token.get(i).CP.equals("}")) { //just to exit
            i--;   //creating issue
            return true;
        }

        return false;
    }

    boolean recu_cbody() { //recursive working

        if (c_body()) {

            i++;

            if (token.get(i).CP.equals("}")) {
                //function to enter in the class table!
                return true;
            } else {

                return recu_cbody();

            }
        }
        return false;
    }

    boolean int_param2() {
        if (token.get(i).CP.equals(",")) {

            i++;

            if (int_param()) {

                return true;
            }

        } else if (token.get(i).CP.equals("ID")) { //DT ID

            i++;

            if (token.get(i).CP.equals(",")) {
                if (int_param2()) {

                    return true;
                }
            } else if (token.get(i).CP.equals(")")) {

                return true;
            }
        } else if (token.get(i).CP.equals("[") && token.get(i + 1).CP.equals("]")) {  //DT[]

            i += 2;

            if (token.get(i).CP.equals("ID")) {
                return int_param2();
            } else {

                return int_param2();
            }

        } else if (token.get(i).CP.equals(")")) { //for exit

            return true;
        }
        i--;
        return false;
    }

    boolean int_param() {

        if (token.get(i).CP.equals("DT") || token.get(i).CP.equals("String")) {
            i++;

            if (int_param2()) {
                return true;
            }

        } else if (token.get(i).CP.equals("ID") && token.get(i + 1).CP.equals("ID")) {

            i += 2;

            if (int_param2()) {
                return true;
            }

        } else if (token.get(i).CP.equals(")")) { //if param is NULL

            return true;
        }

        return false;
    }

    boolean int_body(){
        if (token.get(i).CP.equals("DT") || token.get(i).CP.equals("String") || token.get(i).CP.equals("void")) {
            i++;
            if (token.get(i).CP.equals("ID")){
                i++;
                if (token.get(i).CP.equals("(")){
                    i++;
                    if (token.get(i).CP.equals(")")){
                        i++;
                        if (token.get(i).CP.equals(";")){
                            return true;
                        }
                    }
                    else if (int_param()) {

                        if (token.get(i).CP.equals(")") && token.get(i + 1).CP.equals(";")) {
                            i += 1; //creating issue
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    boolean recu_int_body() { //recursive working

        if (int_body()) {

            i++;

            if (token.get(i).CP.equals("}")) {
                //function to enter in the class table!
                return true;
            } else {

                return recu_int_body();

            }
        }
        return false;
    }

    boolean class1() {

        if (token.get(i).CP.equals("class") && token.get(i + 1).CP.equals("ID")) {
            temp[1] ="class";
            temp[0]=token.get(i+1).VP;
            temp_CT[1]=token.get(i+1).VP;
            temp[3]="null";
            func_flag="class";
            i += 2;
            if (inherit()) {
               update_MT(temp);
                if (token.get(i).CP.equals("{")) {
                    temp_CT[0]="class";
                    scope++;
                    current_scope++;
                    i++;
                    return recu_cbody();

                }

            }
        }


        return false;

    }

    boolean interface1() {

        if (token.get(i).CP.equals("interface") && token.get(i + 1).CP.equals("ID")) {
            temp[1] ="interface";
            temp[0] =token.get(i+1).VP;
            temp[3] = "null";
            i += 2;
            if (inherit()) {
                update_MT(temp);
                if (token.get(i).CP.equals("{")) {

                    i++;
                    return recu_int_body();

                }

            }
        }


        return false;

    }

    boolean def2() {
        if (token.get(i).VP.equals("fin")) {
            temp[3] ="final";
            i++;
            if (class1()) {
                return true;
            }
        } else if (token.get(i).VP.equals("abst")) {
            temp[3] ="abstract";
            i++;
            if (class1()) {

                return true;
            }

        } else if (class1()) {
            return true;
        }
        else if (interface1()) {
            return true;
        }
        temp[3]="null";
        return false;

    }



    boolean def1() {

        if (token.get(i).VP.equals("secure")) {
            temp[2]="secure";
            i++;
            if (def2()) {
                return true;
            }

        } else if (token.get(i).VP.equals("public")) {
           temp[2]="public";
            i++;
            if (interface1()) {
                temp[3]="null";
                return true;
            }

            else if (def2()) {
                return true;
            }

        } else if (token.get(i).VP.equals("protected")) {
            temp[2]="protected";
            i++;
            if (def2()) {
                return true;
            }

        } else if (def2()) {
           temp[2]="default";
            return true;

        }

        return false;
    }

    boolean start() {

        if (def1()) {
            i++;
            if (token.get(i).CP.equals("$")) {
                return true;
            } else {

                return start();
            }
        }

        return false;
    }

    boolean arr_start() {
        if (token.get(i).CP.equals("DT")) {
            i++;
            return true;

        }

        return false;
    }

    boolean arr3D() {

        if (token.get(i).CP.equals("new")) {
            i++;
            if (arr_start()) {

                if (token.get(i).CP.equals("[")) {
                    i++;
                    //Now OE will check
                    if (OE()) {

                        if (token.get(i).CP.equals("]")) {
                            i++;

                        }
                    }

                }
                if (token.get(i).CP.equals("[")) {
                    i++;
                    //Now OE will check
                    if (OE()) {

                        if (token.get(i).CP.equals("]")) {
                            i++;

                        }
                    }

                }
                if (token.get(i).CP.equals("[")) {
                    i++;
                    //Now OE will check
                    if (OE()) {

                        if (token.get(i).CP.equals("]")) {
                            i++;
                            return true;
                        }
                    }

                }

            }

        }

        return false;
    }

    boolean arr2D() {

        if (token.get(i).CP.equals("new")) {
            i++;
            if (arr_start()) {

                if (token.get(i).CP.equals("[")) {
                    i++;
                    //Now OE will check
                    if (OE()) {

                        if (token.get(i).CP.equals("]")) {
                            i++;

                        }
                    }

                }
                if (token.get(i).CP.equals("[")) {
                    i++;
                    //Now OE will check
                    if (OE()) {

                        if (token.get(i).CP.equals("]")) {
                            i++;
                            return true;
                        }
                    }

                }

            }

        }

        return false;
    }

    boolean arr1D() {

        if (token.get(i).CP.equals("new")) {

            i++;
            if (arr_start()) {

                if (token.get(i).CP.equals("[")) {
                    i++;
                    //Now OE will check
                    if (OE()) {

                        if (token.get(i).CP.equals("]")) {
                            i++;

                            return true;
                        }
                    }

                }

            }

        }

        return false;
    }

    boolean arr_3init() {
        if (token.get(i).CP.equals("[") && token.get(i + 1).CP.equals("]")) {
            i += 2;
            if (token.get(i).CP.equals("ID") && token.get(i + 1).VP.equals("=")) {
                temp_FT[0]=token.get(i-3).VP;
                temp_FT[1]=token.get(i).VP;
                temp_FT[2]=scope;
                if(func_flag.equals("func")) {
                    add_func_table(temp_FT);
                }
                i += 2;
                if (arr3D()) {
                    return true;
                }

            }

        } else if (token.get(i).CP.equals("ID") && token.get(i + 1).VP.equals("=")) {
            temp_FT[0]=token.get(i-3).VP;
            temp_FT[1]=token.get(i).VP;
            temp_FT[2]=scope;
            if(func_flag.equals("func")) {
                add_func_table(temp_FT);
            }
            i += 2;
            if (arr2D()) {
                return true;
            }

        }

        return false;
    }

    boolean arr_2init() {
        if (token.get(i).CP.equals("[") && token.get(i + 1).CP.equals("]")) {
            i += 2;
            if (arr_3init()) {
                return true;
            }
        } else if (token.get(i).CP.equals("ID") && token.get(i + 1).VP.equals("=")) {
            temp_FT[0]=token.get(i-3).VP;
            temp_FT[1]=token.get(i).VP;
            temp_FT[2]=scope;
            if(func_flag.equals("func")) {
                add_func_table(temp_FT);
            }
            i += 2;
            if (arr1D()) {
                return true;
            }

        }

        return false;
    }

    boolean arr_1init() {
        if (token.get(i).CP.equals("[") && token.get(i + 1).CP.equals("]")) {
            i += 2;
            if (arr_2init()) {
                return true;
            }
        }

        return false;
    }

    public boolean arr_dec() {

        if (arr_start()) {
            if (token.get(i).CP.equals("[") && token.get(i + 1).CP.equals("]") && token.get(i + 2).CP.equals("ID")) {
                i += 3;
                if (arr_1init()) {

                    if (token.get(i).CP.equals(";")) {
                        i++;
                        return true;
                    }

                }

            }

        }

        return false;

    }

}

