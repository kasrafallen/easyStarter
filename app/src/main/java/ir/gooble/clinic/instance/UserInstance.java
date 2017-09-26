package ir.gooble.clinic.instance;

import android.content.Context;

import com.google.gson.Gson;

import ir.gooble.clinic.model.User;
import ir.gooble.clinic.util.Util;

public class UserInstance {

    private final static UserInstance instance = new UserInstance();
    private SignResult signResult;

    public static UserInstance getInstance() {
        return instance;
    }

    public SignResult getSignResult() {
        return signResult;
    }

    public void setSignResult(SignResult signResult) {
        this.signResult = signResult;
    }

    public interface SignResult {
        void onDone();

        void onDismiss();
    }

    private UserInstance() {

    }

    public static User getUser(Context context) {
        String data = Util.get(context).getString(UserInstance.class.getSimpleName(), null);
        if (data == null) {
            return null;
        } else {
            return new Gson().fromJson(data, User.class);
        }
    }

    public static void setUser(Context context, User user) {
        Util.get(context).edit().putString(UserInstance.class.getSimpleName(), new Gson().toJson(user)).apply();
    }

    public static boolean isEmpty(Context context) {
        return !Util.get(context).contains(UserInstance.class.getSimpleName());
    }
}
