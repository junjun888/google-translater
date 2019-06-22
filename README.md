# google-translater


>  1 获取 tkk
	 2 根据 tkk，和 输入内容 获取 tk
	 3 根据 word,tk ,组装 url 访问 谷歌翻译 api

------------------------------------------- 2019-06-22 --------------------------------
之前的破解不能用了，笔者，通过反编译还原了代码，并且修改了下代码

> https://github.com/junjun888/google-translater


------------------------------------------

调用如下：

```
public static void main(String[] args) {
		GoogleApi googleApi = new GoogleApi();
		/*GoogleApi googleApi = new GoogleApi("122.224.227.202", 3128);*/
		String result = googleApi.translate("Many applications within the enterprise domain ", "", "zh");
		System.out.println(result);
	}
```

输出：

```
企业领域内的许多应用程序
```

