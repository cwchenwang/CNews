package wangchen.java.com.cnews;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import wangchen.java.com.cnews.db.NewsDBHelper;

public class LoginActivity extends AppCompatActivity {

  public static final int LOGIN_SUCCESS = 1;
  public static final int LOGIN_FAIL = 2;

  EditText emailText;
  EditText passwordText;
  Button loginButton;
  TextView signupLink;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    TextInputLayout layout1 = findViewById(R.id.user_layout);
    TextInputLayout layout2 = findViewById(R.id.pw_layout);
    emailText = layout1.getEditText();
    passwordText = layout2.getEditText();

    loginButton = findViewById(R.id.btn_login);
    signupLink = findViewById(R.id.link_signup);

    loginButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        loginButtonClicked();
      }
    });

    signupLink.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        //startActivityForResult(intent, REQUEST_SIGNUP);
        startActivity(intent);
        finish();
        //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
      }
    });
  }

  private void loginButtonClicked() {
    String email = emailText.getText().toString();
    String password = passwordText.getText().toString();
    if(validate(email, password)) {
      NewsDBHelper db = ((CNewsApp)getApplication()).getDB();
      int res = db.checkPassword(email, password);
      if(res == NewsDBHelper.LOGIN_SUCCESS) {
        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("USERNAME", email);
        setResult(LOGIN_SUCCESS, intent);
        finish();

      } else if(res == NewsDBHelper.PASSWORD_ERROR) {
        Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
      } else if(res == NewsDBHelper.USER_NOT_FOUND) {
        Toast.makeText(getApplicationContext(), "没有找到相应的用户", Toast.LENGTH_SHORT).show();
      }
    }
  }

  public boolean validate(String email, String password) {
    boolean valid = true;

    if (email.isEmpty() || email.length() < 4 || email.length() > 10) {
      emailText.setError("用户名需在4到10位");
      valid = false;
    } else {
      emailText.setError(null);
    }
    if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
      passwordText.setError("密码应在4到10位");
      valid = false;
    } else {
      passwordText.setError(null);
    }
    return valid;
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent();
    setResult(LOGIN_FAIL, intent);
    super.onBackPressed();
  }
}
