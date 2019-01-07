
package tyxo.mobilesafe.db.exception;
/**
 * @deprecated: Database Exception，数据库异常
 * @Classname DbException
 * @date 2015-05-12 13:20
 */
public class DbException extends HHBaseException {
	private static final long serialVersionUID = 1L;
	
	public DbException() {}
	
	
	public DbException(String msg) {
		super(msg);
	}
	
	public DbException(Throwable ex) {
		super(ex);
	}
	
	public DbException(String msg,Throwable ex) {
		super(msg,ex);
	}
	
}
