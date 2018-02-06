import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ID3 {
	//��þ�����
	public DecisionTree getDecisionTree(
			List<AttributeSet> attributeSets   //�����б�
			)
	{
		DecisionTree dt = new DecisionTree();
		dt.root = new DecisionTreeNode();
		// �������DecisionTreeNode
		Queue<DecisionTreeNode> nodes = new LinkedList<DecisionTreeNode>();
		// ������DecisionTreeNode����Ӧ�����Լ�
		Queue<List<AttributeSet>> filteredAttributeSets = new LinkedList<List<AttributeSet>>();
		
		nodes.offer(dt.root);  //�������ĸ��������
		
		filteredAttributeSets.offer(attributeSets);
		do {
			
			DecisionTreeNode dtn = nodes.poll(); //������
			List<AttributeSet> correspondingAttributeSet = filteredAttributeSets.poll(); //������
			//ͨ������,��������Ϣ���������
			dtn.attributeName = getAttributeNameWithMaxInformationGain(
					correspondingAttributeSet, dtn.usedAttributeNames);
			//���ù���������
			dtn.usedAttributeNames.add(dtn.attributeName);
			dtn.children = getAttributesUsingAttributeName(dtn.attributeName, 
					correspondingAttributeSet, dtn.usedAttributeNames);
			
			// ������ѷ������Խ������ӽڵ�
			for ( Map.Entry<Attribute, DecisionTreeNode> e : dtn.children.entrySet() ) {
				List<AttributeSet> restAttributeSet = getAttributeSets(e.getKey(), correspondingAttributeSet);
				// ����Ƿ��������Լ����ѱ�ȫ������
				if ( getEntropy(restAttributeSet) == 0 ) {
					DecisionTreeLeafNode dtln = new DecisionTreeLeafNode();
					Attribute decisionAttribute = null;
					if ( restAttributeSet.size() != 0 ) {
						decisionAttribute = restAttributeSet.get(0).getDecisionAttribute();
						dtln.decisionAttribute = decisionAttribute;
					}
					e.setValue(dtln);
				} else {
					nodes.offer(e.getValue());
					filteredAttributeSets.offer(restAttributeSet);
				}
			}
		} while ( !nodes.isEmpty() );
		return dt;
	}
	
	/**
	 * ��ȡ�������ĺ��ӽڵ�.
	 * @param attributeName �����ڵ�����Լ����ѵ���������
	 * @param attributeSets ���ڵ�����Լ�
	 * @param usedAttributeNames �Ѿ�ʹ�õ���������
	 * @return
	 */
	private Map<Attribute, DecisionTreeNode> getAttributesUsingAttributeName(
			String attributeName, List<AttributeSet> attributeSets, List<String> usedAttributeNames) {
		Map<Attribute, DecisionTreeNode> decisionTreeNodeChildren = 
				new HashMap<Attribute, DecisionTreeNode>();
		for ( AttributeSet as : attributeSets ) { 
			Attribute attr = as.getAttribute(attributeName);
			if ( !decisionTreeNodeChildren.containsKey(attr) ) {
				DecisionTreeNode dtn = new DecisionTreeNode();
				dtn.usedAttributeNames = new ArrayList<String>(usedAttributeNames);
				decisionTreeNodeChildren.put(attr, dtn);
			}
		}
		return decisionTreeNodeChildren;
	}
	
	/**
	 * ������(��Ϣ�������)�ķ�������.
	 * @param attributeSets ����������Լ�
	 * @param usedAttributeNames �Ѿ�ʹ�ù��ķ������� 
	 * @return ��ѷ������Ե�����
	 */
	private String getAttributeNameWithMaxInformationGain(List<AttributeSet> attributeSets, List<String> usedAttributeNames) {
		if ( attributeSets.size() == 0 ) {
			return null;
		}
		String bestClassificationAttributeName = null;
		double minEntropy = Double.MAX_VALUE;
		List<String> classificationAttributeNames = attributeSets.get(0).getClassificationAttributeNames();
		for ( String classificationAttributeName : classificationAttributeNames ) {
			if ( usedAttributeNames == null ||
					!usedAttributeNames.contains(classificationAttributeName) ) {
				double entropy = getEntropy(classificationAttributeName, attributeSets);
				if ( entropy < minEntropy ) {
					minEntropy = entropy;
					bestClassificationAttributeName = classificationAttributeName;
				}
			}
		}
		return bestClassificationAttributeName;
	}
	
	/**
	 * ��ȡ����ĳ������ֵ�����Լ�.
	 * @param classificationAttribute ��������
	 * @param attributeSets ����������Լ�
	 * @return ����ĳ������ֵ�����Լ�
	 */
	private List<AttributeSet> getAttributeSets(Attribute classificationAttribute, List<AttributeSet> attributeSets) {
		List<AttributeSet> filteredAttributeSets = new ArrayList<AttributeSet>();
		for ( AttributeSet as : attributeSets ) { 
			Attribute attr = as.getAttribute(classificationAttribute.attributeName);
			if ( attr.attributeValue.equals(classificationAttribute.attributeValue) ) {
				filteredAttributeSets.add(as);
			}
		}
		return filteredAttributeSets;
	}

	/**
	 * ���δ����ʱ����Ϣ��.
	 * @param attributeSets ����������Լ�
	 * @return δ����ʱ����Ϣ��
	 */
	private double getEntropy(List<AttributeSet> attributeSets) {
		Map<Attribute, Integer> classCounter = new HashMap<Attribute, Integer>();
		
		for ( AttributeSet as : attributeSets ) {
			Attribute attr = as.getDecisionAttribute();
			if ( classCounter.containsKey(attr) ) {
				int count = classCounter.get(attr);
				classCounter.put(attr, count + 1);
			} else {
				classCounter.put(attr, 1);
			}
		}
		int totalCount = attributeSets.size();
		double entropy = 0;
		for ( Map.Entry<Attribute, Integer> e : classCounter.entrySet() ) {
			entropy += getEntropy(e.getValue(), totalCount);
		}
		return entropy;
	}

	/**
	 * ����ĳ���ض������Լ�����Ϣ��.
	 * @param attributeName �ض�������(����: �¶�)
	 * @param attributeSets ����������Լ�
	 * @return ĳ���ض������Ե���Ϣ��
	 */
	private double getEntropy(String attributeName, List<AttributeSet> attributeSets) {
		Map<Attribute, List<AttributeSet>> attributes = new HashMap<Attribute, List<AttributeSet>>();
		for ( AttributeSet as : attributeSets ) {
			Attribute attr = as.getAttribute(attributeName);
			if ( attributes.containsKey(attr) ) {
				List<AttributeSet> l = attributes.get(attr);
				l.add(as);
			} else {
				List<AttributeSet> l = new ArrayList<AttributeSet>();
				l.add(as);
				attributes.put(attr, l);
			}
		}
		int totalCount = attributeSets.size();
		double entropy = 0;
		for ( Map.Entry<Attribute, List<AttributeSet>> e : attributes.entrySet() ) {
			List<AttributeSet> l = e.getValue();
			entropy += (l.size() * 1.0 / totalCount) * getEntropy(e.getKey(), l);
		}
		return entropy;
	}

	/**
	 * ����ĳ���ض�������ֵ������Ϣ��.
	 * @param classificationAttribute �ض�������ֵ(����: �¶� = ��)
	 * @param attributeSets ����������Լ�
	 * @return ĳ���ض�������ֵ����Ϣ��
	 */
	private double getEntropy(Attribute classificationAttribute, List<AttributeSet> attributeSets) {
		Map<Attribute, Integer> classCounter = new HashMap<Attribute, Integer>();
		
		for ( AttributeSet as : attributeSets ) {
			Attribute attr = as.getAttribute(classificationAttribute.attributeName);
			if ( attr.attributeValue.equals(classificationAttribute.attributeValue) ) {
				Attribute decisionAttribute = as.getDecisionAttribute();
				if ( classCounter.containsKey(decisionAttribute) ) {
					int count = classCounter.get(decisionAttribute);
					classCounter.put(decisionAttribute, count + 1);
				} else {
					classCounter.put(decisionAttribute, 1);
				}
			}
		}
		int totalCount = attributeSets.size();
		double entropy = 0;
		for ( Map.Entry<Attribute, Integer> e : classCounter.entrySet() ) {
			entropy += getEntropy(e.getValue(), totalCount);
		}
		return entropy;
	}

	/**
	 * ������Ϣ��.
	 * @param currentCount ����������ֵ��ʵ������
	 * @param totalCount ����ǰ�����Ե�ʵ������
	 * @return ������ֵ����Ϣ��
	 */
	private double getEntropy(int currentCount, int totalCount) {
		if ( currentCount == 0 || totalCount == 0 ) {
			return 0;
		}
		double p = currentCount * 1.0 / totalCount;
		return -p * Math.log(p) / Math.log(2);
	}
}