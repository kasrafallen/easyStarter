package ir.gooble.clinic.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import ir.gooble.clinic.activity.ClinicActivity;
import ir.gooble.clinic.activity.DoctorActivity;
import ir.gooble.clinic.activity.FactActivity;
import ir.gooble.clinic.activity.GalleryActivity;
import ir.gooble.clinic.activity.LaunchActivity;
import ir.gooble.clinic.activity.RegisterActivity;
import ir.gooble.clinic.activity.ReserveActivity;
import ir.gooble.clinic.init.InitClinic;
import ir.gooble.clinic.init.InitDescription;
import ir.gooble.clinic.init.InitDetail;
import ir.gooble.clinic.init.InitDoctor;
import ir.gooble.clinic.init.InitDrawer;
import ir.gooble.clinic.init.InitFact;
import ir.gooble.clinic.init.InitGallery;
import ir.gooble.clinic.init.InitMain;
import ir.gooble.clinic.init.InitRegister;
import ir.gooble.clinic.init.InitReserve;
import ir.gooble.clinic.instance.Attributes;
import ir.gooble.clinic.instance.DoctorInstance;
import ir.gooble.clinic.instance.RegistryInstance;
import ir.gooble.clinic.instance.UserInstance;
import ir.gooble.clinic.model.FactModel;
import ir.gooble.clinic.util.PromptUtil;
import ir.gooble.clinic.util.Util;
import ir.gooble.clinic.view.AppDrawerLayout;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {
    private static final String EXIT = "BaseActivity.EXIT";

    public AppDrawerLayout drawer;
    public PromptUtil prompt;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prompt = new PromptUtil(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(EXIT));
    }

    public BaseInit setContentView(Object object) {
        if (object != null && object instanceof BaseActivity) {
            BaseInit init = selectView(((BaseActivity) object).getClass().getSimpleName(), object);
            if (init != null) {
                super.setContentView(init.getView());
                return init;
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    private BaseInit selectView(String simpleName, Object object) {
        switch (simpleName) {
            case "MainActivity":
                return new InitMain((BaseActivity) object);
            case "ClinicActivity":
                return new InitClinic((BaseActivity) object);
            case "GalleryActivity":
                return new InitGallery((BaseActivity) object);
            case "DoctorActivity":
                return new InitDoctor((BaseActivity) object);
            case "DetailActivity":
                return new InitDetail((BaseActivity) object);
            case "FactActivity":
                return new InitFact((BaseActivity) object);
            case "DescriptionActivity":
                return new InitDescription((BaseActivity) object);
            case "RegisterActivity":
                return new InitRegister((BaseActivity) object);
            case "ReserveActivity":
                return new InitReserve((BaseActivity) object);
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void openDrawer() {
        if (drawer != null && !drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.openDrawer(Gravity.RIGHT);
        }
    }

    public void run(String tag, BaseActivity context, View view, String data) {
        Intent intent;
        switch (tag) {
            case Attributes.FIELD_ABOUT_CLINIC:
                if (context instanceof ClinicActivity) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    return;
                }
                intent = new Intent(context, ClinicActivity.class);
                start(intent, view, data);
                break;
            case Attributes.FIELD_GALLERY:
                if (context instanceof GalleryActivity) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    return;
                }
                intent = new Intent(context, GalleryActivity.class);
                start(intent, view, data);
                break;
            case Attributes.FIELD_ABOUT_DOCTORS:
                if (context instanceof DoctorActivity) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    return;
                }
                intent = new Intent(context, DoctorActivity.class);
                start(intent, view, data);
                break;
            case Attributes.FIELD_NEW_FACTS:
                if (context instanceof FactActivity) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    return;
                }
                intent = new Intent(context, FactActivity.class);
                start(intent, view, data);
                break;
            case Attributes.FIELD_REGISTER:
                if (context instanceof RegisterActivity) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    return;
                }
                intent = new Intent(context, RegisterActivity.class);
                start(intent, view, data);
                break;
            case Attributes.FIELD_RESERVE:
                if (context instanceof ReserveActivity) {
                    drawer.closeDrawer(Gravity.RIGHT);
                    return;
                }
                intent = new Intent(context, ReserveActivity.class);
                start(intent, view, data);
                break;
            case InitDrawer.LOGOUT:
                logOut();
                break;
        }
    }

    private void logOut() {
        Util.get(this).edit()
                .remove(DoctorInstance.class.getSimpleName())
                .remove(UserInstance.class.getSimpleName())
                .remove(RegistryInstance.class.getSimpleName())
                .apply();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(EXIT));
        LaunchActivity.start(this);
    }

    private void start(Intent intent, View view, String data) {
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeBasic();
//        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    public void openDial(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        startActivity(intent);
    }

    public void openLink(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void openMail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        startActivity(Intent.createChooser(emailIntent, "مکاتبه با ایمیل"));
    }

    public void share(FactModel tag) {

    }

    public void share() {

    }
}
