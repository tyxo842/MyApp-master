package tyxo.mobilesafe.db.sqlite;

import java.util.List;

import tyxo.mobilesafe.db.exception.DbException;
import tyxo.mobilesafe.db.table.ColumnUtils;
import tyxo.mobilesafe.db.table.Finder;
import tyxo.mobilesafe.db.table.Table;

public class FinderLazyLoader<T> {
	private final Finder finderColumn;
	private final Object finderValue;

	public FinderLazyLoader(Finder finderColumn, Object value) {
		this.finderColumn = finderColumn;
		this.finderValue = ColumnUtils.convert2DbColumnValueIfNeeded(value);
	}

	public List<T> getAllFromDb() throws DbException {
		List<T> entities = null;
		Table table = finderColumn.getTable();
		if (table != null) {
			entities = table.db.findAll(Selector.from(
					finderColumn.getTargetEntityType()).where(
					finderColumn.getTargetColumnName(), "=", finderValue));
		}
		return entities;
	}

	public T getFirstFromDb() throws DbException {
		T entity = null;
		Table table = finderColumn.getTable();
		if (table != null) {
			entity = table.db.findFirst(Selector.from(
					finderColumn.getTargetEntityType()).where(
					finderColumn.getTargetColumnName(), "=", finderValue));
		}
		return entity;
	}
}
