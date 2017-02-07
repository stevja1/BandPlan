package org.jaredstevens.apps.bandplan;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jaredstevens.apps.bandplan.json.Repeater;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Search extends Activity implements LocationListener {
	private LocationManager geo = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void loadMap(View view) {
		Intent i = new Intent(getApplicationContext(), MapSearch.class);
		startActivity(i);
	}
	
	public void searchRepeaters(View view) {
		// Get geo location. Once we have that, the update event listener (onLocationChanged) will do the WS call.
		if(this.geo == null)
			this.geo = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		geo.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
		
		Button searchButton = (Button)findViewById(R.id.searchButton);
		searchButton.setClickable(false);
		searchButton.setEnabled(false);
		
		Button cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setClickable(true);
		cancelButton.setEnabled(true);
		
		final ListView repeaterList = (ListView) findViewById(R.id.repeaterList);
		repeaterList.setAdapter(null);
	}
	
	public void cancelSearch(View view) {
		if(this.geo != null) this.geo.removeUpdates(this);
		Button searchButton = (Button)findViewById(R.id.searchButton);
		searchButton.setClickable(true);
		searchButton.setEnabled(true);
		
		Button cancelButton = (Button)findViewById(R.id.cancelButton);
		cancelButton.setClickable(false);
		cancelButton.setEnabled(false);
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	@Override
	public void onProviderEnabled(String provider) {
	}
	
	@Override
	public void onProviderDisabled(String provider) {
	}
	
	@Override
	public void onLocationChanged(Location location) {
		float accuracy = location.getAccuracy();
		if(accuracy < 20) {
			double latitude = location.getLatitude() * 1000000;
			double longitude = location.getLongitude() * 1000000;
			EditText radiusInput = (EditText)findViewById(R.id.radiusInput);
			String radius = radiusInput.getText().toString();
			if(this.geo != null) this.geo.removeUpdates(this);
			LongRunningGetIO ws = new LongRunningGetIO(String.valueOf((int)latitude), String.valueOf((int)longitude), radius);
			ws.setContext(this);
			ws.execute();
		}
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {
		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}
	
	private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
		private String latitude;
		private String longitude;
		private String radius;
		private Context context;
		
		LongRunningGetIO( String latitude, String longitude, String radius) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.radius = radius;
		}
		
		public void setContext(Context inContext) {
			this.context = inContext;
		}
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			long length = entity.getContentLength();
			if(length < 0) length = 65536;
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n>0) {
				byte[] b = new byte[(int)length];
				n =  in.read(b);
				if (n>0) out.append(new String(b, 0, n));
			}
			return out.toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet("http://api.jaredstevens.org/BandPlanWS/conduit/findRepeaters/"+this.latitude+"/"+this.longitude+"/"+this.radius+"/1/50");
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}

		protected void onPostExecute(String results) {
			// Parse JSON and build list
			if (results!=null) {
//				EditText et = (EditText)findViewById(R.id.my_edit);
//				et.setText(results);
				JsonFactory json = new JsonFactory();
				JsonParser parser = null;
				ArrayList<Repeater> repeaters = new ArrayList<Repeater>();
				try {
					String fieldName;
					parser = json.createParser(results);
					Repeater repeater = null;
					JsonToken token = null;
					while((token = parser.nextToken()) != JsonToken.END_ARRAY) {
						// Starting to parse the array of Repeater objects
						if(token == JsonToken.START_ARRAY) {
							repeater = new Repeater();
							continue;
						}
						if(token == JsonToken.END_OBJECT) {
							if(repeater != null) repeaters.add(repeater);
							repeater = new Repeater();
							continue;
						}
						if(token == JsonToken.START_OBJECT) continue;
						fieldName = parser.getCurrentName();
						parser.nextToken();
						if(fieldName.equals("id")) repeater.setId(parser.getIntValue());
						else if(fieldName.equals("status")) repeater.setStatus(parser.getText().equals("ACTIVE")?Repeater.Status.ACTIVE:Repeater.Status.INACTIVE);
						else if(fieldName.equals("availability")) repeater.setAvailability(parser.getText().equals("OPEN")?Repeater.Availability.OPEN:Repeater.Availability.CLOSED);
						else if(fieldName.equals("band")) repeater.setBand(parser.getText());
						else if(fieldName.equals("input")) repeater.setInput(Float.parseFloat(parser.getText()));
						else if(fieldName.equals("output")) repeater.setOutput(Float.parseFloat(parser.getText()));
						else if(fieldName.equals("ctcssIn")) repeater.setCtcssIn(Float.parseFloat(parser.getText()));
						else if(fieldName.equals("ctcssOut")) repeater.setCtcssOut(Float.parseFloat(parser.getText()));
						else if(fieldName.equals("dcsCode")) repeater.setDcsCode(parser.getText());
						else if(fieldName.equals("callsign")) repeater.setCallsign(parser.getText());
						else if(fieldName.equals("sponsor")) repeater.setSponsor(parser.getText());
						else if(fieldName.equals("latitude")) repeater.setLatitude(Float.parseFloat(parser.getText()));
						else if(fieldName.equals("longitude")) repeater.setLongitude(Float.parseFloat(parser.getText()));
						else if(fieldName.equals("closed")) repeater.setClosed(parser.getBooleanValue());
					}
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final ArrayList<String> values = new ArrayList<String>();
				for(Repeater repeater : repeaters) {
					values.add(repeater.getBand()+" -- "+repeater.getOutput()+" / "+repeater.getInput());
				}
				StableArrayAdapter adapter = new StableArrayAdapter(this.context, android.R.layout.simple_list_item_1, values);
				final ListView repeaterList = (ListView) findViewById(R.id.repeaterList);
				repeaterList.setAdapter(adapter);
			}

			Button searchButton = (Button)findViewById(R.id.searchButton);
			searchButton.setClickable(true);
			searchButton.setEnabled(true);
			
			Button cancelButton = (Button)findViewById(R.id.cancelButton);
			cancelButton.setClickable(false);
			cancelButton.setEnabled(false);
		}
	}
}