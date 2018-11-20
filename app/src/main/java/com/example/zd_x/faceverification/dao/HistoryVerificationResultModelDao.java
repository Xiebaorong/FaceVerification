package com.example.zd_x.faceverification.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HISTORY_VERIFICATION_RESULT_MODEL".
*/
public class HistoryVerificationResultModelDao extends AbstractDao<HistoryVerificationResultModel, Long> {

    public static final String TABLENAME = "HISTORY_VERIFICATION_RESULT_MODEL";

    /**
     * Properties of entity HistoryVerificationResultModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property VerificationTime = new Property(1, String.class, "verificationTime", false, "VERIFICATION_TIME");
        public final static Property ImageId = new Property(2, String.class, "imageId", false, "IMAGE_ID");
        public final static Property FaceBase64 = new Property(3, String.class, "faceBase64", false, "FACE_BASE64");
        public final static Property IsVerification = new Property(4, boolean.class, "isVerification", false, "IS_VERIFICATION");
        public final static Property Total = new Property(5, int.class, "total", false, "TOTAL");
    }

    private DaoSession daoSession;


    public HistoryVerificationResultModelDao(DaoConfig config) {
        super(config);
    }
    
    public HistoryVerificationResultModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HISTORY_VERIFICATION_RESULT_MODEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"VERIFICATION_TIME\" TEXT," + // 1: verificationTime
                "\"IMAGE_ID\" TEXT," + // 2: imageId
                "\"FACE_BASE64\" TEXT," + // 3: faceBase64
                "\"IS_VERIFICATION\" INTEGER NOT NULL ," + // 4: isVerification
                "\"TOTAL\" INTEGER NOT NULL );"); // 5: total
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HISTORY_VERIFICATION_RESULT_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HistoryVerificationResultModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String verificationTime = entity.getVerificationTime();
        if (verificationTime != null) {
            stmt.bindString(2, verificationTime);
        }
 
        String imageId = entity.getImageId();
        if (imageId != null) {
            stmt.bindString(3, imageId);
        }
 
        String faceBase64 = entity.getFaceBase64();
        if (faceBase64 != null) {
            stmt.bindString(4, faceBase64);
        }
        stmt.bindLong(5, entity.getIsVerification() ? 1L: 0L);
        stmt.bindLong(6, entity.getTotal());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HistoryVerificationResultModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String verificationTime = entity.getVerificationTime();
        if (verificationTime != null) {
            stmt.bindString(2, verificationTime);
        }
 
        String imageId = entity.getImageId();
        if (imageId != null) {
            stmt.bindString(3, imageId);
        }
 
        String faceBase64 = entity.getFaceBase64();
        if (faceBase64 != null) {
            stmt.bindString(4, faceBase64);
        }
        stmt.bindLong(5, entity.getIsVerification() ? 1L: 0L);
        stmt.bindLong(6, entity.getTotal());
    }

    @Override
    protected final void attachEntity(HistoryVerificationResultModel entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public HistoryVerificationResultModel readEntity(Cursor cursor, int offset) {
        HistoryVerificationResultModel entity = new HistoryVerificationResultModel( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // verificationTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // imageId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // faceBase64
            cursor.getShort(offset + 4) != 0, // isVerification
            cursor.getInt(offset + 5) // total
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HistoryVerificationResultModel entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setVerificationTime(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setImageId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFaceBase64(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIsVerification(cursor.getShort(offset + 4) != 0);
        entity.setTotal(cursor.getInt(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(HistoryVerificationResultModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(HistoryVerificationResultModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HistoryVerificationResultModel entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
