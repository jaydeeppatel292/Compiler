package models.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class SymTabEntry {
	public String m_entry    = null; 
	public SymTab m_subtable = null;
	public SymbolType symbolType;
	public int varDimensionSize;
	public String symbolName;
	public String returnType;
	public String extraData;
	public SymTabEntry(String p_entry){
		m_entry = p_entry;
	}
	
	public SymTabEntry(String p_entry, SymTab p_subtable){
		m_entry = p_entry;		
		m_subtable = p_subtable;
	}

	public enum SymbolType{
		FUNCTION,
		CLASS,
		PARAMETER,
		VARIABLE;
	}
}
