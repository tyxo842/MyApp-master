package tyxo.mobilesafe.db.table;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.List;

import tyxo.mobilesafe.db.exception.DbException;
import tyxo.mobilesafe.db.sqlite.ColumnDbType;
import tyxo.mobilesafe.db.sqlite.FinderLazyLoader;
import tyxo.mobilesafe.utils.log.HLog;

public class Finder extends Column {

	private final String valueColumnName;
	private final String targetColumnName;

	/* package */Finder(Class<?> entityType, Field field) {
		super(entityType, field);

		tyxo.mobilesafe.db.annotation.Finder finder = field
				.getAnnotation(tyxo.mobilesafe.db.annotation.Finder.class);
		this.valueColumnName = finder.valueColumn();
		this.targetColumnName = finder.targetColumn();
	}

	public Class<?> getTargetEntityType() {
		return ColumnUtils.getFinderTargetEntityType(this);
	}

	public String getTargetColumnName() {
		return targetColumnName;
	}

	@Override
	public void setValue2Entity(Object entity, Cursor cursor, int index) {
		Object value = null;
		Class<?> columnType = columnField.getType();
		Object finderValue = TableUtils.getColumnOrId(entity.getClass(),
				this.valueColumnName).getColumnValue(entity);
		if (columnType.equals(FinderLazyLoader.class)) {
			value = new FinderLazyLoader(this, finderValue);
		} else if (columnType.equals(List.class)) {
			try {
				value = new FinderLazyLoader(this, finderValue).getAllFromDb();
			} catch (DbException e) {
				HLog.e(getClass().getSimpleName(), e.getMessage());
			}
		} else {
			try {
				value = new FinderLazyLoader(this, finderValue)
						.getFirstFromDb();
			} catch (DbException e) {
				HLog.e(getClass().getSimpleName(), e.getMessage());
			}
		}

		if (setMethod != null) {
			try {
				setMethod.invoke(entity, value);
			} catch (Throwable e) {
				HLog.e(getClass().getSimpleName(), e.getMessage());
			}
		} else {
			try {
				this.columnField.setAccessible(true);
				this.columnField.set(entity, value);
			} catch (Throwable e) {
				HLog.e(getClass().getSimpleName(), e.getMessage());
			}
		}
	}

	@Override
	public Object getColumnValue(Object entity) {
		return null;
	}

	@Override
	public Object getDefaultValue() {
		return null;
	}

	@Override
	public ColumnDbType getColumnDbType() {
		return ColumnDbType.TEXT;
	}
}
