import java.util.List;
public interface AttributeSet {
	Attribute getAttribute(String attributeName);//�������
	
	Attribute getDecisionAttribute();//��þ�������
	
	List<String> getClassificationAttributeNames(); //��÷���������
}
