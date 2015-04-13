package com.cjj.cartoon.dao;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;
import android.util.Log;

import com.cjj.cartoon.constants.Constant;
import com.cjj.cartoon.model.HeadViewDataModel;
import com.cjj.cartoon.model.ImageAndTitle;
import com.cjj.volley.utils.LogUtil;

public class YinhunDao {
	/** 私有类对象 */
	private static YinhunDao instance;

	/** 单例模式 */
	public static YinhunDao getInstance() {
		if (instance == null) {
			instance = new YinhunDao();
		}
		return instance;
	}
	
	/**
	 * 获取银魂所有列表数据
	 * @param URL
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ImageAndTitle> getComicListData(String URL)throws Exception
	{
		int i = 0;
		ArrayList<ImageAndTitle> list_data = new ArrayList<ImageAndTitle>();
		Document doc = Jsoup.connect(URL).timeout(10*1000).get();
			Elements elements = doc.select("div.col-xs-6");
			for(Element element:elements)
			{
				
				i++;
				
				Document eleDoc = Jsoup.parseBodyFragment(element.html());
				if(i<=elements.size()-2)
				{
					LogUtil.LogMsg_I("i="+i);
					String link = eleDoc.select("a[href]").first().attr("href");
					String imageUrl = eleDoc.select("img[src]").first().attr("src");
					String title = eleDoc.select("a[title]").first().attr("title");
					if(!TextUtils.isEmpty(link)&&!TextUtils.isEmpty(imageUrl)&&!TextUtils.isEmpty(title))
					{
						ImageAndTitle model = new ImageAndTitle();
						model.link = link;
						model.imageUrl = imageUrl;
						model.title = title;
						list_data.add(model);
						LogUtil.LogMsg_I(link+"");
						LogUtil.LogMsg_I(imageUrl+"");
						LogUtil.LogMsg_I(title+"");
						LogUtil.LogMsg_I(list_data.size()+"");
					}
				}else{
					return list_data;
				}
				
				
			}
		return list_data;
	
	}
	
	/**
	 * 获得图片资源
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getDetailComicImage(String url)throws Exception
	{
		ArrayList<String> allImages = new ArrayList<String>();
		Document doc = Jsoup.connect(url).timeout(10 * 1000).get();
		Elements elements = doc.getElementsByClass("article-content");
		for (Element element : elements) {
			Elements es = element.getElementsByTag("img");
			for (Element e : es) {
				String a = e.attr("src");
				if (a != null && a.length() > 0) {
					allImages.add(a);
				}
			}
		}
		return allImages;
	}
	
	
	/**
	 * 获取头部信息
	 * @return
	 */
	public ArrayList<HeadViewDataModel> getHeadViewDataFromNet()
			throws Exception {
		ArrayList<HeadViewDataModel> list_models = new ArrayList<HeadViewDataModel>();
		Document doc = Jsoup.connect(Constant.HEADVIEW_URL).timeout(10 * 1000)
				.get();
		Elements elements = doc.select("div#slide");
		for (Element element : elements) {
			Document eleDoc = Jsoup.parseBodyFragment(element.html());
			for (int i = 1; i <= 5; i++) {
				HeadViewDataModel model = new HeadViewDataModel();
				Elements elements2 = eleDoc.select("div#slide" + i);
				for (Element element2 : elements2) {
					
					String temUrl = element2.attr("style");
					model.url = temUrl.substring(21, temUrl.length()-2);
					Log.i("cjj", "cext jj t>>>>>>>>>3" +model.url );
					for (Element element3 : element2.select("a[title]")) {
						model.link = element3.attr("href");
						model.title = element3.attr("title")
								.replace("<br>", "").trim();
						Log.i("cjj",
								"cext jj t>>>>>>>>>4"
										+ element3.attr("title")
												.replace("<br>", "\n").trim());
						Log.i("cjj",
								"cext jj t>>>>>>>>>5" + element3.attr("href"));
					}
				}
				list_models.add(model);
			}
		}
		return list_models;

	}
}







