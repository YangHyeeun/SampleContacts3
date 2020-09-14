package org.techtown.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseContacts();
            }
        });
    }

    public void chooseContacts() {
        /* 인텐트 객체 생성
        ** 1. Intent.ACTION_PICK이라는 액션 정보 전달
        ** 2. ContactsContract.Contacts.CONTENT_URI => 연락처 정보를 조회하는 데 사용되는 URI 값
         */
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(contactPickerIntent, 101); // 연락처를 선택할 수 있는 화면이 표시됨
    }

    // startActivityForResult() 메서드가 실행되어 사용자가 연락처를 하나 선택하면 onActivityForResult() 메서드가 자동 호출됨
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                try {
                    // 파라미터로 전달된 인텐트 객체의 getData() 메서드 호출하면 선택된 연락처 정보 가리키는 Uri 객체가 반환됨
                    Uri contactsUri = data.getData();
                    String id = contactsUri.getLastPathSegment(); // 선택한 연락처의 id 값 확인

                    getContacts(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getContacts(String id) {
        Cursor cursor = null;
        String name = "";

        try {
            /*
            ** 1. ContactsContract.Data.CONTENT_URI => 연락처의 상세 정보를 조회하는 데 사용되는 Uri
            ** 2. 
             */
            cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                                                null,
                                                ContactsContract.Data.CONTACT_ID + "=?",
                                                new String[] {id},
                                                null);

            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                println("Name : " + name);

                String columns[] = cursor.getColumnNames();
                for (String column : columns) {
                    int index = cursor.getColumnIndex(column);
                    String columnOutput = ('#' + index + " -> [" + column + "] " + cursor.getString(index));
                    println(columnOutput);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void println(String data) {
        textView.append(data + "\n");
    }

}