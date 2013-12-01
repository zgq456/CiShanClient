package org.crazyit.gongyi.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crazyit.auction.client.util.DialogUtil;
import org.crazyit.auction.client.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class SeekListAct extends Activity {
	protected static final String ACTIVITY_TAG = "MyAndroid";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ListView listview = (ListView) findViewById(R.id.lv);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.listview_item, new String[] { "img", "name", "logo",
						"place", "donation", "donation", "jindu", "still_need",
						"riqi", "num", "file" }, new int[] { R.id.iv,
						R.id.name, R.id.logo, R.id.diqu, R.id.renshu,
						R.id.juankuan, R.id.jindu, R.id.still_need, R.id.riqi,
						R.id._id, R.id.file });

		listview.setAdapter(adapter);
		// setListAdapter(adapter);

		adapter.setViewBinder(new ViewBinder() {

			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				// 判断是否为我们要处理的对象
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView iv = (ImageView) view;

					iv.setImageBitmap((Bitmap) data);
					return true;
				} else
					return false;
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView tv1 = (TextView) arg1.findViewById(R.id._id);
				int _id = Integer.valueOf(tv1.getText().toString());

				TextView tv2 = (TextView) arg1.findViewById(R.id.jindu);
				String jindu = tv2.getText().toString();

				TextView tv3 = (TextView) arg1.findViewById(R.id.renshu);
				int donation = Integer.valueOf(tv3.getText().toString());

				TextView tv4 = (TextView) arg1.findViewById(R.id.file);
				String file = tv4.getText().toString();
				/*
				 * TextView tv5=(TextView)arg1.findViewById(R.id.diqu); String
				 * diqu=String.valueOf(tv5.getText().toString());
				 * 
				 * TextView tv6=(TextView)arg1.findViewById(R.id.renshu); String
				 * renshu=String.valueOf(tv6.getText().toString());
				 * 
				 * TextView tv7=(TextView)arg1.findViewById(R.id.jindu); String
				 * jindu=String.valueOf(tv7.getText().toString());
				 */

				Intent intent = new Intent(SeekListAct.this, null);
				intent.putExtra("_id", _id);
				intent.putExtra("jindu", jindu);
				intent.putExtra("donation", donation);
				intent.putExtra("file", file);
				// intent.putExtra("logo", logo);
				// intent.putExtra("name", name);
				// intent.putExtra("diqu", diqu);
				// intent.putExtra("renshu", renshu);
				// intent.putExtra("jindu", jindu);

				startActivity(intent);
			}
		});
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String action = "viewSeekList.jsp";
		String url = HttpUtil.BASE_URL + action;
		try {
			// 向指定URL发送请求，并把服务器响应转换成JSONArray对象
			JSONArray jsonArray = new JSONArray(HttpUtil.getRequest(url));
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				int num = 8888;
				Picture p = new Picture();
				String name = jsonObj.getString("seekerName");
				String logo = jsonObj.getString("title");
				String place = jsonObj.getString("seekerProv")
						+ jsonObj.getString("seekerZone");
				int donation = jsonObj.getInt("helpAmount");
				int need = jsonObj.getInt("needAmount");

				int still_need = need - donation;

				DecimalFormat df = new DecimalFormat("0.00");
				float f = Float.valueOf(df.format((float) donation
						/ (float) need));
				String jindu = String.valueOf((int) (f * 100)) + "%";

				String riqi = jsonObj.getString("createDate");

				String file = "file";

				map.put("num", num);
				map.put("img",
						getBitmap(HttpUtil.BASE_URL0
								+ jsonObj.getString("photo")));
				map.put("name", name);
				map.put("logo", logo);
				map.put("place", place);
				map.put("donation", donation);
				map.put("still_need", still_need);
				map.put("riqi", riqi);
				map.put("jindu", jindu);
				map.put("file", file);

				list.add(map);

				Log.i(SeekListAct.ACTIVITY_TAG, name);
			}
		} catch (Exception e) {
			DialogUtil.showDialog(this, "服务器响应异常，请稍后再试！", false);
			Log.e(SeekListAct.ACTIVITY_TAG, e.getMessage());
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public Bitmap getBitmap(String imageUrl) {
		Bitmap mBitmap = null;
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream is = conn.getInputStream();
			mBitmap = BitmapFactory.decodeStream(is);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mBitmap;
	}

}