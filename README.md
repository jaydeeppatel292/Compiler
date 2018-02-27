# Compiler

prog -> cd fd program funcBody ;
cd -> classDecl cd | EPSILON
fd -> funcDef fd | EPSILON

classDecl -> class id oid { varfundecl } ;
oid -> : id mid | EPSILON
mid -> , id mid | EPSILON


varfundecl -> type id varFunTail | EPSILON
varFunTail -> ar ; varfundecl | ( fParams ) ; funcDeclRep
funcDeclRep -> funcDecl funcDeclRep | EPSILON

ar -> arraySize ar | EPSILON
type -> int | float | id 
arraySize -> [ integer ]

idsrid -> sr id | EPSILON

funcBody -> { VDST }

VDST -> id VDSTPrime | int id ar ; VDST | float id ar ; VDST | statementPrime ST | EPSILON
VDSTPrime -> id ar ; VDST | variableTail assignOp expr ; ST
statementPrime ->  if ( expr ) then statBlock else statBlock ; | forBlock | get ( variable ) ; | put ( expr ) ; | return ( expr ) ;
ST -> statement ST | EPSILON

funcDef ->  funcHead funcBody ;
funcHead -> type id idsrid ( fParams )
funcDecl -> type id ( fParams ) ;
fParams -> type id ar fpTail | EPSILON
fpTail -> fParamsTail fpTail | EPSILON
fParamsTail -> , type id ar


statement -> assignStat ; | if ( expr ) then statBlock else statBlock ; | forBlock | get ( variable ) ; | put ( expr ) ; | return ( expr ) ;
forBlock -> for ( type id assignOp expr ; relExpr ; assignStat ) statBlock ;
assignStat -> variable assignOp expr



statBlock -> { ST } | statement | EPSILON


expr -> arithExpr exprTail
exprTail -> EPSILON | relOp arithExpr
relExpr -> arithExpr relOp arithExpr

arithExpr -> term arithExprTAIL
arithExprTAIL -> addOp term arithExprTAIL | EPSILON

exprTail -> EPSILON | relOp arithExpr

term -> factor termTail
termTail -> EPSILON | multOp factor termTail


factor -> factorTemp | integer | float | ( arithExpr ) | not factor | sign factor
factorTemp ->  id factorPrime
factorPrime -> indiceRep factorTempA | ( aParams ) factorTempA
factorTempA -> . factorTemp | EPSILON

indiceRep -> indice indiceRep | EPSILON
indice -> [ arithExpr ]

variable -> id variableTail
variableTail -> indiceRep variablePrime | ( aParams ) . variable
variablePrime -> . variable | EPSILON

aParams -> expr aParamsTailRep | EPSILON
aParamsTailRep -> aParamsTail aParamsTailRep | EPSILON 
aParamsTail -> , expr

addOp -> + | - | or
relOp -> eq | neq | lt | gt | leq | geq
sign -> + | -
multOp -> * | / | and
assignOp -> =

