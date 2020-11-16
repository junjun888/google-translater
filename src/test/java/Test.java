import org.hwj.GoogleApi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        // 普通方式初始化
        GoogleApi googleApi = new GoogleApi();
        // 通过代理
//        GoogleApi googleApi = new GoogleApi("122.224.227.202", 3128);
        String result = googleApi.translate("神马",  "ja");
        System.out.println(result);

//        transAndroidXml(googleApi);
        transText(googleApi);

    }

    // 翻译普通文本
    // 放到in.txt中，逐行翻译
    static void transText(GoogleApi googleApi) throws Exception{
        String[] langs = {"ar","es","de","fr","ja","ko","zh-CN","zh-TW"};

        List<String> vals = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new FileReader("in.txt"));
        String s;
        while ((s = br.readLine()) != null) {
            if (s.trim().length() == 0) {
                vals.add("");
            } else {
                String val = s.trim();
                vals.add(val);
            }
        }
        br.close();

        String path = "out.txt";
        File file = new File(path);
        if (file.exists()) file.delete();

        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        for (String lang : langs) {
            log("trans..." + lang);
            bw.append("\n\n--------" + lang + "------\n");

            for (String val : vals) {
                if (val.length() == 0) {
                    bw.append("\r\n");
                } else {
                    String trans = googleApi.translate(val, lang);
                    log(trans);
                    bw.append(trans);
                    bw.append("\n");
                }
            }
        }
        bw.close();
    }

    // 翻译Android xml文本
    // 放到in.txt中，逐行翻译
    // 格式：<string name="test">Test</string>
    static void transAndroidXml(GoogleApi googleApi) throws Exception{
        String[] langs = {"ar","es","de","fr","ja","ko","zh-CN","zh-TW"};

        List<String> keys = new ArrayList<String>();
        List<String> vals = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new FileReader("in.txt"));
        String s;
        while ((s = br.readLine()) != null) {
            if (s.trim().length() == 0) {
                keys.add("");
                vals.add("");
            } else {
                String key = s.substring(s.indexOf("=\"") + 2, s.indexOf("\">"));
                keys.add(key);

                String val = s.substring(s.indexOf("\">") + 2, s.indexOf("</"));
                vals.add(val);
            }
        }
        br.close();

        String path = "out.txt";
        File file = new File(path);
        if (file.exists()) file.delete();

        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        for (String lang : langs) {
            log("trans..." + lang);
            bw.append("\n\n--------" + lang + "------\n");

            int i = 0;
            for (String val : vals) {
                if (val.length() == 0) {
                    bw.append("\r\n");
                } else {
                    String key = keys.get(i);
                    String trans = googleApi.translate(val, lang);
                    log(trans);
                    String res = "\t<string name=\"" +  key + "\">" + trans + "</string>";
                    bw.append(res);
                    bw.append("\n");
                }
                i++;
            }
        }
        bw.close();
    }

    static void log(String s) {
        System.out.println(s);
    }

}
