package models.SymbolTable;

import models.AST.IdNode;
import models.AST.Node;

import java.util.ArrayList;
import java.util.List;

public class SymTabEntry {
	private String m_type;
	public String m_entry    = null;
	public SymTab m_subtable = null;
	public SymbolType symbolType;
	public SymbolDataType symbolDataType;
	public int varDimensionSize;
	public List<Integer> dimList = new ArrayList<>();
	public String symbolName;
	public String returnType;
	public String extraData;
	public List<SymTabEntry> multiLevelInheritedSymTab =new ArrayList<>();
	public Node createdFromNode=new IdNode("");
	public int m_size;
	public int m_offset;

	public SymTabEntry(String p_entry){
		m_entry = p_entry;
	}


	public SymTabEntry(SymbolType p_kind, String p_type, String p_name){
		symbolType = p_kind;
		m_type = p_type;
		symbolName = p_name;
	}

	public List<SymTabEntry> inheritedSymTab=new ArrayList<>();
	public SymTabEntry(String p_entry, SymTab p_subtable){
		m_entry = p_entry;		
		m_subtable = p_subtable;
	}

	public void addInheritedSymTab(SymTabEntry symTab){
		inheritedSymTab.add(symTab);
	}
	public enum SymbolType{
		FUNCTION,
		CLASS,
		PARAMETER,
		VARIABLE,
		TEMPVAR,
		LITVAL,
		RETVAL;
	}
	public enum SymbolDataType{
		INT,
		FLOAT,
		CLASS;
	}
}
