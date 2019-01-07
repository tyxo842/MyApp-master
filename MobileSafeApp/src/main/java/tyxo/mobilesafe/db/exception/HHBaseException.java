 
package tyxo.mobilesafe.db.exception;
/**
 * @deprecated: base Exception，运行异常
 * @Classname DbException
 * @date 2015-05-12 13:25
 */
public class HHBaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public HHBaseException() {
		super();
	}
	
	public HHBaseException(String msg) {
		super(msg);
	}
	
	public HHBaseException(Throwable ex) {
		super(ex);
	}
	
	public HHBaseException(String msg,Throwable ex) {
		super(msg,ex);
	}

}
