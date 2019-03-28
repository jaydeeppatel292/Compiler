# Compiler

## 1 Lexical analyzer
Designed and implemented a scanner for a programming language whose lexical specifications are given below.
The scanner identifies and outputs tokens (valid words and punctuation) in the source program. Its output
is a token that can thereafter be used by the syntactic analyzer to verify that the program is syntactically
valid. When called, the lexical analyzer should extract the next token from the source program. The lexical
analyzer should be able to output a token even if the input does not form a correct program. The syntax of
the language will be specified later in assignment #2. Note that completeness of testing is a major grading
topic. You are responsible for providing appropriate test cases that test for a wide variety of valid and invalid
cases

## 2 Syntactic analyzer
Designed and implemented a syntactic analyzer for the language specified by the grammar specified below.

## 3 SDT generation
Modified the productions so that the left recursions and ambiguities are removed without modifying the language and implemented a LL(1) predictive parser using table-driven technique for the modified production rules.   

## 4 Semantic analysis
Implemented a semantic analysis phase which implies two inter-related sub-phases: (1) implementation of semantic actions for the generation of symbol tables and (2) implementation of semantic checking and typechecking semantic actions. AST is generated using Syntax-directed translation. Using visitor pattern parser can traverse AST tree traversal that triggers semantic actions for symbol table generation and sementic type checking. 

## 5 Code generation
Implemented a moon code generation phase.The following is a list of different specific constructs/concepts for which code generation needs to be implemented for all the aspects of the language to become executable:

### 5.1 Memory allocation:  
Allocate memory for arrays of basic types,objects,objects with inheritance,objects having object members,arrays of objects.  

### 5.2 Functions:  
Branch to a function’s code block, execute the code block, branch back to the calling function,Branch to a function that has been branched upon, Pass parameters as local values to the function’s code block, Upon function resolution, pass the return value back to the calling function, Function call stack mechanism and recursive function calls, Call to member functions, Call to deeply nested member function.   
 
### 5.3 Statements:  
Implementation of Moon code for every kind of statement as defined in the grammar: assignment conditional statement, loop statement, input/output statement, return statement. Correct implementation the specific branching mechanisms for control flow statements. 

### 5.4 Aggregate data members access:  
Aggregate data types such as arrays and objects contain a group of data values. Code is generated so that contained member values in such an aggregated value can be accessed when referred to as factors in an expression, or the left hand side of an assignment statement.

### 5.5 Expressions:  
Computing of the resulting value of an entire expression, including the simple case when an
expression is either a variable name of even a single literal value, up to complex expressions involving a kinds of
operators, array indexed with expressions, deeply nested object members, etc


## 6 Error/Warning Identification
The parser can also identify all the errors in the input program and print a meaningful message to the user for each error encountered as well as the location of the errors in the input file. It also has an error recovery method that permits to report all errors present in the source code. 


# Grammar

prog -> {classDecl} {funcDef} 'program' funcBody ';'  
classDecl -> 'class' 'id' [':' 'id' {',' 'id'}] '{' {varDecl} {funcDecl} '}' ';'  
funcDecl -> type 'id' '(' fParams ')' ';'  
funcHead -> type ['id' 'sr'] 'id' '(' fParams ')'  
funcDef -> funcHead funcBody ';'  
funcBody -> '{' {varDecl} {statement} '}'  
varDecl -> type 'id' {arraySize} ';'  
statement -> assignStat ';'  
 | 'if' '(' expr ')' 'then' statBlock 'else' statBlock ';'  
 | 'for' '(' type 'id' assignOp expr ';' relExpr ';' assignStat ')' statBlock ';'  
 | 'get' '(' variable ')' ';'  
 | 'put' '(' expr ')' ';'  
 | 'return' '(' expr ')' ';'  
assignStat -> variable assignOp expr  
statBlock -> '{' {statement} '}' | statement | EPSILON  
expr -> arithExpr | relExpr  
relExpr -> arithExpr relOp arithExpr  
arithExpr -> arithExpr addOp term | term  
sign -> '+' | '-'  
term -> term multOp factor | factor  
factor -> variable  
 | functionCall  
 | 'intNum' | 'floatNum'  
 | '(' arithExpr ')'  
 | 'not' factor  
 | sign factor  
variable -> {idnest} 'id' {indice}  
functionCall -> {idnest} 'id' '(' aParams ')'  
idnest -> 'id' {indice} '.'  
 | 'id' '(' aParams ')' '.'  
indice -> '[' arithExpr ']'  
arraySize -> '[' 'intNum' ']'  
type -> 'int' | 'float' | 'id'  
fParams -> type 'id' {arraySize} {fParamsTail} | EPSILON  
aParams -> expr {aParamsTail} | EPSILON  
fParamsTail -> ',' type 'id' {arraySize}  
aParamsTail -> ',' expr  
assignOp -> '='  
relOp -> 'eq' | 'neq' | 'lt' | 'gt' | 'leq' | 'geq'  
addOp -> '+' | '-' | 'or'  
multOp -> '*' | '/' | 'and'  



# Tokens Keywords 
program | class |  
if | then | else | for | get | put | return |  
int | float  

# Opreators 
eq (==) | neq (<>) | lt (<) | gt (>) | leq (<=) | geq (>=) | + | - | * | /  
sr (::) |
= | 
not | and | or  

# Punctuation 
:  | ,  | .  | ;  | [  |  ]  |  {  | }  | (  | )   


# Atomic lexical elements of the language
id ::= letter alphanum*  
alphanum ::= letter | digit | _  
integer ::= nonzero digit* | 0  
float ::= integer fraction [e[+|−] integer]  
fraction ::= .digit* nonzero | .0  
letter ::= a..z |A..Z  
digit ::= 0..9  
nonzero ::= 1..9  



# Example program

class InheritedUtility {  
  int member1;  
 };  
 
class Utility : InheritedUtility {  
  int var1[4][5][7][8][9][1][0];  
  float var2;  
  int findMax(int array[100]);  
  int findMin(int array[100]);
 };  
 
int Utility::findMax(int array[100]){  
  int maxValue;  
  int idx;  
  maxValue = array[100];  
  for( int idx = 99; idx > 0; idx = idx - 1 ){  
   if(array[idx] > maxValue) then {  
   maxValue = array[idx];}  
   else{};  
  };  
  return (maxValue);  
 };  

int Utility::findMin(int array[100]){  
  int minValue;  
  int idx;  
  minValue = array[100];  
  for( int idx = 1; idx <= 99; idx = ( idx ) + 1) {  
  if(array[idx] < maxValue + 1 / 8 or idx) then {  
  maxValue = array[idx];}  
  else{};  
  };  
  return (minValue);  
};  

float randomize(){  
 float value;  
 value = 100 * (2 + 3.0 / 7.0006);  
 value = 1.05 + ((2.04 * 2.47) - 3.0) + 7.0006 > 1 and not - 1;  
 return (value);  
 };  

program {  
 int sample[100];  
 int idx;  
 int maxValue;  
 int minValue;  
 Utility utility;  
 Utility arrayUtility[2][3][6][7];  
 for(int t = 0; t<=100 ; t = t + 1) {  
 get(sample[t]);  
 sample[t] = (sample[t] * randomize());  
 };  
 maxValue = utility.findMax(sample);  
 minValue = utility.findMin(sample);  
 utility.var1[4][1][0][0][0][0][0] = 10;  
 arrayUtility[utility.var1[1][2][3][4][5][6][idx+maxValue]][1][1][1].var2 = 2.5;  
 put(maxValue);  
 put(minValue);  
}; 

