package tyxo.mobilesafe.db.converter;

import android.database.Cursor;
import android.text.TextUtils;

import tyxo.mobilesafe.db.sqlite.ColumnDbType;

/**
 * Date: 13-11-4
 * Time: 下午10:51
 */
public class LongColumnConverter implements ColumnConverter<Long> {
    @Override
    public Long getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getLong(index);
    }

    @Override
    public Long getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) return null;
        return Long.valueOf(fieldStringValue);
    }

    @Override
    public Object fieldValue2ColumnValue(Long fieldValue) {
        return fieldValue;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
