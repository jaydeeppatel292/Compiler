# Compiler

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



# Tokens
Keywords program | class |
if | then | else | for | get | put | return |
int | float

# Opreators 
eq (==) | neq (<>) | lt (<) | gt (>) | leq (<=) | geq (>=)
 + | - | * | /
 not | and | or
 =
 sr (::)

# Punctuation 
: | , | . | ; | [ | ] | { | } | ( | )


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
 int member1;};
 
class Utility : InheritedUtility {
 int var1[4][5][7][8][9][1][0];
 float var2;
 int findMax(int array[100]);
 int findMin(int array[100]);};
int Utility::findMax(int array[100]){
 int maxValue;
 int idx;
 maxValue = array[100];
 for( int idx = 99; idx > 0; idx = idx - 1 ){
 if(array[idx] > maxValue) then {
 maxValue = array[idx];}
 else{};
 };
 return (maxValue);};

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

