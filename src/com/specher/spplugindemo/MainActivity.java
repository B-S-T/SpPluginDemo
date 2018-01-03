package com.specher.spplugindemo;

/**
 * �˳����Ϊ��ʾSDK���ܣ�����ʵ����;
 * 2017��12��26�� Specher
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import com.specher.qqrobotsdk.QQRobot;
import com.specher.qqrobotsdk.data.MessageRecord;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	QQRobot qqrobot;
	TextView tv;
	ScrollView scrollView;
	String init=null;
	Handler handler = new Handler();
	//ʵ����һ���㲥������
	BroadcastReceiver rec= new BroadcastReceiver() {
		//����Ϊ�˷������������ڲ��࣬��Ҳ�����ö������ⲿ��������
		@Override
		public void onReceive(Context context, Intent intent) {
		// TODO ���յ���Ϣ��ע��Ҳ���յ��Լ����͵���Ϣ������ʱ������Լ���QQ
		int cmd = 	intent.getIntExtra(QQRobot.CMD,0);
		String qq  = intent.getStringExtra(QQRobot.ACTION_QQ);
		String group="", member="",msg="";
		
		//����CMD������Ϣ
		switch(cmd){
		case QQRobot.CMD_GET_GROUP_MEMBER_NICKNAME://�õ�Ⱥ�ǳƣ�������Ĳ���һ�����Թ���
		case QQRobot.CMD_REC_GROUP_TXTMSG :
		group =	intent.getStringExtra(QQRobot.DEAL_UIN);
		member = intent.getStringExtra(QQRobot.DEAL_UIN2);
		//����ֱ�Ӷ�ȡ��Ϣ�ı�����
		msg = intent.getStringExtra(QQRobot.DEAL_STR);
		
		//Ҳͨ�����ݹ�����Parcel������Խ�����Ϣ����ϸ��Ϣ
		 if(intent.hasExtra(QQRobot.ParcelObj)){
		 MessageRecord messageRecord1  =  (MessageRecord) intent.getSerializableExtra(QQRobot.ParcelObj);
		//ͨ��msgtype�����ж���Ϣ���͵ĸ�����Ϣ,�����Ⱥ��Ϣ
		 msg = messageRecord1.toString();
		 }
			break;
		case QQRobot.CMD_REC_FRIEND_TXTMSG:	 
			member = intent.getStringExtra(QQRobot.DEAL_UIN2);
			//����ֱ�Ӷ�ȡ��Ϣ�ı�����
			msg = intent.getStringExtra(QQRobot.DEAL_STR);
			//Ҳͨ�����ݹ�����Parcel������Խ�����Ϣ����ϸ��Ϣ
			if(intent.hasExtra(QQRobot.ParcelObj)){
			 MessageRecord messageRecord  = (MessageRecord) intent.getSerializableExtra(QQRobot.ParcelObj);
			 //ͨ��msgtype�����ж���Ϣ���͵ĸ�����Ϣ,�����Ⱥ��Ϣ
			//if( messageRecord.msgtype == messageRecord.MSG_TYPE_TROOP_TIPS_ADD_MEMBER);
			 msg = messageRecord.toString();
			 }
		break;
		case QQRobot.CMD_GET_CURRENT_NICKNAME:
			msg = intent.getStringExtra(QQRobot.DEAL_STR);
			break;
		case QQRobot.CMD_GET_CURRENT_QQ:
			msg = intent.getStringExtra(QQRobot.DEAL_STR);
			break;
		case QQRobot.CMD_INIT:
			
			init = intent.getStringExtra(QQRobot.DEAL_STR);
			msg = init;
			break;
		}
		
		//����UI�����Handler������ֻΪ����ʾ�Ͳ�д��
		tv.setText(tv.getText() +"\n"
		+(
		(getTimeText())
		+(group==""?"": group+ "|") 
		+ (member==""?"":member + "|") 
		+  ( msg==""?"":msg)
		)
		);//��ʾ���յ�����Ϣ
		
		//ͨ��handler�����߳��и���
		handler.post(new Runnable() {  
		    @Override  
		    public void run() {  	
		    	//�������ײ�
		        scrollView.fullScroll(ScrollView.FOCUS_DOWN);  
		    }  
		}); 
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.textView1);
		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		
		//ʵ����һ��QQ�����˶���
		qqrobot = new QQRobot(this,rec);
		
		//��ʼ��QQ�����˲��
		qqrobot.initQQRobot();
		
		//����Ƿ��ʼ���ɹ�
		 new Handler().postDelayed(new Runnable(){
             public void run() {
            	 if(init==null){ 
            		 tv.setText("��ʼ�����ʧ�ܣ�");
            	 }
             }
		 }, 2000);//�ӳ�2��ִ��
	
	}

	public static String getTimeText(){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");  
		Date date =  new Date();
		return "["+format.format(date)+"]";   
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//ȡ�ǳư�ť�����
	public void onGetNickClick(View v){
		qqrobot.getCurrentNickName();
	}
	//ȡQQ��ť�����
	public void onGetQQClick(View v){
		qqrobot.getCurrentQQ();
	}
	//���԰�ť�����
	public void onGagTestClick(View v){
		//����һ���Ի���
		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("���Բ���");
        View dlgview= getLayoutInflater().inflate(R.layout.testlayout, null);
        builder2.setView(dlgview);
        final AlertDialog alertDialog =  builder2.create();
        final EditText text1 = (EditText) dlgview.findViewById(R.id.editText1);
        final EditText text2 = (EditText) dlgview.findViewById(R.id.editText2);
        text1.setHint("Ⱥ��+�ո�+QQ�� ����123456 10000");
        text2.setHint("����ʱ�䣨�룩");
        final CheckBox cb1=(CheckBox) dlgview.findViewById(R.id.checkBox1);
        cb1.setVisibility(View.GONE);
        Button btn1 = (Button) dlgview.findViewById(R.id.button1);
        Button btn2 = (Button) dlgview.findViewById(R.id.button2);
        btn1.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			String[] a = 	text1.getText().toString().split(" ");
			if(a.length==2){
				qqrobot.doGagMember(a[0], a[1], text2.getText().toString());
			}else{
				Toast.makeText(MainActivity.this, "QQ��ʽ����",Toast.LENGTH_SHORT).show();
			}
					
			}
		});
        btn2.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				alertDialog.dismiss();
			}
		});
      
        
       alertDialog.show();
	}
	
	
	//����Ϣ��ť�����
	public void onMsgTestClick(View v){
		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("��Ϣ����");
        View dlgview= getLayoutInflater().inflate(R.layout.testlayout, null);
        builder2.setView(dlgview);
        final AlertDialog alertDialog =  builder2.create();
        final EditText text1 = (EditText) dlgview.findViewById(R.id.editText1);
        final EditText text2 = (EditText) dlgview.findViewById(R.id.editText2);
        final CheckBox cb1=(CheckBox) dlgview.findViewById(R.id.checkBox1);
        Button btn1 = (Button) dlgview.findViewById(R.id.button1);
        Button btn2 = (Button) dlgview.findViewById(R.id.button2);
        btn1.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				if(!cb1.isChecked()){
					qqrobot.sendFriendTxt(QQRobot.DEFAULT_ACTION_QQ, text1.getText().toString(), text2.getText().toString());
				}else{
					qqrobot.sendGroupTxt(QQRobot.DEFAULT_ACTION_QQ, text1.getText().toString(), text2.getText().toString());
				}		
			}
		});
        
        btn2.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				alertDialog.dismiss();
			}
		});
      
        
       alertDialog.show();
	}
	
	
	//����Ϣ�����ر����
	public void onAtMsgClick(View v){
		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("���ز���");
        View dlgview= getLayoutInflater().inflate(R.layout.testlayout2, null);
        builder2.setView(dlgview);
        final AlertDialog alertDialog =  builder2.create();
        final EditText text1 = (EditText) dlgview.findViewById(R.id.editText1);
        final EditText text2 = (EditText) dlgview.findViewById(R.id.editText2);
        final EditText text3 = (EditText) dlgview.findViewById(R.id.editText3);
        Button btn1 = (Button) dlgview.findViewById(R.id.button1);
        Button btn2 = (Button) dlgview.findViewById(R.id.button2);
        btn1.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				
					qqrobot.sendGroupTxtWithAT(QQRobot.DEFAULT_ACTION_QQ, text1.getText().toString(), text2.getText().toString(),text3.getText().toString());
			
			}
		});
        
        btn2.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				alertDialog.dismiss();
			}
		});
      
        
       alertDialog.show();
	}
	
	// ȡȺ��Ա�ǳƱ�����
	public void onGetMemberNickClick(View v){

		//����һ���Ի���
		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("ȡȺ��Ա�ǳƲ���");
        View dlgview= getLayoutInflater().inflate(R.layout.testlayout, null);
        builder2.setView(dlgview);
        final AlertDialog alertDialog =  builder2.create();
        final EditText text1 = (EditText) dlgview.findViewById(R.id.editText1);
        final EditText text2 = (EditText) dlgview.findViewById(R.id.editText2);
        text1.setHint("Ⱥ��");
        text2.setHint("Ⱥ��ԱQQ��");
        final CheckBox cb1=(CheckBox) dlgview.findViewById(R.id.checkBox1);
        cb1.setVisibility(View.GONE);
        Button btn1 = (Button) dlgview.findViewById(R.id.button1);
        Button btn2 = (Button) dlgview.findViewById(R.id.button2);
        btn1.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {

				qqrobot.getGroupMemberNickName(text1.getText().toString(),text2.getText().toString());
			
					
			}
		});
        btn2.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				alertDialog.dismiss();
			}
		});
      
        
       alertDialog.show();
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_exit) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onDestroy() {
	//ȡ��ע��㲥���������ǵ��ڽ���رյ�ʱ��ע���㲥����Ȼ���ж���㲥�����ߴ��ڣ�����Ҳ�ᱨ��
		this.unregisterReceiver(rec);
		super.onDestroy();
	}
	
	
}
