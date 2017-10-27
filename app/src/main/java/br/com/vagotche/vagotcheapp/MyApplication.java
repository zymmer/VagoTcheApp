package br.com.vagotche.vagotcheapp;

/**
 * Created by guilherme on 24/10/17.
 */

        import br.com.vagotche.vagotcheapp.util.IabHelper;
        import android.app.Application;

public class MyApplication extends Application {
    private IabHelper mHelper;

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public IabHelper getmHelper() {
        return mHelper;
    }
    public void setmHelper(IabHelper mHelper) {
        this.mHelper = mHelper;
    }
}