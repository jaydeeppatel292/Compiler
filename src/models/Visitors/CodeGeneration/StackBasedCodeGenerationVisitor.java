package models.Visitors.CodeGeneration;

import models.AST.*;
import models.SymbolTable.SymTab;
import models.SymbolTable.SymTabEntry;
import models.Terminal;
import models.Visitors.Visitor;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Stack;

/**
 * Visitor to generate moon code for simple expressions and assignment and put
 * statements. Also include code for function calls using a stack-based model.
 */

public class StackBasedCodeGenerationVisitor extends Visitor {

    public Stack<String> m_registerPool = new Stack<String>();
    public String m_moonExecCode = new String();              // moon instructions part
    public String m_moonDataCode = new String();              // moon data part
    public String m_mooncodeindent = new String("          ");
    public String m_outputfilename = new String();

    public StackBasedCodeGenerationVisitor() {
        // create a pool of registers as a stack of Strings
        // assuming only r1, ..., r12 are available
        for (Integer i = 12; i >= 1; i--)
            m_registerPool.push("r" + i.toString());
    }

    public StackBasedCodeGenerationVisitor(String p_filename) {
        this.m_outputfilename = p_filename;
        // create a pool of registers as a stack of Strings
        // assuming only r1, ..., r12 are available
        for (Integer i = 12; i >= 1; i--)
            m_registerPool.push("r" + i.toString());
    }

    public VarNode getVarNodeFromArithNode(ArithExprNode arithExprNode) {
        if (arithExprNode.getChildren().size() > 0) {
            Node termNode = arithExprNode.getChildren().get(0);
            if (termNode.getChildren().size() > 0) {
                Node factorNode = termNode.getChildren().get(0);
                if (factorNode.getChildren().size() > 0 && factorNode.getChildren().get(0) instanceof VarNode) {
                    return (VarNode) factorNode.getChildren().get(0);
                }
            }
        }
        return null;
    }

    public void printRegisterDataOnScreen(Node p_node, String localregister1) {
        m_moonExecCode += m_mooncodeindent + "% put value on stack:" + localregister1 + "\n";
        // make the stack frame pointer point to the called function's stack frame
        m_moonExecCode += m_mooncodeindent + "addi r14,r14," + p_node.symtab.m_size + "\n";
        // copy the value to be printed in the called function's stack frame
        m_moonExecCode += m_mooncodeindent + "sw -8(r14)," + localregister1 + "\n";
        m_moonExecCode += m_mooncodeindent + "% link buffer to stack\n";
        m_moonExecCode += m_mooncodeindent + "addi " + localregister1 + ",r0, buf\n";
        m_moonExecCode += m_mooncodeindent + "sw -12(r14)," + localregister1 + "\n";
        m_moonExecCode += m_mooncodeindent + "% convert int to string for output\n";
        m_moonExecCode += m_mooncodeindent + "jl r15, intstr\n";
        // receive the return value in r13 and right away put it in the next called function's stack frame
        m_moonExecCode += m_mooncodeindent + "sw -8(r14),r13\n";
        m_moonExecCode += m_mooncodeindent + "% output to console\n";
        m_moonExecCode += m_mooncodeindent + "jl r15, putstr\n";
        // make the stack frame pointer point back to the current function's stack frame
        m_moonExecCode += m_mooncodeindent + "subi r14,r14," + p_node.symtab.m_size + "\n";
    }

    public String getRegisterOfVar(VarNode node) {
        SymTab searchSymTab = null;
        String localregister1 = this.m_registerPool.pop();
        String localregisterFinal = this.m_registerPool.pop();
        m_moonExecCode += m_mooncodeindent + "% processing: Var:\n";
        // initialize localregister1 as 1 for multiplication
        SymTabEntry symTabEntry = node.symtab.lookupName(node.getData());
        if (symTabEntry != null) {
            m_moonExecCode += m_mooncodeindent + "addi " + localregisterFinal + ",r0," + symTabEntry.m_offset + "\n";
        } else {
            m_moonExecCode += m_mooncodeindent + "addi " + localregisterFinal + ",r0,0\n";
        }
        boolean isDataMember = false;
        for (int i = 0; i < node.getChildren().size(); i++) {
            if (node.getChildren().get(i) instanceof VarElementNode) {
                VarElementNode varElementNode = (VarElementNode) node.getChildren().get(i);
                int varElementOffset = getOffsetOfVarElement(varElementNode, searchSymTab);
                if (varElementOffset == -1) {
                    if (varElementNode.getChildren().get(0) instanceof FCallNode) {
                        m_moonExecCode += m_mooncodeindent + "addi " + localregisterFinal + ",r14," + varElementNode.symtabentry.m_offset + "\n";
                        isDataMember = false;
                    } else {
                        m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + varElementNode.symtabentry.m_offset + "(r14)\n";
                        m_moonExecCode += m_mooncodeindent + "add " + localregisterFinal + "," + localregisterFinal + "," + localregister1 + "\n";
                        isDataMember = true;
                    }
                } else {
                    m_moonExecCode += m_mooncodeindent + "addi " + localregisterFinal + ",r14," + varElementOffset + "\n";
                    if(node.getChildren().size()>1) {
                        isDataMember = true;
                    }else {
                        isDataMember = false;
                    }
                }
            }
        }
        if(isDataMember) {
            m_moonExecCode += m_mooncodeindent + "% Adding relative offset:\n";
            m_moonExecCode += m_mooncodeindent + "add " + localregisterFinal + "," + localregisterFinal + ",r14\n";
        }
        this.m_registerPool.push(localregister1);
        return localregisterFinal;
    }


    public int getOffsetOfVarElement(VarElementNode varElementNode, SymTab searchSymTab) {
        if (searchSymTab == null) {
            searchSymTab = varElementNode.symtab;
        }
        if (varElementNode.getChildren().get(0) instanceof FCallNode) {
            if (varElementNode.symtabentry != null && varElementNode.symtabentry.symbolType == SymTabEntry.SymbolType.RETVAL) {
                return -1;
            }
        }
        if (varElementNode.getChildren().get(0) instanceof DataMemberNode) {
            DataMemberNode dataMemberNode = (DataMemberNode) varElementNode.getChildren().get(0);
            Node idNode = dataMemberNode.getChildren().get(0);
            List<Node> dimList = dataMemberNode.getChildren().get(1).getChildren();

            SymTabEntry symTabEntry = searchSymTab.lookupName(idNode.m_moonVarName);
            int offset = symTabEntry.m_offset;
            System.out.println("MoonVarName:" + idNode.m_moonVarName + "Offset:" + offset + " Size:" + symTabEntry.m_size);
            if (dimList.size() == 0) {
                return offset;
            }
            String localregister1 = this.m_registerPool.pop();
            String localregister2 = this.m_registerPool.pop();
            m_moonExecCode += m_mooncodeindent + "% processing: dimension of:" + idNode.m_moonVarName + " :=  dimension tempvar:" + varElementNode.m_moonVarName + "\n";
            // initialize localregister1 as 1 for multiplication
            m_moonExecCode += m_mooncodeindent + "addi " + localregister1 + ",r0,1\n";

            for (Node dimNode : dimList) {
                m_moonExecCode += m_mooncodeindent + "lw " + localregister2 + "," + searchSymTab.lookupName(dimNode.m_moonVarName).m_offset + "(r14)\n";
                // add operands
                m_moonExecCode += m_mooncodeindent + "mul " + localregister1 + "," + localregister1 + "," + localregister2 + "\n";
                // deallocate the registers
            }
            // assign the result into a temporary variable (assumed to have been previously created by the symbol table generator)
            m_moonExecCode += m_mooncodeindent + "sw " + varElementNode.symtabentry.m_offset + "(r14)," + localregister1 + "\n";


            this.m_registerPool.push(localregister1);
            this.m_registerPool.push(localregister2);

        }
        return -1;
    }

    public void visit(ProgNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
        // if the Visitor was given a file name,
        // then write the generated code into this file
        if (!this.m_outputfilename.isEmpty()) {
            File file = new File(this.m_outputfilename);
            try (PrintWriter out = new PrintWriter(file)) {
                out.println(this.m_moonExecCode);
                out.println(this.m_moonDataCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ;

    public void visit(NumNode p_node) {
        // First, propagate accepting the same visitor to all the children
        // This effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
        // Then, do the processing of this nodes' visitor
        // create a local variable and allocate a register to this subcomputation
        String localregister1 = this.m_registerPool.pop();
        // generate code
        m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + " := " + p_node.getData() + "\n";
        // create a value corresponding to the literal value
        m_moonExecCode += m_mooncodeindent + "addi " + localregister1 + ",r0," + p_node.getData() + "\n";
        // assign this value to a temporary variable (assumed to have been previously created by the symbol table generator)
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.symtabentry.m_offset + "(r14)," + localregister1 + "\n";
        // deallocate the register for the current node
        this.m_registerPool.push(localregister1);
    }

    public void visit(AddOpNode p_node) {
        // First, propagate accepting the same visitor to all the children
        // This effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
        // Then, do the processing of this nodes' visitor
        // allocate registers to this subcomputation
        String localregister1 = this.m_registerPool.pop();
        String localregister2 = this.m_registerPool.pop();
        String localregister3 = this.m_registerPool.pop();
        String localregister4 = this.m_registerPool.pop();
        // generate code
        m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + " := " + p_node.getChildren().get(0).m_moonVarName + " + " + p_node.getChildren().get(1).m_moonVarName + "\n";
        // load the values of the operands into registers
        m_moonExecCode += m_mooncodeindent + "lw " + localregister2 + "," + p_node.symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_offset + "(r14)\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localregister3 + "," + p_node.symtab.lookupName(p_node.getChildren().get(1).m_moonVarName).m_offset + "(r14)\n";
        // add operands
        m_moonExecCode += m_mooncodeindent + "add " + localregister4 + "," + localregister2 + "," + localregister3 + "\n";
        // assign the result into a temporary variable (assumed to have been previously created by the symbol table generator)
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.symtab.lookupName(p_node.m_moonVarName).m_offset + "(r14)," + localregister4 + "\n";
        // deallocate the registers
        this.m_registerPool.push(localregister1);
        this.m_registerPool.push(localregister2);
        this.m_registerPool.push(localregister3);
        this.m_registerPool.push(localregister4);
    }

    public void visit(ArithExprNode p_node) {
        // First, propagate accepting the same visitor to all the children
        // This effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);

        if (p_node.getChildren().size() == 1) {
            return;
        }
        // Then, do the processing of this nodes' visitor
        // allocate registers to this subcomputation
        String localregister1 = this.m_registerPool.pop();
        String localregister2 = this.m_registerPool.pop();
        String localregister3 = this.m_registerPool.pop();
        String localregister4 = this.m_registerPool.pop();
        // generate code
        m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + " := " + p_node.getChildren().get(0).m_moonVarName + " + " + p_node.getChildren().get(1).m_moonVarName + "\n";
        // load the values of the operands into registers
        m_moonExecCode += m_mooncodeindent + "lw " + localregister2 + "," + p_node.symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_offset + "(r14)\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localregister3 + "," + p_node.symtab.lookupName(p_node.getChildren().get(1).m_moonVarName).m_offset + "(r14)\n";
        // add operands
        m_moonExecCode += m_mooncodeindent + "add " + localregister4 + "," + localregister2 + "," + localregister3 + "\n";
        // assign the result into a temporary variable (assumed to have been previously created by the symbol table generator)
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.symtab.lookupName(p_node.m_moonVarName).m_offset + "(r14)," + localregister4 + "\n";
        // deallocate the registers
        this.m_registerPool.push(localregister1);
        this.m_registerPool.push(localregister2);
        this.m_registerPool.push(localregister3);
        this.m_registerPool.push(localregister4);
    }


    public void visit(MultOpNode p_node) {
        // First, propagate accepting the same visitor to all the children
        // This effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
        // Then, do the processing of this nodes' visitor
        // create a local variable and allocate a register to this subcomputation
        String localregister2 = this.m_registerPool.pop();
        String localregister3 = this.m_registerPool.pop();
        String localregister4 = this.m_registerPool.pop();
        // generate code
        m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + " := " + p_node.getChildren().get(0).m_moonVarName + " * " + p_node.getChildren().get(1).m_moonVarName + "\n";
        // load the values of the operands into registers
        m_moonExecCode += m_mooncodeindent + "lw " + localregister2 + "," + p_node.symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_offset + "(r14)\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localregister3 + "," + p_node.symtab.lookupName(p_node.getChildren().get(1).m_moonVarName).m_offset + "(r14)\n";
        // multiply operands
        m_moonExecCode += m_mooncodeindent + "mul " + localregister4 + "," + localregister2 + "," + localregister3 + "\n";
        // assign the result into a temporary variable (assumed to have been previously created by the symbol table generator)
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.symtab.lookupName(p_node.m_moonVarName).m_offset + "(r14)," + localregister4 + "\n";
        // deallocate the registers for the two children, and the current node
        this.m_registerPool.push(localregister2);
        this.m_registerPool.push(localregister3);
        this.m_registerPool.push(localregister4);
    }


    @Override
    public void visit(RelExprNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

        String localregister2 = this.m_registerPool.pop();
        String localregister3 = this.m_registerPool.pop();
        String localregister4 = this.m_registerPool.pop();
        // generate code
        m_moonExecCode += m_mooncodeindent + "% processing: " + node.m_moonVarName + " := " + node.getChildren().get(0).m_moonVarName + " "+ node.getData() +" " + node.getChildren().get(1).m_moonVarName + "\n";
        // load the values of the operands into registers
        m_moonExecCode += m_mooncodeindent + "lw " + localregister2 + "," + node.symtab.lookupName(node.getChildren().get(0).m_moonVarName).m_offset + "(r14)\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localregister3 + "," + node.symtab.lookupName(node.getChildren().get(1).m_moonVarName).m_offset + "(r14)\n";


        if(node.getData().equals(Terminal.GT.getData())){
            // multiply operands
            m_moonExecCode += m_mooncodeindent + "cgt " + localregister4 + "," + localregister2 + "," + localregister3 + "\n";

        }else if(node.getData().equals(Terminal.GEQ.getData())){
            m_moonExecCode += m_mooncodeindent + "cge " + localregister4 + "," + localregister2 + "," + localregister3 + "\n";
        }else if(node.getData().equals(Terminal.LT.getData())){
            m_moonExecCode += m_mooncodeindent + "clt " + localregister4 + "," + localregister2 + "," + localregister3 + "\n";
        }else if(node.getData().equals(Terminal.LEQ.getData())){
            m_moonExecCode += m_mooncodeindent + "cle " + localregister4 + "," + localregister2 + "," + localregister3 + "\n";
        }else if(node.getData().equals(Terminal.EQ.getData())){
            m_moonExecCode += m_mooncodeindent + "ceq " + localregister4 + "," + localregister2 + "," + localregister3 + "\n";
        }else if(node.getData().equals(Terminal.NEQ.getData())){
            m_moonExecCode += m_mooncodeindent + "cne " + localregister4 + "," + localregister2 + "," + localregister3 + "\n";
        }
        // assign the result into a temporary variable (assumed to have been previously created by the symbol table generator)
        m_moonExecCode += m_mooncodeindent + "sw " + node.symtab.lookupName(node.m_moonVarName).m_offset + "(r14)," + localregister4 + "\n";

        // deallocate the registers for the two children, and the current node
        this.m_registerPool.push(localregister2);
        this.m_registerPool.push(localregister3);
        this.m_registerPool.push(localregister4);
    }



    public void visit(AssignStatNode p_node) {
        // First, propagate accepting the same visitor to all the children
        // This effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);


        String localregister = getRegisterOfVar((VarNode) p_node.getChildren().get(0));

        /*String localregister1  = getRegisterOfVar((VarNode) p_node.getChildren().get(0));
        String localregister2  = getRegisterOfVar((VarNode) p_node.getChildren().get(1));*/
        // Then, do the processing of this nodes' visitor
        // allocate local registers
        String localregister1 = this.m_registerPool.pop();
        String localregister2 = this.m_registerPool.pop();
        String localregister3 = this.m_registerPool.pop();
        //generate code
        m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.getChildren().get(0).m_moonVarName + " := " + p_node.getChildren().get(1).m_moonVarName + "\n";
        // load the assigned value into a register
        m_moonExecCode += m_mooncodeindent + "lw " + localregister2 + "," + p_node.symtab.lookupName(p_node.getChildren().get(1).m_moonVarName).m_offset + "(r14)\n";
        // assign the value to the assigned variable
        m_moonExecCode += m_mooncodeindent + "sw -0(" + localregister + ")," + localregister2 + "\n";
        // deallocate local registers
        this.m_registerPool.push(localregister1);
        this.m_registerPool.push(localregister2);
        this.m_registerPool.push(localregister3);
        this.m_registerPool.push(localregister);
//        printRegisterDataOnScreen(p_node, localregister);

    }

    public void visit(ProgramBlockNode p_node) {
        // generate moon program's entry point
        m_moonExecCode += m_mooncodeindent + "entry\n";
        // make the stack frame pointer (address stored in r14) point
        // to the top address allocated to the moon processor
        m_moonExecCode += m_mooncodeindent + "addi r14,r0,topaddr\n";
        // propagate acceptance of this visitor to all the children
        for (Node child : p_node.getChildren())
            child.accept(this);
        // generate moon program's end point
        m_moonDataCode += m_mooncodeindent + "% buffer space used for console output\n";
        // buffer used by the lib.m subroutines
        m_moonDataCode += String.format("%-10s", "buf") + "res 20\n";
        // halting point of the entire program
        m_moonExecCode += m_mooncodeindent + "hlt\n";
    }

    public void visit(PutStatNode p_node) {
        // First, propagate accepting the same visitor to all the children
        // This effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
        // Then, do the processing of this nodes' visitor
        // create a local variable and allocate a register to this subcomputation

        String localregister2 = this.m_registerPool.pop();
        String localregister1 = this.m_registerPool.pop();

        //generate code
        m_moonExecCode += m_mooncodeindent + "% processing: put(" + p_node.getChildren().get(0).m_moonVarName + ")\n";

        if (p_node.getChildren().get(0).m_moonVarName != null) {
            // put the value to be printed into a register
            m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + p_node.symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_offset + "(r14)\n";
        } else {
            Node exprNode = p_node.getChildren().get(0);
            if (exprNode.getChildren().get(0) instanceof ArithExprNode) {
                ArithExprNode arithExprNode = (ArithExprNode) exprNode.getChildren().get(0);
                if (arithExprNode.m_moonVarName != null) {
                    localregister1 = this.m_registerPool.pop();
                    // put the value to be printed into a register
                    m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + p_node.symtab.lookupName(arithExprNode.m_moonVarName).m_offset + "(r14)\n";
                } else {
                    VarNode varNode = getVarNodeFromArithNode(arithExprNode);
                    if (varNode != null) {
                        String localregisterTemp = getRegisterOfVar(varNode);
                        m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + ",-0(" + localregisterTemp + ")\n";
                        this.m_registerPool.push(localregisterTemp);
                    }
                }
            }
        }
        m_moonExecCode += m_mooncodeindent + "% put value on stack\n";
        // make the stack frame pointer point to the called function's stack frame
        m_moonExecCode += m_mooncodeindent + "addi r14,r14," + p_node.symtab.m_size + "\n";
        // copy the value to be printed in the called function's stack frame
        m_moonExecCode += m_mooncodeindent + "sw -8(r14)," + localregister1 + "\n";
        m_moonExecCode += m_mooncodeindent + "% link buffer to stack\n";
        m_moonExecCode += m_mooncodeindent + "addi " + localregister1 + ",r0, buf\n";
        m_moonExecCode += m_mooncodeindent + "sw -12(r14)," + localregister1 + "\n";
        m_moonExecCode += m_mooncodeindent + "% convert int to string for output\n";
        m_moonExecCode += m_mooncodeindent + "jl r15, intstr\n";
        // receive the return value in r13 and right away put it in the next called function's stack frame
        m_moonExecCode += m_mooncodeindent + "sw -8(r14),r13\n";
        m_moonExecCode += m_mooncodeindent + "% output to console\n";
        m_moonExecCode += m_mooncodeindent + "jl r15, putstr\n";
        // make the stack frame pointer point back to the current function's stack frame
        m_moonExecCode += m_mooncodeindent + "subi r14,r14," + p_node.symtab.m_size + "\n";
        //deallocate local register
        this.m_registerPool.push(localregister1);
        this.m_registerPool.push(localregister2);
    }


    @Override
    public void visit(IfStatNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        String localregister1 = this.m_registerPool.pop();
        String localregister2 = this.m_registerPool.pop();

        if(node.getChildren().size()==3){
            // Accept First child so It will generate code for RelExp ..
            node.getChildren().get(0).accept(this);

            m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + node.symtab.lookupName(node.getChildren().get(0).m_moonVarName).m_offset + "(r14)\n";
            m_moonExecCode += m_mooncodeindent + "bz "  +localregister1+ ",else"+node.getChildren().get(0).m_moonVarName+"\n";

            // Accept If statement block
            node.getChildren().get(1).accept(this);
            m_moonExecCode += m_mooncodeindent + "j elseif"+node.getChildren().get(0).m_moonVarName+"\n";


            // Accept else statement block
            m_moonExecCode +=  "else"+node.getChildren().get(0).m_moonVarName+"\n";
            node.getChildren().get(2).accept(this);


            m_moonExecCode +=  "elseif"+node.getChildren().get(0).m_moonVarName+"\n";
        }

    }
    public void visit(FuncDefNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        m_moonExecCode += m_mooncodeindent + "% processing function definition: " + p_node.m_moonVarName + "\n";
        //create the tag to jump onto
        // and copy the jumping-back address value in the called function's stack frame
        m_moonExecCode += String.format("%-10s", p_node.getData()) + "sw -4(r14),r15\n";
        //generate the code for the function body
        for (Node child : p_node.getChildren())
            child.accept(this);
        // copy back the jumping-back address into r15
        m_moonExecCode += m_mooncodeindent + "lw r15,-4(r14)\n";
        // jump back to the calling function
        m_moonExecCode += m_mooncodeindent + "jr r15\n";
    }


    public void visit(ReturnStatNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        String localregister1 = this.m_registerPool.pop();
        for (Node child : p_node.getChildren())
            child.accept(this);
        // copy the result of the return value into the first memory cell in the current stack frame
        // this way, the return value is conveniently at the top of the calling function's stack frame
        m_moonExecCode += m_mooncodeindent + "% processing: return(" + p_node.getChildren().get(0).m_moonVarName + ")\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + p_node.symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_offset + "(r14)\n";
        m_moonExecCode += m_mooncodeindent + "sw " + "0(r14)," + localregister1 + "\n";
        this.m_registerPool.push(localregister1);
    }

    public void visit(FCallNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);

		/*
		String localregister1 = this.m_registerPool.pop();
		// pass parameters
		// here we assume that the parameters are the size of a word, 
		// which is not true for arrays and objects. 
		// In those cases, a loop copying the values e.g. byte-by-byte is necessary
		SymTabEntry tableentryofcalledfunction = p_node.symtab.lookupName(p_node.getData());
		int indexofparam = 0;
		m_moonExecCode += m_mooncodeindent + "% processing: function call to "  + p_node.getChildren().get(0).m_moonVarName + " \n";
		for(Node param : p_node.getChildren().get(1).getChildren()){
			m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + p_node.symtab.lookupName(param.m_moonVarName).m_offset + "(r14)\n";
			int offsetofparam = p_node.symtab.m_size + tableentryofcalledfunction.m_subtable.m_symlist.get(indexofparam).m_offset;
			m_moonExecCode += m_mooncodeindent + "sw " + offsetofparam + "(r14)," + localregister1 + "\n";
			indexofparam++;
		}
		// make the stack frame pointer point to the called function's stack frame
		m_moonExecCode += m_mooncodeindent + "addi r14,r14," + p_node.symtab.m_size + "\n";
		// jump to the called function's code
		// here the function's name is the label
		// a unique label generator is necessary in the general case
		m_moonExecCode += m_mooncodeindent + "jl r15," + p_node.getData() + "\n";
		// upon jumping back, set the stack frame pointer back to the current function's stack frame  
		m_moonExecCode += m_mooncodeindent + "subi r14,r14," + p_node.symtab.m_size + "\n";
		// copy the return value in memory space to store it on the current stack frame
		// to evaluate the expression in which it is 
		m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + p_node.symtab.m_size + "(r14)\n";
		m_moonExecCode += m_mooncodeindent + "sw " + p_node.symtab.lookupName(p_node.m_moonVarName).m_offset + "(r14)," + localregister1 + "\n";
		this.m_registerPool.push(localregister1);	*/
    }


    // Below are the visit methods for node types for which this visitor does
    // not apply. They still have to propagate acceptance of the visitor to
    // their children.
    public void visit(ClassListNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
    }

    ;

    public void visit(ClassNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
    }

    public void visit(DimListNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
    }

    public void visit(FuncDefListNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
    }

    ;

    public void visit(ParamListNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
    }

    public void visit(VarDeclNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
    }

    public void visit(IdNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
    }

    public void visit(Node p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
    }

    ;

    public void visit(StatBlockNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
    }

    public void visit(TypeNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : p_node.getChildren())
            child.accept(this);
    }

    ;


    @Override
    public void visit(OpNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);
    }

    @Override
    public void visit(AParamsNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    @Override
    public void visit(GetStatNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }


    @Override
    public void visit(ScopeSpecNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }


    @Override
    public void visit(ForStatNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    @Override
    public void visit(FParamListNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    @Override
    public void visit(FParamNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    @Override
    public void visit(FuncDeclNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    ;

    @Override
    public void visit(DataMemberNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    ;

    @Override
    public void visit(ExprNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    @Override
    public void visit(FactorNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    @Override
    public void visit(FactorSignNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    ;

    @Override
    public void visit(VarElementNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    @Override
    public void visit(VarNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }


    @Override
    public void visit(IndexListNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    @Override
    public void visit(InherListNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    @Override
    public void visit(MemberListNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }


    @Override
    public void visit(StatementNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    @Override
    public void visit(TermNode node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (Node child : node.getChildren())
            child.accept(this);

    }

    ;

}
