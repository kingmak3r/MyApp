package arr.myapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;

public class MyMethods {
	static MyDatabase mydb;
	
	public static Activity activity;
	static Intent intent=new Intent();
	public static void ShowAlert(Activity act,String title,String msg,Boolean finishOnOK,Boolean ok){
		MyMethods.activity=act;
		AlertDialog.Builder d = new AlertDialog.Builder(act);
		d.setCancelable(false);
		d.setTitle(title);
		if(ok){
			d.setIcon(R.drawable.check_white_48);
		}else{
			d.setIcon(R.drawable.stop_32);
		}
		d.setMessage(msg);
		if(finishOnOK){
			d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MyMethods.activity.finish();
				}
			});
		}else{
			d.setPositiveButton("OK",null);
		}

		d.show();
	}
	
	public static void SendMail(Context con,CharSequence title,CharSequence body){
		intent.putExtra(Intent.EXTRA_SUBJECT,title);
		intent.putExtra(Intent.EXTRA_TEXT,body);
		intent.setType("message/rfc822");
		con.startActivity(Intent.createChooser(intent,"Send mail using"));
	}
	
	public static boolean DeleteItemFromListView(ListAdapter ListAdapt,AdapterContextMenuInfo itemview){
		NewsAdapter news_adapt=(NewsAdapter) ListAdapt;
		Log.d("ARR","DB Item ID :" +news_adapt.getItem(itemview.position)._id+" and View ID "+news_adapt.getItem(itemview.position));
		if(MyMethods.RemoveFromLatestNewsDB(news_adapt.getItem(itemview.position)._id)){
			news_adapt.remove(news_adapt.getItem(itemview.position));
			news_adapt.notifyDataSetChanged();
			return true;
		}else{
			return false;
		}
		
		
	}
	
	public static boolean RemoveFromLatestNewsDB(int id){
	
	SQLiteDatabase sql=LatestNews.mydb.getWritableDatabase();
	String[] args={""+id};
	int affected_rows=sql.delete(MyDatabase.TAB_NEWS,MyDatabase.FLD_ID+"=?",args);
	sql.close();
	if(affected_rows>0){
		return true;
	}else{
		return false;
	}
	
		
	}
	
	public static boolean AddToFav(int id,SQLiteDatabase db){
		db.rawQuery("UPDATE "+MyDatabase.TAB_NEWS+" SET fav=1 WHERE _id="+id, null);
		Log.d("ARR","ID:"+id);
		db.close();
		return true;
	}

	public static String getIndianDateTime(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dt;
		try {
			dt = dateFormat.parse(date);
			return dateFormat.format(dt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
        
}
public static String getCurrentDateTime(){
	SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    Date date = new Date();
    return dateFormat.format(date);
}

public static void setLanguage(Context con,String lang){
	Locale loc = new Locale(lang);
	Locale.setDefault(loc);
	Configuration conf =new Configuration();
	conf.locale=loc;
	con.getResources().updateConfiguration(conf, con.getResources().getDisplayMetrics());
}
}

