package models.SymbolTable;
public class SymTabEntry {
	public String m_entry    = null; 
	public SymTab m_subtable = null;
	
	public SymTabEntry(String p_entry){
		m_entry = p_entry;
	}
	
	public SymTabEntry(String p_entry, SymTab p_subtable){
		m_entry = p_entry;		
		m_subtable = p_subtable;
	}
}
