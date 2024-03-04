/*
package com.pedro.streamer.data.local;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import haus.bello.R;
import haus.bello.ui.activities.login.LoginModel;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmModel;


public class RealmController {

    private static final String TAG = "RealmController";
    public static RealmController realmController;
    public static Realm realm;
    // initialize in mains
    public static void InitRealm(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(context.getResources().getString(R.string.app_name_ream))
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public static LoginModel getUser() {
        return getRealm().where(LoginModel.class).findFirst();
    }

    public static void setUser(LoginModel user) {
        getRealm().beginTransaction();
        getRealm().copyToRealmOrUpdate(user);
        getRealm().commitTransaction();
    }

    public static void closeRealm() {
        getRealm().close();
    }

    public static void clearDatabase() {
        getRealm().executeTransaction(realm -> realm.deleteAll());
    }

    public static void insertOrUpdate(RealmModel object) {
        getRealm().executeTransaction(realm -> realm.insertOrUpdate(object));
    }

    public static void insertOrUpdate(RealmList object) {
        getRealm().executeTransaction(realm -> realm.insertOrUpdate(object));
    }

    */
/****************** YOUR METHODS *************************************************************//*

    public static RealmController realmControllerInIt() {
        if (realmController == null) {
            realmController = new RealmController();
        }
        return realmController;
    }

    public static Realm getRealm() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    public static void copyToRealmOrUpdate(RealmModel object) {
        if (RealmController.getRealm() != null) {
            RealmController.getRealm().executeTransaction(realm -> realm.copyToRealmOrUpdate(object));
        }else {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> realm.copyToRealmOrUpdate(object));
        }
    }

    public void copyToRealmOrUpdate(RealmList objectList) {
        getRealm().executeTransaction(realm -> realm.copyToRealmOrUpdate(objectList));
    }
    public static void getUpdateLoginData(String idd,String url,String urlpath) {
        RealmController.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                LoginModel loginModel = realm.where(LoginModel.class).equalTo("id", idd).findFirst();
                if (loginModel != null) {
                    loginModel.setProfilePath(urlpath);
                    loginModel.setProfileImage(url);
                }
            }
        });
    }

    public static void getDeleteLoginData(String idd) {
        RealmController.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                LoginModel loginModel = realm.where(LoginModel.class).equalTo("id", idd).findFirst();
                if (loginModel != null) {
                    loginModel.deleteFromRealm();
                }
            }
        });
    }

}
*/
