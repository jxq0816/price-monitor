import com.inter3i.monitor.util.DateComparatorHelp;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by boxiaotong on 2017/6/15.
 */
public class TaskTest {
    @Test
    public void test() {
        ArrayList<HashMap<String, Object>> list= new ArrayList<HashMap<String, Object>>(2);

        HashMap tempMap = new HashMap<String, Object>();
        tempMap.put("index", 2);
        tempMap.put("date", "2016-06-23");   //派序标示
        list.add(tempMap);

        HashMap tempMap2 = new HashMap<String, Object>();
        tempMap2.put("index", 1);
        tempMap2.put("date", "2016-06-21");   //派序标示
        list.add(tempMap2);

        Collections.sort(list, new DateComparatorHelp());
        System.out.print(list);
    }
}
