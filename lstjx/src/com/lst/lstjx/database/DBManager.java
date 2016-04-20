package com.lst.lstjx.database;

import android.content.Context;

public class DBManager {

    private static DBManager instance;
    private com.lst.lstjx.database.DaoMaster daoMaster;
    private com.lst.lstjx.database.GroupDaoMaster groupDaoMaster;
    private DaoSession daoSession;
    private GroupDaoSession groupDaoSession;

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager(context);
                }
            }
        }
        return instance;
    }

    private DBManager(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                com.lst.lstjx.database.DaoMaster.OpenHelper helper = new com.lst.lstjx.database.DaoMaster.DevOpenHelper(context, context.getPackageName(), null);
                daoMaster = new com.lst.lstjx.database.DaoMaster(helper.getWritableDatabase());
            }
            daoSession = daoMaster.newSession();
        }
        if (groupDaoSession == null) {
        	if (groupDaoMaster == null) {
        		com.lst.lstjx.database.GroupDaoMaster.OpenHelper helper = new com.lst.lstjx.database.GroupDaoMaster.DevOpenHelper(context, context.getPackageName(), null);
        		groupDaoMaster = new com.lst.lstjx.database.GroupDaoMaster(helper.getWritableDatabase());
        	}
        	groupDaoSession = groupDaoMaster.newSession();
        }
    }

    public com.lst.lstjx.database.DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public void setDaoMaster(DaoMaster daoMaster) {
        this.daoMaster = daoMaster;
    }
 

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }
    public com.lst.lstjx.database.GroupDaoMaster getGroupDaoMaster() {
    	return groupDaoMaster;
    }
    
    public void setGroupDaoMaster(GroupDaoMaster groupDaoMaster) {
    	this.groupDaoMaster = groupDaoMaster;
    }
    public GroupDaoSession getGroupDaoSession() {
    	return groupDaoSession;
    }
    
    public void setGroupDaoSession(GroupDaoSession groupDaoSession) {
    	this.groupDaoSession = groupDaoSession;
    }
}
