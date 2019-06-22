package org.hwj;

import com.alibaba.fastjson.JSONArray;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;

public class GoogleApi {

    private static final String PATH = "/gettk.js";

    static ScriptEngine engine = null;

    private Browser browser = null;

    static {
        ScriptEngineManager maneger = new ScriptEngineManager();
        engine = maneger.getEngineByName("javascript");
        FileInputStream fileInputStream = null;
        Reader scriptReader = null;

        try {
            scriptReader = new InputStreamReader(GoogleApi.class.getResourceAsStream(PATH), "utf-8");
            engine.eval(scriptReader);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (scriptReader != null) {
                try {
                    scriptReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public GoogleApi() {
        this.browser = new Browser();
    }

    public GoogleApi(String ip, Integer port) {
        this.browser = new Browser();
        this.browser.setProxy(ip, port);
    }

    public String getTKK() throws Exception {
        browser.setUrl("https://translate.google.cn/");

        try {
            String result = browser.executeGet();
            if (StringUtils.isNotBlank(result)) {
                if (result.indexOf("tkk") > -1) {
                    String matchString = RegularUtil.findMatchString(result, "tkk:.*?',");
                    String tkk = matchString.substring(5, matchString.length() - 2);
                    return tkk;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("获取 tkk 出错");
        }

        return null;
    }

    public static String getTK(String word, String tkk) {
        String result = null;

        try {
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                result = (String) invocable.invokeFunction("tk", new Object[]{word, tkk});
            }
        } catch (Exception e) {
            throw new RuntimeException("获取 tk 出错");
        }

        return result;
    }

    public String translate(String word, String from, String to) throws Exception {
        if (StringUtils.isBlank(word)) {
            return null;
        }

        String tkk = getTKK();

        if (StringUtils.isBlank(tkk)) {
            throw new RuntimeException("无法获取 tkk");
        }

        String tk = getTK(word, tkk);

        try {
            word = URLEncoder.encode(word, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer buffer = new StringBuffer("https://translate.google.cn/translate_a/single?client=t");

        if (StringUtils.isBlank(from)) {
            from = "auto";
        }

        buffer.append("&sl=" + from);
        buffer.append("&tl=" + to);
        buffer.append("&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&source=btn&kc=0");
        buffer.append("&tk=" + tk);
        buffer.append("&q=" + word);
        browser.setUrl(buffer.toString());

        try {
            String result = browser.executeGet();
            JSONArray array = (JSONArray) JSONArray.parse(result);
            JSONArray rArray = array.getJSONArray(0);
            StringBuffer rBuffer = new StringBuffer();
            for (int i = 0; i < rArray.size(); i++) {
                String r = rArray.getJSONArray(i).getString(0);
                if (StringUtils.isNotBlank(r)) {
                    rBuffer.append(r);
                }
            }

            return rBuffer.toString();
        } catch (Exception e) {
            throw new RuntimeException("结果集解析出错");
        }
    }

    /**
     * 自动检测源语言
     *
     * @param word 要翻译的词
     * @param to   翻译的目标语言, 参考谷歌接口
     * @return
     * @throws Exception
     */
    public String translate(String word, String to) throws Exception {
        return translate(word, null, to);
    }
}
