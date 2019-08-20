package org.techtown.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    public static final String TAG = "SmsReceiver";

    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onReceive(Context context, Intent intent) { //Intent 객체 안에 SMS 데이터가 들어있음
        Log.i(TAG, "onReceive() 메서드 호출됨");

        Bundle bundle = intent.getExtras(); //인텐트에서 Bundle 객체 가져오기
        SmsMessage[] messages = parseSmsMessage(bundle); // parseSmsMessagge () 메서드 호출하기

        if (messages != null && messages.length > 0) {
            String sender = messages[0].getOriginatingAddress(); //발신자 번호 확인하려면
            Log.i(TAG, "SMS sender:" + sender);

            String contents = messages[0].getMessageBody().toString(); //문자 내용을 확인하려면
            Log.i(TAG, "SMS contents:" + contents);

            Date receivedDate = new Date(messages[0].getTimestampMillis()); //SMS 받은 시각도 확인
            Log.i(TAG, "SMS received date:" + receivedDate.toString());

            sendToActivity(context, sender, contents, receivedDate); //새로운 화면을 띄우기 위한 메서드 호출
        }
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle) {

        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length]; //Bundle 객체에 들어가 있는 부가 데이터 중에 pdus 가져오기

        int smsCount = objs.length;
        for (int i = 0; i < smsCount; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //단말기에 OS 버전을 확일할때 사용
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]); //단말 OS 버전에 따라 다른 방식으로 메서드 호출하기
            }
        }

        return messages;
    }

    private void sendToActivity(Context context, String sender, String contents, Date receivedDate){
        Intent myIntent = new Intent(context, SmsActivity.class);

        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // 인텐트에 플래그 추가하기

        myIntent.putExtra("sender",sender);
        myIntent.putExtra("contents", contents);
        myIntent.putExtra("receivedDate", format.format(receivedDate));
    }
}