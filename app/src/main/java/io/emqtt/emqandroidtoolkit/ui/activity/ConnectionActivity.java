package io.emqtt.emqandroidtoolkit.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.OnClick;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Connection;
import io.emqtt.emqandroidtoolkit.ui.base.ToolBarActivity;
import io.emqtt.emqandroidtoolkit.util.TipUtil;


public class ConnectionActivity extends ToolBarActivity {

    private static final String EXTRA_MODE = "mode";
    public static final String EXTRA_CONNECTION = "connection";

    public static final int MODE_ADD = 0;
    public static final int MODE_EDIT = 1;


    @BindView(R.id.linear_layout) LinearLayout mLinearLayout;
    @BindView(R.id.host) EditText mHost;
    @BindView(R.id.port) EditText mPort;
    @BindView(R.id.client_id) EditText mClientId;
    @BindView(R.id.clean_session) Switch mCleanSession;
    @BindView(R.id.username) EditText mUsername;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.operate_connection) Button mOperateConnectionButton;

    private int mMode;

    @IntDef({MODE_ADD, MODE_EDIT})
    public @interface mode {

    }

    public static void openActivity(Context context, @mode int mode) {
        Intent intent = new Intent(context, ConnectionActivity.class);
        intent.putExtra(EXTRA_MODE, mode);
        context.startActivity(intent);

    }

    public static void openActivityForResult(Context context, @mode int mode, int requestCode) {
        openActivityForResult(context, mode, requestCode, null);

    }

    public static void openActivityForResult(Context context, @mode int mode, int requestCode, Connection connection) {
        Intent intent = new Intent(context, ConnectionActivity.class);
        intent.putExtra(EXTRA_MODE, mode);
        if (null != connection) {
            intent.putExtra(EXTRA_CONNECTION, connection);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_connection;
    }

    @Override
    protected void setUpView() {

        setTitle("Connection");

        mMode = getIntent().getIntExtra(EXTRA_MODE, MODE_ADD);
        if (isAddMode()) {
            mOperateConnectionButton.setText(R.string.add_connection);
        } else {
            mOperateConnectionButton.setText(R.string.save_connection);
            Connection connection = getIntent().getParcelableExtra(EXTRA_CONNECTION);
            setConnection(connection);

        }


    }



    @Override
    protected void setUpData() {

    }


    @OnClick(R.id.operate_connection)
    public void onClick() {
        if (mHost.getText().toString().isEmpty()) {
            TipUtil.showSnackbar(mLinearLayout, "Host cannot be empty");
            return;
        }
        if (mClientId.getText().toString().isEmpty()) {
            TipUtil.showSnackbar(mLinearLayout, "Client Id cannot be empty");
            return;
        }

        if (mPort.getText().toString().isEmpty()) {
            TipUtil.showSnackbar(mLinearLayout, "Port cannot be empty");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CONNECTION, getConnection());
        setResult(RESULT_OK, intent);
        finish();

    }

    private void setConnection(Connection connection) {
        mHost.setText(connection.getHost());
        mPort.setText(connection.getPort());
        mClientId.setText(connection.getClintId());
        mCleanSession.setChecked(connection.isCleanSession());
        mUsername.setText(connection.getUsername());
        mPassword.setText(connection.getPassword());
    }

    private Connection getConnection() {
        String host = mHost.getText().toString().trim();
        String port = mPort.getText().toString().trim();
        String clientId = mClientId.getText().toString().trim();
        boolean cleanSession = mCleanSession.isChecked();
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        Connection connection = new Connection(host, port, clientId, cleanSession);
        connection.setUsername(username);
        connection.setPassword(password);
        return connection;
    }


    private boolean isAddMode() {
        return mMode == MODE_ADD;
    }

}