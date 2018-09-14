package wangchen.java.com.cnews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SignupActivity extends AppCompatActivity {

//  @BindView(R.id.input_name)
  EditText nameText;
  EditText addressText;
  EditText usernameText;
  EditText passwordText;
  EditText reEnterPasswordText;
  Button signupButton;
  TextView loginLink;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    nameText = ((TextInputLayout)findViewById(R.id.input_name)).getEditText();
    addressText = ((TextInputLayout)findViewById(R.id.input_address)).getEditText();
    usernameText = ((TextInputLayout)findViewById(R.id.username_signup)).getEditText();
    passwordText = ((TextInputLayout)findViewById(R.id.password_signup)).getEditText();
    reEnterPasswordText = ((TextInputLayout)findViewById(R.id.repassword_signup)).getEditText();
    signupButton = findViewById(R.id.btn_signup);
    loginLink = findViewById(R.id.link_login);
    signupButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        signup();
      }
    });

    loginLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Finish the registration screen and return to the Login activity
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
      }
    });
  }

  private void signup() {
    if(!validate()) {
      Toast.makeText(getBaseContext(), "注册失败", Toast.LENGTH_LONG).show();
      return;
    }
    final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
            R.style.AppTheme_Dark_Dialog);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("正在创建账号...");
    progressDialog.show();

    String name = nameText.getText().toString();
    String address = addressText.getText().toString();
    final String username = usernameText.getText().toString();
    final String password = passwordText.getText().toString();
    final String ipAdress = "101.5.121.239";
    new Thread() {
      public void run() {
        try {
          Socket s1 = new Socket(ipAdress, 6666);
          OutputStream os = s1.getOutputStream();
          DataOutputStream dos = new DataOutputStream(os);
          dos.writeUTF("Register" + " " + username + " " + password);
//      new Handler().postDelayed(new Runnable() {
//        @Override
//        public void run() {
//
//        }
//      }, 1000);
          InputStream is = s1.getInputStream();
          DataInputStream dis = new DataInputStream(is);
          String getStr = dis.readUTF();
          if(getStr.equals("USEREXISTS")) {
            SignupActivity.this.runOnUiThread(new Runnable()
            {
              public void run()
              {
                Toast.makeText(getApplicationContext(), "用户名已经存在", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
              }
            });
            return;
          } else if(getStr.equals("REGISTERSUCCESS")) {
            SignupActivity.this.runOnUiThread(new Runnable() {
              public void run() {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
              }
            });
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return;
          }
          dis.close();
          dos.close();
          s1.close();

        } catch(IOException e) {
          Toast.makeText(getApplicationContext(), "登录失败 ", Toast.LENGTH_SHORT).show();
        }
      }
    }.start();
//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//        NewsDBHelper db = ((CNewsApp)getApplicationContext()).getDB();
//        int res = db.createUser(username, password);
//        if(res == NewsDBHelper.USER_EXITS) {
//          SignupActivity.this.runOnUiThread(new Runnable()
//          {
//            public void run()
//            {
//              Toast.makeText(getApplicationContext(), "用户名已被占用", Toast.LENGTH_SHORT).show();
//              progressDialog.dismiss();
//            }
//          });
//          return;
//        } else {
//          SignupActivity.this.runOnUiThread(new Runnable()
//          {
//            public void run()
//            {
//              Toast.makeText(getApplicationContext(), "创建账号成功", Toast.LENGTH_SHORT).show();
//              progressDialog.dismiss();
//            }
//          });
//          startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//          return;
//        }
//      }
//    }) {
//    }.start();
  }

  public boolean validate() {
    boolean valid = true;

    String name = nameText.getText().toString();
    String address = addressText.getText().toString();
    String username = usernameText.getText().toString();
    String password = passwordText.getText().toString();
    String reEnterPassword = reEnterPasswordText.getText().toString();

    if (name.isEmpty() || name.length() < 2) {
      nameText.setError("名字最少两个字符");
      valid = false;
    } else {
      nameText.setError(null);
    }

    if (address.isEmpty()) {
      addressText.setError("地址不能为空");
      valid = false;
    } else {
      addressText.setError(null);
    }


    if (username.isEmpty() || username.length() < 4 || username.length() > 10) {
      usernameText.setError("请输入4到10位的用户名");
      valid = false;
    } else {
      usernameText.setError(null);
    }

    if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
      passwordText.setError("密码应为4-10位");
      valid = false;
    } else {
      passwordText.setError(null);
    }

    if (!(reEnterPassword.equals(password))) {
      reEnterPasswordText.setError("两次输入的密码不一致");
      valid = false;
    } else {
      reEnterPasswordText.setError(null);
    }

    return valid;
  }
}
