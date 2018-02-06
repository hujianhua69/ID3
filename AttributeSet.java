import java.util.List;
public interface AttributeSet {
	Attribute getAttribute(String attributeName);//获得属性
	
	Attribute getDecisionAttribute();//获得决策属性
	
	List<String> getClassificationAttributeNames(); //获得分类属性名
}
