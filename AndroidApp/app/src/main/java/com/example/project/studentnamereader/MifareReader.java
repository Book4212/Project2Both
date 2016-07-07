package com.example.project.studentnamereader;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.widget.Toast;

public class MifareReader extends Activity {

    private static NfcAdapter mNfcAdapter;

    public static void setNfcAdapter(NfcAdapter nfcAdapter) {
        mNfcAdapter = nfcAdapter;
    }

    public static NfcAdapter getNfcAdapter() {
        return mNfcAdapter;
    }

    public static String readNewTag(Intent intent, Context context) {
        // Check if Intent has a NFC Tag.
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            //Log.d("print", "Coming !!!!!");
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            // Show Toast message with UID.
            String uid = byte2HexString(tag.getId());
            //Toast.makeText(context, "UID : " + uid, Toast.LENGTH_LONG).show();
            //Log.d("print", "UID : " + uid);
            return uid;
        }
        return "0x";
    }


    public static String byte2HexString(byte[] bytes) {
        String ret = "";
        if (bytes != null) {
            for (Byte b : bytes) {
                ret += String.format("%02X", b.intValue() & 0xFF);
            }
        }
        return ret;
    }

    public static void disableNfc(Activity targetActivity) {
        //Log.d("print", "function disableNfcForegroundDispatch in Common.java");
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            mNfcAdapter.disableForegroundDispatch(targetActivity);
            //Log.d("print", "function disableNfc in MifareReader.java");
        }
    }

    public static void enableNfc(Activity targetActivity) {
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {

            Intent intent = new Intent(targetActivity,
                    targetActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(targetActivity, 0, intent, 0);
            mNfcAdapter.enableForegroundDispatch(
                    targetActivity, pendingIntent, null, new String[][]{new String[]{NfcA.class.getName()}});
            //Log.d("print", "function enableNfc in MifareReader.java");
        }
    }

}
