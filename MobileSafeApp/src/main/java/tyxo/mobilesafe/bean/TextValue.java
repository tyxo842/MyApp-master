package tyxo.mobilesafe.bean;

/**
 * Created by tyxo
 * created at 2016/10/8 16:15
 * des : 之所以这样写字段的名称,是因为后台不同接口返回的数据,有些接口返回同样字段的小写,有的是大写多有不便;
 */
public class TextValue {

    public String TEXT; //类型名称
    public String VALUE;//类型值
    public String text;
    public String value;
    public String Text;
    public String Value;

    public TextValue() {
    }

    public TextValue(String TEXT, String VALUE) {
        this.TEXT = TEXT;
        this.VALUE = VALUE;
    }

    public TextValue(String TEXT, String VALUE, String text, String value, String text1, String value1) {
        this.TEXT = TEXT;
        this.VALUE = VALUE;
        this.text = text;
        this.value = value;
        Text = text1;
        Value = value1;
    }

    public void setText(String text) {
        TEXT = text;
    }

    public void setValue(String value) {
        VALUE = value;
    }

    public String getText() {
        return TEXT != null ? TEXT : (text != null ? text : Text);
    }

    public String getValue() {
        return VALUE != null ? VALUE : (value != null ? value : VALUE);
    }
}
