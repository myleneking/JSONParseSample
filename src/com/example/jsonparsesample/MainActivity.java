package com.example.jsonparsesample;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String mName, mCode, mPrice, mPercentChange, mVolume, mAsOf;
	private ListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
		String jString = JSONParser.getJSONFromUrl(Util.API_PSE_ALL);

		try {
			JSONObject jObject = new JSONObject(jString);

			String asOf = jObject.getString("as_of");
			String date = asOf.substring(0, 10);
			String time = asOf.substring(11, 19);
			mAsOf = date + " " + time;

			JSONArray stockArr = jObject.getJSONArray("stock");

			for (int i = 0; i < stockArr.length(); i++) {
				JSONObject stock = stockArr.getJSONObject(i);
				mName = stock.getString("name");
				mCode = stock.getString("symbol");
				mPercentChange = stock.getString("percent_change");
				mVolume = stock.getString("volume");

				JSONObject price = stock.getJSONObject("price");
				String currency = price.getString("currency");
				String amount = price.getString("amount");
				mPrice = currency + " " + amount;

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", mName);
				map.put("code", mCode);
				map.put("percentChange", mPercentChange);
				map.put("price", mPrice);
				map.put("volume", mVolume);
				contactList.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		mAdapter = new SimpleAdapter(this, contactList, R.layout.row_item,
				new String[] { "name", "code", "percentChange", "price",
						"volume" }, new int[] { R.id.tvName, R.id.tvCode,
						R.id.tvPercentChange, R.id.tvPrice, R.id.tvVolume });

		initialize();

	}

	private void initialize() {
		TextView asOf = (TextView) findViewById(R.id.tvAsOf);
		asOf.setText(mAsOf);

		final ListView listview = (ListView) findViewById(R.id.listView);
		listview.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
