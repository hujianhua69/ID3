import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionTreeNode {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("DecisionTreeNode [Attr = %s, Children = %s]", 
				new Object[] {attributeName, children});
	}
	
	/**
	 * ��ǰ�ڵ����������.
	 */
	public String attributeName;
	
	/**
	 * ��ʹ�õ�����ֵ.
	 */
	public List<String> usedAttributeNames  = new ArrayList<String>();
	
	public Map<Attribute, DecisionTreeNode> children = new HashMap<Attribute, DecisionTreeNode>(); 
}

class DecisionTreeLeafNode extends DecisionTreeNode {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("DecisionTreeLeafNode [%s]", decisionAttribute.toString());
	}
	
	Attribute decisionAttribute;
}