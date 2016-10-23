package twittertest.bassem.com.twittertest.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import twittertest.bassem.com.twittertest.Models.Follower;

/**
 * Created by Bassem Samy on 10/21/2016.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "twitterormlite.db";
    private static final int DATABASE_VERSION = 2;

    private Dao<Follower, Integer> mFollowerDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Follower.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Follower.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* User */

    public Dao<Follower, Integer> getFollowerDao() throws SQLException {
        if (mFollowerDao == null) {
            mFollowerDao = getDao(Follower.class);
        }

        return mFollowerDao;
    }

    public void clearFollowerTable() throws SQLException {
        TableUtils.clearTable(connectionSource, Follower.class);
    }

    @Override
    public void close() {
        mFollowerDao = null;

        super.close();
    }
}