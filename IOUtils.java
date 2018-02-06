import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
	//���ļ��ж�ȡ����
	public static List<AttributeSet> getWeatherRecords(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);  //�ļ���
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);

		String line = "";
		//һ�����Լ�����
		List<AttributeSet> attributeSets = new ArrayList<AttributeSet>();
		while ( (line = br.readLine()) != null ) {
			String[] attributes = line.split(", ");//һ������ת��Ϊ����
			if ( attributes.length != 5 ) {
				System.out.println("[WARN] Line #" + attributeSets.size() + 1 + " is ignored.");
				continue;
			}

			attributeSets.add(new Weather(
					Weather.getOutlookAttribute(attributes[0]),
					Weather.getTemperatureAttribute(attributes[1]),
					Weather.getHumidityAttribute(attributes[2]),
					Weather.getWindAttribute(attributes[3]),
					Weather.getPlayBallAttribute(attributes[4])
			));
		}
		br.close();
		isr.close();
		fis.close();

		return attributeSets;
	}
}